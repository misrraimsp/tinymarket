package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cart extends BasicEntity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

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
}
