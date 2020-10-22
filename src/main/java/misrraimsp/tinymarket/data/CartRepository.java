package misrraimsp.tinymarket.data;

import misrraimsp.tinymarket.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
