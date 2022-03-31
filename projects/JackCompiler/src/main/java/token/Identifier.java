package token;

import java.util.Objects;

public class Identifier implements Token {

    private String value;

    public Identifier(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "identifier";
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "value='" + value + '\'' +
                '}';
    }
}
