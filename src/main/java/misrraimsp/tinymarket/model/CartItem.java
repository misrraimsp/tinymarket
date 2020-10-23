package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "item_cart")
public class CartItem extends Item {

    @ManyToOne
    @JoinColumn(name = "fk_cart")
    private Cart cart;

    public BigDecimal getPrice() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

}
