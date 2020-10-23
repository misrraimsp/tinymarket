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
@Table(name = "item_pedido")
public class PedidoItem extends Item {

    private BigDecimal unitSalesPrice; //precio unitario de venta

    @ManyToOne
    @JoinColumn(name = "fk_pedido")
    private Pedido pedido;

    public BigDecimal getCompoundPrice() {
        return unitSalesPrice.multiply(new BigDecimal(quantity));
    }
}
