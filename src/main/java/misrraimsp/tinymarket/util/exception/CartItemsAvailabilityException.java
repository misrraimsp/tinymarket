package misrraimsp.tinymarket.util.exception;


import misrraimsp.tinymarket.model.CartItem;

import java.util.List;

public class CartItemsAvailabilityException extends IllegalArgumentException {

    List<CartItem> itemsOutOfStock;

    public CartItemsAvailabilityException(List<CartItem> itemsOutOfStock) {
        super("Trying to checkout with cartItems that are not available");
        this.itemsOutOfStock = itemsOutOfStock;
    }

    public List<CartItem> getItemsOutOfStock() {
        return itemsOutOfStock;
    }

}
