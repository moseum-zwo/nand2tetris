package token;

import java.util.*;

public class Keyword implements Token {

    private String value;

    public Keyword(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return "keyword";
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static Set<String> allKeywords() {
        return new HashSet<>(Arrays.asList("class", "constructor", "function", "method", "field", "static", "var",
                "int", "char", "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while",
                "return"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keyword keyword = (Keyword) o;
        return getValue().equals(keyword.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "value='" + value + '\'' +
                '}';
    }
}
