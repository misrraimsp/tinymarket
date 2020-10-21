package misrraimsp.tinymarket.util;

import misrraimsp.tinymarket.data.ProductRepository;
import misrraimsp.tinymarket.data.UserRepository;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.model.dto.UserDTO;
import misrraimsp.tinymarket.service.UserServer;
import misrraimsp.tinymarket.util.enums.Category;
import misrraimsp.tinymarket.util.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataLoader_H2 {
    private static final Logger log = LoggerFactory.getLogger(DataLoader_H2.class);

    @Bean
    CommandLineRunner initDatabase(UserServer userServer,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   ProductRepository productRepository) {

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
            userServer.persist(andrea,passwordEncoder, Role.CUSTOMER);

            UserDTO admin = new UserDTO();
            admin.setEmail("admin@tm.com");
            admin.setPassword("admin");
            admin.setMatchingPassword("admin");
            userServer.persist(admin,passwordEncoder,Role.ADMINISTRATOR);

            userRepository.findAll().forEach(person -> log.info("Loaded " + person.getEmail()));

            Product p1 = new Product();
            p1.setTitle("Producto 1");
            p1.setDescription("Producto de test número 1");
            p1.setCategory(Category.ART);
            p1.setPrice(BigDecimal.valueOf(10));
            p1.setStock(15);
            productRepository.save(p1);

            Product p2 = new Product();
            p2.setTitle("Producto 2");
            p2.setDescription("Producto de test número 2");
            p2.setCategory(Category.ART);
            p2.setPrice(BigDecimal.valueOf(20));
            p2.setStock(30);
            productRepository.save(p2);

            Product p3 = new Product();
            p3.setTitle("Producto 3");
            p3.setDescription("Producto de test número 3");
            p3.setCategory(Category.SCIENCE);
            p3.setPrice(BigDecimal.valueOf(15));
            p3.setStock(0);
            productRepository.save(p3);

            Product p4 = new Product();
            p4.setTitle("Producto 4");
            p4.setDescription("Producto de test número 4");
            p4.setCategory(Category.ART);
            p4.setPrice(BigDecimal.valueOf(19));
            p4.setStock(15);
            productRepository.save(p4);

            Product p5 = new Product();
            p5.setTitle("Producto 5");
            p5.setDescription("Producto de test número 5");
            p5.setCategory(Category.SCIENCE);
            p5.setPrice(BigDecimal.valueOf(99));
            p5.setStock(150);
            productRepository.save(p5);

            Product p6 = new Product();
            p6.setTitle("Producto 6");
            p6.setDescription("Producto de test número 6");
            p6.setCategory(Category.ART);
            p6.setPrice(BigDecimal.valueOf(1));
            p6.setStock(1555);
            productRepository.save(p6);

            productRepository.findAll().forEach(product -> log.info("Loaded " + product.getTitle()));

        };

    }
}
