package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.PedidoInfoRepository;
import misrraimsp.tinymarket.data.PedidoItemRepository;
import misrraimsp.tinymarket.data.PedidoRepository;
import misrraimsp.tinymarket.model.*;
import misrraimsp.tinymarket.util.exception.CartItemsAvailabilityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServer {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final PedidoInfoRepository pedidoInfoRepository;
    private final ProductServer productServer;
    private final UserServer userServer;

    @Transactional
    public void createPedido(User user, PedidoInfo pedidoInfo) throws CartItemsAvailabilityException {
        List<CartItem> cartItems = new ArrayList<>(user.getCart().getCartItems());
        if (cartItems.isEmpty()) return;
        productServer.checkAvailabilityFor(cartItems);
        productServer.removeFromStock(cartItems);
        Pedido pedido = pedidoRepository.save(new Pedido());
        pedido.setInfo(pedidoInfoRepository.save(pedidoInfo));
        cartItems.forEach(cartItem -> {
            PedidoItem pedidoItem = pedidoItemRepository.save(new PedidoItem());
            pedidoItem.setProduct(cartItem.getProduct());
            pedidoItem.setQuantity(cartItem.getQuantity());
            pedidoItem.setUnitSalesPrice(cartItem.getProduct().getPrice());
            pedido.addPedidoItem(pedidoItem);
            pedidoItemRepository.save(pedidoItem);
        });
        pedidoRepository.save(pedido);
        user.addPedido(pedido);
        userServer.resetCart(user);
        userServer.persist(user);
    }
}
