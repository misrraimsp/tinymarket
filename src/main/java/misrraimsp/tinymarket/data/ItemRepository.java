package misrraimsp.tinymarket.data;

import misrraimsp.tinymarket.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
