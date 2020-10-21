package misrraimsp.tinymarket.util.converter;

import misrraimsp.tinymarket.util.enums.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Category convert(String s) {
        int maxIndex = Category.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
            if (index > maxIndex) {
                LOGGER.error("Category index string out of bound. Defaults to index=0");
                index = 0;
            }
        }
        catch (NumberFormatException e) {
            LOGGER.error("Category index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        return Category.values()[index];
    }
}
