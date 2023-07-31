package tallerM2.tallerM2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.ERole;
import tallerM2.tallerM2.model.Role;
import tallerM2.tallerM2.model.User;
import tallerM2.tallerM2.repository.RoleRepository;
import tallerM2.tallerM2.repository.UserRepository;

@SpringBootApplication
public class TallerM2Application {

    public static void main(String[] args) {
        SpringApplication.run(TallerM2Application.class, args);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() throws ValueNotFound {
        Logger logger = LoggerFactory.getLogger(getClass());
        if (userRepository.count() != 0L) {
            logger.info("Using existing database");
            return;
        }
        logger.info("Generating demo data");
        logger.info("... generando Usuarios");
        createAdmin("leiniercy", "leiniercy@uci.cu", "admin");

    }

    private User createAdmin(String username, String email, String password) throws ValueNotFound {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        if (userRole.isEmpty()) {
            throw new ValueNotFound("role not found");
        }
        roles.add(userRole.get());
        user.setRoles(roles);
        userRepository.saveAndFlush(user);
        return user;
    }

}
