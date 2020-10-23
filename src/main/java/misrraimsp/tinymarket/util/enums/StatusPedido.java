package misrraimsp.tinymarket.util.enums;

public enum StatusPedido {

    COMPLETED("Completed"),
    PROCESSING("Processing"),
    SHIPPING("In shipment");

    private final String text;

    StatusPedido(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
