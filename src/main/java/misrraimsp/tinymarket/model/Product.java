package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.tinymarket.util.enums.Category;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BasicEntity {

    private BigDecimal price;

    private int stock;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Category category;
}
