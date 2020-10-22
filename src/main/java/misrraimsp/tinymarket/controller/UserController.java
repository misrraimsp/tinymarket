package misrraimsp.tinymarket.controller;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.model.PedidoInfo;
import misrraimsp.tinymarket.model.User;
import misrraimsp.tinymarket.model.dto.UserDTO;
import misrraimsp.tinymarket.service.PedidoServer;
import misrraimsp.tinymarket.service.ProductServer;
import misrraimsp.tinymarket.service.UserServer;
import misrraimsp.tinymarket.util.exception.CartItemsAvailabilityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final UserServer userServer;
    private final ProductServer productServer;
    private final PedidoServer pedidoServer;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String redirectHome(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(@AuthenticationPrincipal User authUser) {

        if (authUser != null) {
            LOGGER.warn("Authenticated user trying to GET /login (id={})", authUser.getId());
            return "redirect:/user/products";
        }
        return "loginForm";
    }

    @GetMapping("/register")
    public String showNewUserForm(Model model,
                                  @AuthenticationPrincipal User authUser) {

        if (authUser == null) {
            model.addAttribute("userDTO", new UserDTO());
            return "registerForm";
        }
        LOGGER.warn("Authenticated user trying to GET /register (id={})", authUser.getId());
        return "redirect:/user/products";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid UserDTO dto,
                                      Errors errors,
                                      @AuthenticationPrincipal User authUser) {

        if (authUser != null) {
            LOGGER.warn("Authenticated user trying to POST /register (id={})", authUser.getId());
            return "redirect:/user/products";
        }
        // error checks
        boolean hasError = false;
        if (errors.hasGlobalErrors()) {
            hasError = true;
            errors.getGlobalErrors().forEach(objectError -> {
                if ("PasswordMatches".equals(Objects.requireNonNull(objectError.getCode()))) {
                    errors.rejectValue("matchingPassword", "password.notMatching", Objects.requireNonNull(objectError.getDefaultMessage()));
                    LOGGER.info("Passwords does not match: {}", objectError.toString());
                } else {
                    LOGGER.warn("There has been an unexpected global error: {}", objectError.toString());
                }
            });
        }
        // email already exists
        else if (userServer.emailExists(dto.getEmail())) {
            hasError = true;
            errors.rejectValue("email", "email.notUnique");
            LOGGER.info("Trying to registrate a new user with an already used email={}", dto.getEmail());
        }
        if (hasError) {
            return "registerForm";
        }
        // persist
        Long userId = userServer.persist(dto, passwordEncoder, null, null).getId();
        LOGGER.info("User registered (id={})", userId);
        return "redirect:/login";
    }

    @GetMapping("/user/cart")
    public String showCart(Model model,
                           @AuthenticationPrincipal User authUser) {

        model.addAttribute("user", userServer.findById(authUser.getId()));
        return "cart";
    }

    @PostMapping("/user/cart/add")
    public String processCartAdd(@RequestParam Long productId,
                                 @AuthenticationPrincipal User authUser) {

        userServer.addProductToCart(productServer.findById(productId), authUser.getId());
        return "redirect:/user/products";
    }

    @PostMapping("/user/cart/increment")
    public String processItemIncrement(@RequestParam Long itemId,
                                       @AuthenticationPrincipal User authUser) {

        userServer.incrementCartItem(itemId, authUser.getId());
        return "redirect:/user/cart";
    }

    @PostMapping("/user/cart/decrement")
    public String processItemDecrement(@RequestParam Long itemId,
                                       @AuthenticationPrincipal User authUser) {

        userServer.decrementCartItem(itemId, authUser.getId());
        return "redirect:/user/cart";
    }

    @GetMapping("/user/checkout")
    public String showCheckoutForm(Model model,
                                   @AuthenticationPrincipal User authUser) {

        model.addAttribute("user", userServer.findById(authUser.getId()));
        model.addAttribute("info", new PedidoInfo());
        return "checkoutForm";
    }

    @PostMapping("/user/checkout")
    public String processCheckout(PedidoInfo pedidoInfo,
                                  @AuthenticationPrincipal User authUser) {

        User user = userServer.findById(authUser.getId());
        try {
            pedidoServer.createPedido(user, pedidoInfo);
        } catch (CartItemsAvailabilityException e) {
            LOGGER.info("Trying to buy more products than availability allows");
            return "redirect:/user/cart";
        }
        return "redirect:/user/products";
    }
}
