
package rars.assembler;
public class Token {
    private TokenType type;
    private String value;
    public Token (TokenType t, String value) {
        this.type = t;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public TokenType getType() {
        return type;
    }
}
