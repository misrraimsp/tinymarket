package misrraimsp.tinymarket.util.validation;

import misrraimsp.tinymarket.model.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDTO> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initialize(PasswordMatches constraintAnnotation) { }

    @Override
    public boolean isValid(UserDTO dto, ConstraintValidatorContext context){
        boolean isValid = dto.getPassword().equals(dto.getMatchingPassword());
        LOGGER.debug("Password matching condition: {}", isValid);
        return isValid;
    }

}
