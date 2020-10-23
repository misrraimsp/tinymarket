package misrraimsp.tinymarket.util;

import misrraimsp.tinymarket.data.ProductRepository;
import misrraimsp.tinymarket.data.UserRepository;
import misrraimsp.tinymarket.model.Cart;
import misrraimsp.tinymarket.model.CartItem;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.model.dto.UserDTO;
import misrraimsp.tinymarket.service.CartServer;
import misrraimsp.tinymarket.service.CartItemServer;
import misrraimsp.tinymarket.service.UserServer;
import misrraimsp.tinymarket.util.enums.Category;
import misrraimsp.tinymarket.util.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Profile("dev-h2")
@Configuration
public class DataLoader_H2 {
    private static final Logger log = LoggerFactory.getLogger(DataLoader_H2.class);

    @Bean
    CommandLineRunner initDatabase(UserServer userServer,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   ProductRepository productRepository,
                                   CartItemServer cartItemServer,
                                   CartServer cartServer) {

        return args -> {

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

            Cart cartMisrra = cartServer.persist(new Cart());

            CartItem cartItem1 = new CartItem();
            cartItem1.setProduct(p1);
            cartItem1.setQuantity(3);
            cartMisrra.addCartItem(cartItem1);
            cartItemServer.persist(cartItem1);

            CartItem cartItem2 = new CartItem();
            cartItem2.setProduct(p2);
            cartItem2.setQuantity(1);
            cartMisrra.addCartItem(cartItem2);
            cartItemServer.persist(cartItem2);

            cartServer.persist(cartMisrra);

            UserDTO misrra = new UserDTO();
            misrra.setEmail("misrraim@tm.com");
            misrra.setPassword("misrra");
            misrra.setMatchingPassword("misrra");
            userServer.persist(misrra,passwordEncoder,null, cartMisrra);

            UserDTO andrea = new UserDTO();
            andrea.setEmail("andrea@tm.com");
            andrea.setPassword("andrea");
            andrea.setMatchingPassword("andrea");
            userServer.persist(andrea,passwordEncoder, Role.CUSTOMER, null);

            UserDTO admin = new UserDTO();
            admin.setEmail("admin@tm.com");
            admin.setPassword("admin");
            admin.setMatchingPassword("admin");
            userServer.persist(admin,passwordEncoder,Role.ADMINISTRATOR, null);

            userRepository.findAll().forEach(person -> log.info("Loaded " + person.getEmail()));

        };

    }
}
