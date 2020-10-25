package misrraimsp.tinymarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import misrraimsp.tinymarket.data.ProductRepository;
import misrraimsp.tinymarket.data.UserRepository;
import misrraimsp.tinymarket.model.PedidoInfo;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.model.User;
import misrraimsp.tinymarket.model.dto.UserDTO;
import misrraimsp.tinymarket.service.CartServer;
import misrraimsp.tinymarket.service.UserServer;
import misrraimsp.tinymarket.util.enums.Category;
import misrraimsp.tinymarket.util.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TinymarketApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServer userServer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartServer cartServer;

    private static UserDTO userDTO;


    private String convertToUrlEncoded(Object obj) {
        Map<String,Object> map = objectMapper.convertValue(obj, Map.class);
        return map.keySet()
                .stream()
                .map(key -> {
                    try {
                        String value = (String) map.get(key);
                        return (value != null && !value.isBlank()) ? key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8.toString()) : null;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(joining("&"));
    }

    private boolean checkPassword(PasswordEncoder passwordEncoder, String candidatePassword, String actualPassword) {
        return passwordEncoder.matches(candidatePassword, actualPassword);
    }

    private static void setUserDTO() {
        userDTO = new UserDTO();
        userDTO.setEmail("misrraim@tm.com");
        userDTO.setPassword("MADRID");
        userDTO.setMatchingPassword("MADRID");
    }


    @BeforeEach
    public void setUserRepository() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        userRepository.deleteAll();
    }


    @Test
    void contextLoads() {
    }

    @Test
    void processRegistration_whenValidUserDTO_persistTheUser() throws Exception {
        // given
        setUserDTO();

        // when
        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .content(this.convertToUrlEncoded(userDTO)));

        // then
        User persistedUser = userRepository.findByEmail(userDTO.getEmail());
        assertThat(persistedUser).isNotNull();
        assertThat(this.checkPassword(passwordEncoder,userDTO.getPassword(),persistedUser.getPassword())).isTrue();
    }

    @Test
    void processRegistration_whenPasswordsDoNotMatch_DoNotPersistTheUser() throws Exception {
        // given
        setUserDTO();
        userDTO.setMatchingPassword("BARCELONA");

        // when
        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .content(this.convertToUrlEncoded(userDTO)));

        // then
        User persistedUser = userRepository.findByEmail(userDTO.getEmail());
        assertThat(persistedUser).isNull();
    }

    @Test
    void processRegistration_whenEmailIsNotUnique_DoNotPersistTheUser() throws Exception {
        // given
        String sharedEmail = "misrraim@tm.com";

        setUserDTO();
        userDTO.setEmail(sharedEmail);

        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .content(this.convertToUrlEncoded(userDTO)));

        UserDTO userDTO_2 = new UserDTO();
        userDTO_2.setEmail(sharedEmail);
        userDTO_2.setPassword("BARCELONA");
        userDTO_2.setMatchingPassword("BARCELONA");

        // when
        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .content(this.convertToUrlEncoded(userDTO_2)));

        // then
        User persistedUser = userRepository.findByEmail(sharedEmail);
        assertThat(this.checkPassword(passwordEncoder,userDTO.getPassword(),persistedUser.getPassword())).isTrue();
        assertThat(this.checkPassword(passwordEncoder,userDTO_2.getPassword(),persistedUser.getPassword())).isFalse();
    }

    @Test
    void processRegistration_whenValidUserDTO_PersistTheUserWithCustomerRole() throws Exception {
        // given
        setUserDTO();

        // when
        mockMvc.perform(post("/register")
                .contentType("application/x-www-form-urlencoded")
                .content(this.convertToUrlEncoded(userDTO)));

        // then
        User persistedUser = userRepository.findByEmail(userDTO.getEmail());
        assertThat(persistedUser.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void processLogin_whenValidCredentials_LoginTheUserAndRedirectToCatalog() throws Exception {
        // given
        setUserDTO();
        userServer.persist(userDTO,passwordEncoder,null,null);

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType("application/x-www-form-urlencoded")
                .content("username=" +
                        URLEncoder.encode(userDTO.getEmail(),StandardCharsets.UTF_8.toString()) +
                        "&password=" +
                        URLEncoder.encode(userDTO.getPassword(),StandardCharsets.UTF_8.toString())
                ));

        // then
        resultActions
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/user/products"));
    }

    @Test
    void processLogin_whenNotValidCredentials_RedirectToLoginWithError() throws Exception {
        // given
        setUserDTO();
        userServer.persist(userDTO,passwordEncoder,null,null);

        // when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType("application/x-www-form-urlencoded")
                .content("username=" +
                        URLEncoder.encode(userDTO.getEmail(),StandardCharsets.UTF_8.toString()) +
                        "&password=" +
                        URLEncoder.encode("BARCELONA",StandardCharsets.UTF_8.toString())
                ));

        // then
        resultActions
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @WithMockUser
    //@Test
    void createPedido_whenValidArguments_createPedidoAndUpdateStock() throws Exception {
        // given
        setUserDTO();
        User user = userServer.persist(userDTO,passwordEncoder,null,null);

        int initialStock = 2;
        Product product = new Product();
        product.setTitle("Producto Test");
        product.setDescription("Producto de test número 1");
        product.setCategory(Category.ART);
        product.setPrice(BigDecimal.valueOf(10));
        product.setStock(initialStock);
        Product persistedProduct = productRepository.save(product);

        cartServer.addItem(product,user.getCart());

        PedidoInfo pedidoInfo = new PedidoInfo();
        pedidoInfo.setCard("123456789");
        pedidoInfo.setCity("City Test");
        pedidoInfo.setCountry("Country Test");
        pedidoInfo.setEmail(userDTO.getEmail());
        pedidoInfo.setLine1("Test Street, 99");
        pedidoInfo.setName("Test Name");
        pedidoInfo.setPostalCode("35500");
        pedidoInfo.setProvince("Province Test");

        // when
        mockMvc.perform(post("/user/checkout")
                .contentType("application/x-www-form-urlencoded")
                .content(this.convertToUrlEncoded(pedidoInfo)));

        // then
        assertThat(productRepository.findById(persistedProduct.getId()).get().getStock()).isEqualTo(initialStock - 1);
    }
}
