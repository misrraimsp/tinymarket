package misrraimsp.tinymarket.data;

import misrraimsp.tinymarket.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem,Long> {
}
