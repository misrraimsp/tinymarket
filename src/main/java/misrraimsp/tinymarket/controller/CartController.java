package misrraimsp.tinymarket.controller;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.model.User;
import misrraimsp.tinymarket.service.CartServer;
import misrraimsp.tinymarket.service.ProductServer;
import misrraimsp.tinymarket.service.UserServer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartServer cartServer;
    private final ProductServer productServer;
    private final UserServer userServer;

    @GetMapping("/user/cart")
    public String showCart(Model model,
                           @AuthenticationPrincipal User authUser) {

        model.addAttribute("user", userServer.findById(authUser.getId()));
        return "cart";
    }

    @PostMapping("/user/cart/add")
    public String processCartAdd(@RequestParam Long productId,
                                 @AuthenticationPrincipal User authUser) {
        cartServer.addItem(productServer.findById(productId), userServer.findById(authUser.getId()).getCart());
        return "redirect:/user/products";
    }

    @PostMapping("/user/cart/increment")
    public String processItemIncrement(@RequestParam Long itemId,
                                       @AuthenticationPrincipal User authUser) {

        cartServer.incrementItem(itemId, userServer.findById(authUser.getId()).getCart());
        return "redirect:/user/cart";
    }

    @PostMapping("/user/cart/decrement")
    public String processItemDecrement(@RequestParam Long itemId,
                                       @AuthenticationPrincipal User authUser) {

        cartServer.decrementItem(itemId, userServer.findById(authUser.getId()).getCart());
        return "redirect:/user/cart";
    }
}
