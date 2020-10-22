package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.CartItemRepository;
import misrraimsp.tinymarket.model.Cart;
import misrraimsp.tinymarket.model.CartItem;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.util.exception.EntityNotFoundByIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CartItemServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CartItemRepository cartItemRepository;


    public CartItem persist(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public CartItem findById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundByIdException(cartItemId, CartItem.class.getSimpleName()));
    }

    public CartItem create(Product product, Cart cart) throws EntityNotFoundByIdException {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cart.addCartItem(cartItem);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        LOGGER.info("CartItem(id={}) created (productId={})", savedCartItem.getId(), product.getId());
        return savedCartItem;
    }

    public void increment(Long id) throws EntityNotFoundByIdException {
        CartItem cartItem = this.findById(id);
        cartItem.setQuantity(1 + cartItem.getQuantity());
        cartItemRepository.save(cartItem);
        LOGGER.info("CartItem(id={}) incremented (new quantity = {})", cartItem.getId(), cartItem.getQuantity());
    }

    public void decrement(Long id) throws EntityNotFoundByIdException {
        CartItem cartItem = this.findById(id);
        cartItem.setQuantity(cartItem.getQuantity() - 1);
        cartItemRepository.save(cartItem);
        LOGGER.info("CartItem(id={}) decremented (new quantity = {})", cartItem.getId(), cartItem.getQuantity());
    }

    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
        LOGGER.info("CartItem(id={}) deleted", cartItem.getId());
    }


}
