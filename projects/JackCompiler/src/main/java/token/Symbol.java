package token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Symbol implements Token {

    private String value;

    public Symbol(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "symbol";
    }

    @Override
    public String getValue() {
        return technicalTransformation(this.value);
    }

    private String technicalTransformation(String value) {
        return switch (value) {
            case "<" -> "&lt;";
            case ">" -> "&gt;";
            case "\"" -> "&quot;";
            case "&" -> "&amp;";
            default -> value;
        };
    }

    public static Set<String> allSymbols() {
        return new HashSet<>(Arrays.asList("{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return getValue().equals(symbol.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "value='" + value + '\'' +
                '}';
    }
}
