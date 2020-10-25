package misrraimsp.tinymarket.model;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
public class Cart extends BasicEntity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public int getSize(){
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getPrice() {
        return cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String toString() {
        return "Cart(id=" + this.getId() + ")";
    }
}
