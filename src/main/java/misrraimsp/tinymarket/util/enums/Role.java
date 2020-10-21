package misrraimsp.tinymarket.util.enums;

public enum Role {

    CUSTOMER("ROLE_USER"),
    ADMINISTRATOR("ROLE_ADMIN");

    private final String text;

    Role(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
