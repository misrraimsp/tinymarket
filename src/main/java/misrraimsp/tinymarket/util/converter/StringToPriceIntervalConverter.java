package misrraimsp.tinymarket.util.converter;

import misrraimsp.tinymarket.util.enums.PriceInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPriceIntervalConverter implements Converter<String, PriceInterval> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public PriceInterval convert(String s) {
        int maxIndex = PriceInterval.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            LOGGER.warn("PriceInterval index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        if (index > maxIndex) {
            LOGGER.warn("PriceInterval index string out of bound. Defaults to index=0");
            index = 0;
        }
        return PriceInterval.values()[index];
    }
}
