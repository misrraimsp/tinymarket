package misrraimsp.tinymarket.model;

import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "item_cart")
public class CartItem extends Item {

    @ManyToOne
    @JoinColumn(name = "fk_cart")
    private Cart cart;

    public CartItem() {
    }

    public BigDecimal getPrice() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String toString() {
        return "CartItem(id=" + this.getId() + ")";
    }
}
