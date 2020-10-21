package misrraimsp.tinymarket.model.dto;

import lombok.Data;
import misrraimsp.tinymarket.util.validation.PasswordMatches;

@Data
@PasswordMatches
public class UserDTO {
    private String email;
    private String password;
    private String matchingPassword;
}
