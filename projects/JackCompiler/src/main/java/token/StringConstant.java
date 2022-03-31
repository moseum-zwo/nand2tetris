package token;

import java.util.Objects;

public class StringConstant implements Token {

    private String value;

    public StringConstant(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "stringConstant";
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringConstant that = (StringConstant) o;
        return getValue().equals("\"" + that.getValue() + "\"");
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "StringConstant{" +
                "value='" + value + '\'' +
                '}';
    }
}
