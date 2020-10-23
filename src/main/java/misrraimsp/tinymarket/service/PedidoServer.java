package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.PedidoInfoRepository;
import misrraimsp.tinymarket.data.PedidoItemRepository;
import misrraimsp.tinymarket.data.PedidoRepository;
import misrraimsp.tinymarket.model.*;
import misrraimsp.tinymarket.util.enums.StatusPedido;
import misrraimsp.tinymarket.util.exception.CartItemsAvailabilityException;
import misrraimsp.tinymarket.util.exception.EntityNotFoundByIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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


    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long pedidoId) throws EntityNotFoundByIdException {
        return pedidoRepository.findById(pedidoId).orElseThrow(() ->
                new EntityNotFoundByIdException(pedidoId,Pedido.class.getSimpleName()));
    }

    public Pedido persist(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void createPedido(User user, PedidoInfo pedidoInfo) throws CartItemsAvailabilityException {
        List<CartItem> cartItems = new ArrayList<>(user.getCart().getCartItems());
        if (cartItems.isEmpty()) return;
        productServer.checkAvailabilityFor(cartItems);
        productServer.removeFromStock(cartItems);
        Pedido pedido = pedidoRepository.save(new Pedido());
        pedido.setDate(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PROCESSING);
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

    public void editStatusPedido(Long pedidoId, StatusPedido status) {
        Pedido pedido = this.findById(pedidoId);
        pedido.setStatus(status);
        this.persist(pedido);
    }
}
