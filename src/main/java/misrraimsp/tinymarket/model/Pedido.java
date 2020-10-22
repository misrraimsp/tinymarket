package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
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
    private PedidoInfo info;

    public void addPedidoItem(PedidoItem pedidoItem) {
        this.pedidoItems.add(pedidoItem);
        pedidoItem.setPedido(this);
    }
}
