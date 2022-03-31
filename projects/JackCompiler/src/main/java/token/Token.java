package token;

public interface Token {

    String getType();

    String getValue();

    default String getOpeningTag() {
        return "<" + getType() + ">";
    }

    default String getClosingTag() {
        return "</" + getType() + ">";
    }
}
