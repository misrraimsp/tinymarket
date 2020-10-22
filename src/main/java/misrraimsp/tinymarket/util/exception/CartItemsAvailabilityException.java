package misrraimsp.tinymarket.util.exception;


import misrraimsp.tinymarket.model.CartItem;

import java.util.List;

public class CartItemsAvailabilityException extends IllegalArgumentException {

    List<CartItem> cartItemsOutOfStock;

    public CartItemsAvailabilityException(List<CartItem> cartItemsOutOfStock) {
        super("Trying to checkout with cartItems that are not available");
        this.cartItemsOutOfStock = cartItemsOutOfStock;
    }

    public List<CartItem> getCartItemsOutOfStock() {
        return cartItemsOutOfStock;
    }

}
