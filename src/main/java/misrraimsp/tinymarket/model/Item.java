package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Item extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "fk_product")
    private Product product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fk_cart")
    private Cart cart;

    public BigDecimal getPrice() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

}
