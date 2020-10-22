package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cart extends BasicEntity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        this.items.add(item);
        item.setCart(this);
    }

    public int getSize(){
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public BigDecimal getPrice() {
        return items.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
