package misrraimsp.tinymarket.util.converter;

import misrraimsp.tinymarket.util.enums.StatusPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToStatusPedidoConverter implements Converter<String, StatusPedido> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public StatusPedido convert(String s) {
        int maxIndex = StatusPedido.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
            if (index > maxIndex) {
                LOGGER.error("StatusPedido index string out of bound. Defaults to index=0");
                index = 0;
            }
        }
        catch (NumberFormatException e) {
            LOGGER.error("StatusPedido index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        return StatusPedido.values()[index];
    }
}
