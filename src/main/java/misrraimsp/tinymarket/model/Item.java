package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class Item extends BasicEntity {

    @ManyToOne
    @JoinColumn(name = "fk_product")
    protected Product product;

    protected int quantity;
}
