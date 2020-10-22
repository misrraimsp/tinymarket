package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.CartRepository;
import misrraimsp.tinymarket.model.Cart;
import misrraimsp.tinymarket.model.CartItem;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.util.exception.EntityNotFoundByIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final CartRepository cartRepository;
    private final CartItemServer cartItemServer;

    public Cart persist(Cart cart) {
        return cartRepository.save(cart);
    }

    public void addProduct(Product product, Cart cart) throws EntityNotFoundByIdException {
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        List<Long> matchingItemIds =
                cartItems
                        .stream()
                        .filter(item -> item.getProduct().equals(product))
                        .map(CartItem::getId)
                        .collect(Collectors.toList())
                ;
        if (matchingItemIds.isEmpty()) {
            cartItemServer.create(product, cart);
        } else if (matchingItemIds.size() == 1) {
            cartItemServer.increment(matchingItemIds.get(0));
        } else {
            LOGGER.error("Trying to add a product(id={}) that is already bound to several items(ids={}) within the cart(id={})",
                    product.getId(), matchingItemIds.toString(), cart.getId());
            return;
        }
        cartRepository.save(cart);
        LOGGER.info("Product(id={}) added into cart(id={})", product.getId(), cart.getId());
    }

    public void incrementItem(Long cartItemId, Cart cart) throws EntityNotFoundByIdException {
        CartItem cartItemToIncrement = cartItemServer.findById(cartItemId);
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        if (!cartItems.contains(cartItemToIncrement)) {
            LOGGER.error("Trying to increment a cartItem(id={}) that is not present in the cart(id{})", cartItemId, cart.getId());
            return;
        }
        cartItemServer.increment(cartItemId);
        cartRepository.save(cart);
        LOGGER.info("CartItem(id={}) incremented inside cart(id={})", cartItemId, cart.getId());
    }

    public void decrementItem(Long cartItemId, Cart cart) throws EntityNotFoundByIdException {
        CartItem cartItemToDecrement = cartItemServer.findById(cartItemId);
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        if (!cartItems.contains(cartItemToDecrement)) {
            LOGGER.error("Trying to decrement a cartItem(id={}) that is not present in the cart(id{})", cartItemId, cart.getId());
            return;
        }
        if (cartItemToDecrement.getQuantity() > 1){
            cartItemServer.decrement(cartItemToDecrement.getId());
            cartRepository.save(cart);
            LOGGER.info("CartItem(id={}) decremented inside cart(id={})", cartItemId, cart.getId());
        }
        else {
            cartItems.remove(cartItemToDecrement);
            cart.setCartItems(cartItems);
            cartRepository.save(cart);
            cartItemServer.delete(cartItemToDecrement);
            LOGGER.info("Last product of cartItem(id={}) removed inside cart(id={}). CartItem itself also removed", cartItemId, cart.getId());
        }
    }

    public void resetCart(Cart cart) {
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        cartItems.forEach(cartItem -> this.removeCartItem(cart, cartItem.getId()));
        LOGGER.info("Cart(id={}) reset", cart.getId());
    }

    public void removeCartItem(Cart cart, Long cartItemId) throws EntityNotFoundByIdException {
        //update cart
        CartItem deletingCartItem = cartItemServer.findById(cartItemId);
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        if (!cartItems.contains(deletingCartItem)) {
            LOGGER.error("Trying to remove a cartItem(id={}) that is not present in the cart(id{})", cartItemId, cart.getId());
            return;
        }
        cartItems.remove(deletingCartItem);
        cart.setCartItems(cartItems);
        this.persist(cart);
        LOGGER.info("CartItem(id={}) removed from cart(id={})", cartItemId, cart.getId());
        //delete item
        cartItemServer.delete(deletingCartItem);
    }
}
