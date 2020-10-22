package misrraimsp.tinymarket.data;

import misrraimsp.tinymarket.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
}
