package cwru.rac158.hw04;

/**
 * Created by student on 9/24/14.
 */
public class Token{
    private String nonTerminal;
    private String value;
    private int startLoc;
    private int endLoc;

    public String getNonTerminal() {
        return nonTerminal;
    }

    public void setNonTerminal(String nonTerminal) {
        this.nonTerminal = nonTerminal;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(int startLoc) {
        this.startLoc = startLoc;
    }

    public int getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(int endLoc) {
        this.endLoc = endLoc;
    }
}
