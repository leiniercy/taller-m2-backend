/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import tallerM2.tallerM2.configuration.dto.JwtResponse;
import tallerM2.tallerM2.configuration.dto.SignInRequest;
import tallerM2.tallerM2.configuration.dto.SignUpRequest;
import tallerM2.tallerM2.configuration.jwt.JwtUtil;
import tallerM2.tallerM2.configuration.service.UserDetailsImpl;
import tallerM2.tallerM2.model.ERole;
import tallerM2.tallerM2.model.Role;
import tallerM2.tallerM2.model.User;
import tallerM2.tallerM2.repository.RoleRepository;
import tallerM2.tallerM2.repository.UserRepository;
import tallerM2.tallerM2.utils.EmailSenderService;
import java.security.SecureRandom;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.services.servicesImpl.UserService;
import tallerM2.tallerM2.utils.Util;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AuthController {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Autowired
    private EmailSenderService senderService;
    @Autowired
    private UserService userService;

    public AuthController(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is already taken");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is already taken");
        }
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
        if (userRole.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
        }
        roles.add(userRole.get());
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setTaller(signUpRequest.getTaller());
        user.setPassword(hashedPassword);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("User registered success");
    }
    
    @GetMapping("/get/{email}")
    public ResponseEntity<?> findByEmail( @PathVariable(value = "email") String email ) throws ValueNotFound, BadRequest{
    try {
        return ResponseEntity.ok(userService.findByEmail(email));
        } catch (ValueNotFound vn) {
            throw new ValueNotFound(vn.getMessage());
        }catch (BadRequest bd) {
            throw new BadRequest("Bad request");
        }
    
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<String> sendEmail( @PathVariable(value = "email") String email ) throws BadRequest {
        try {
        String claveAleatoria = Util.generarClave(8); // Generar una clave aleatoria de 8 caracteres
                   senderService.sendSimpleEmail(email,
                "Taller, cambio de contraseña",
                "Su nueva contraseña es " + claveAleatoria );
        return ResponseEntity.ok(claveAleatoria);    
        } catch (Exception ex) {
            throw new BadRequest("Bad request: " + ex.getMessage());
        }
   
    }

}
