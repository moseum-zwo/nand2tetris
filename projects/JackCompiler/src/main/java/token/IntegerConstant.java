package token;

import java.util.Objects;

public class IntegerConstant implements Token {

    private String value;

    public IntegerConstant(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "integerConstant";
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerConstant that = (IntegerConstant) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "IntegerConstant{" +
                "value='" + value + '\'' +
                '}';
    }
}
