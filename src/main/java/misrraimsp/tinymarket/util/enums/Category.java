package misrraimsp.tinymarket.util.enums;

public enum Category {

    SCIENCE("Science"),
    ART("Art"),
    HISTORY("History"),
    GARDEN("Gardening"),
    SPORT("Sport"),
    OTHER("Other");

    private final String text;

    Category(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
