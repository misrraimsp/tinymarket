package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.tinymarket.util.enums.StatusPedido;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Pedido extends BasicEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido")
    private List<PedidoItem> pedidoItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_user")
    private User user;

    @OneToOne
    @JoinColumn(name = "fk_info_pedido")
    private PedidoInfo info;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private LocalDateTime date;

    public void addPedidoItem(PedidoItem pedidoItem) {
        this.pedidoItems.add(pedidoItem);
        pedidoItem.setPedido(this);
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public BigDecimal getTotalPrice() {
        return pedidoItems
                .stream()
                .map(PedidoItem::getCompoundPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

}
