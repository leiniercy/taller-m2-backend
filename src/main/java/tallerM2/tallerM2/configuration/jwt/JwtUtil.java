/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.configuration.jwt;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import tallerM2.tallerM2.configuration.dto.UserResponse;
import tallerM2.tallerM2.configuration.service.UserDetailsImpl;
import tallerM2.tallerM2.model.User;
import tallerM2.tallerM2.repository.UserRepository;
import tallerM2.tallerM2.utils.Util;

@Component
@Log4j2
public class JwtUtil {
    @Value("${taller2M.app.jwtSecret}")
    private String jwtSecret;

    @Value("${taller2M.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    UserRepository repository;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        //Extraemos el nombre del usuario y los roles
        String userName = userPrincipal.getUsername();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        Optional<User> op = repository.findByUsername(userName);
        //Creamos un usuario con la informacion que necesitamos
        UserResponse userResponse = new UserResponse();
        userResponse.setId(op.get().getId());
        userResponse.setName(userName);
        userResponse.setEmail(op.get().getEmail());
        userResponse.setTaller(op.get().getTaller());
        userResponse.setRoles(roles);

        //Creamos un estructura de datos con la informacion
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", Util.convertToDto(userResponse, UserResponse.class));

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) //Nombre del usuario
                .setClaims(claims) //Informacion del usuario
                .setIssuedAt(new Date()) //Fecha de creacion
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) //Fecha de expiracion
                .signWith(SignatureAlgorithm.HS512, jwtSecret) //el toquen se firma con mi clave se secreta
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}