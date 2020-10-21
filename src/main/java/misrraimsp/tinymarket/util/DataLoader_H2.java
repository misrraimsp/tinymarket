package misrraimsp.tinymarket.util;

import misrraimsp.tinymarket.data.UserRepository;
import misrraimsp.tinymarket.model.dto.UserDTO;
import misrraimsp.tinymarket.service.UserServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader_H2 {
    private static final Logger log = LoggerFactory.getLogger(DataLoader_H2.class);

    @Bean
    CommandLineRunner initDatabase(UserServer userServer,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {

        return args -> {

            UserDTO misrra = new UserDTO();
            misrra.setEmail("misrraim@tm.com");
            misrra.setPassword("misrra");
            misrra.setMatchingPassword("misrra");
            userServer.persist(misrra,passwordEncoder,null);

            UserDTO andrea = new UserDTO();
            andrea.setEmail("andrea@tm.com");
            andrea.setPassword("andrea");
            andrea.setMatchingPassword("andrea");
            userServer.persist(andrea,passwordEncoder,Role.CUSTOMER);

            UserDTO admin = new UserDTO();
            admin.setEmail("admin@tm.com");
            admin.setPassword("admin");
            admin.setMatchingPassword("admin");
            userServer.persist(admin,passwordEncoder,Role.ADMINISTRATOR);

            userRepository.findAll().forEach(person -> log.info("Loaded " + person.getEmail()));

        };

    }
}
