package misrraimsp.tinymarket.controller;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.model.PedidoInfo;
import misrraimsp.tinymarket.model.User;
import misrraimsp.tinymarket.service.PedidoServer;
import misrraimsp.tinymarket.service.UserServer;
import misrraimsp.tinymarket.util.enums.StatusPedido;
import misrraimsp.tinymarket.util.exception.CartItemsAvailabilityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PedidoController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final UserServer userServer;
    private final PedidoServer pedidoServer;

    @GetMapping("/user/checkout")
    public String showCheckoutForm(Model model,
                                   @AuthenticationPrincipal User authUser) {

        model.addAttribute("user", userServer.findById(authUser.getId()));
        model.addAttribute("info", new PedidoInfo());
        return "checkoutForm";
    }

    @PostMapping("/user/checkout")
    public String processCheckout(PedidoInfo pedidoInfo,
                                  Model model,
                                  @AuthenticationPrincipal User authUser) {

        User user = userServer.findById(authUser.getId());
        try {
            pedidoServer.createPedido(user, pedidoInfo);
        } catch (CartItemsAvailabilityException e) {
            LOGGER.info("Trying to buy more products than availability allows");
            model.addAttribute("user", user);
            model.addAttribute("cartItemsOutOfStock", e.getCartItemsOutOfStock());
            return "cart";
        }
        return "redirect:/user/products";
    }

    @GetMapping("/admin/pedidos")
    public String showPedidos(Model model,
                              @AuthenticationPrincipal User authUser) {

        model.addAttribute("user", userServer.findById(authUser.getId()));
        model.addAttribute("pedidos", pedidoServer.findAll());
        return "pedidos";
    }

    @PostMapping("/admin/pedidos")
    public String processPedidoStatusChange(@RequestParam Long pedidoId,
                                            @RequestParam StatusPedido status,
                                            Model model,
                                            @AuthenticationPrincipal User authUser) {

        pedidoServer.editStatusPedido(pedidoId, status);
        model.addAttribute("user", userServer.findById(authUser.getId()));
        model.addAttribute("pedidos", pedidoServer.findAll());
        return "pedidos";
    }
}
