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
public class PedidoItem extends Item {

    private BigDecimal unitSalesPrice; //precio unitario de venta

    @ManyToOne
    @JoinColumn(name = "fk_pedido")
    private Pedido pedido;

    public BigDecimal getCompoundPrice() {
        return unitSalesPrice.multiply(new BigDecimal(quantity));
    }
}
