package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class OrderInfo extends BasicEntity {
    private String name;
    private String email;
    private String phone;
    private String line1;
    private String line2;
    private String city;
    private String postalCode;
    private String country;
    private String province;
    private String card;
}
