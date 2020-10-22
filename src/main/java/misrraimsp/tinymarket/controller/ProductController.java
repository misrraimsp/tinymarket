package misrraimsp.tinymarket.controller;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.model.User;
import misrraimsp.tinymarket.service.ProductServer;
import misrraimsp.tinymarket.service.UserServer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductServer productServer;
    private final UserServer userServer;

    @GetMapping("/user/products")
    public String searchProducts(@RequestParam(defaultValue = "") String text,
                                 @RequestParam(defaultValue = "") String category,
                                 @RequestParam(defaultValue = "") String price,
                                 Model model,
                                 @AuthenticationPrincipal User authUser) {

        model.addAttribute("products", productServer.findSearchResults(text, category, price));
        model.addAttribute("user", userServer.findById(authUser.getId()));
        return "products";
    }
}
