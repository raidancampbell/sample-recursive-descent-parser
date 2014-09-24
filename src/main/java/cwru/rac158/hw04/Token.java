package cwru.rac158.hw04;

/**
 * Created by student on 9/24/14.
 */
public class Token {
    private String nonTerminal;
    private String value;
    private int startLoc;
    private int endLoc;
    private NonTerminal enumNonTerminal;

    //maybe implement this?
    enum NonTerminal {
        STMT, VAR, PRINT, NAME, EXPR, EQUAL,
        ATOM, PLUS, DASH, COMMA, LPAREN, RPAREN, NUMBER
    }

    public NonTerminal getEnumNonTerminal(){
        return enumNonTerminal;
    }

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
        calculateEnumeratedValue();
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

    private void calculateEnumeratedValue() {
        //huh. this must be compiled with Java6, because
        //Java7 allows strings in switch statements.  I tried, but
        //the IDE wasn't happy.
        if(value.equals("PRINT")){
            enumNonTerminal = NonTerminal.PRINT;
        } else if(value.equals("NAME")){
            enumNonTerminal = NonTerminal.NAME;
        } else if(value.equals("VAR")){
            enumNonTerminal = NonTerminal.VAR;
        } else if(value.equals("EQUAL")){
            enumNonTerminal = NonTerminal.EQUAL;
        } else if(value.equals("NUMBER")){
            enumNonTerminal = NonTerminal.NUMBER;
        } else if(value.equals("PLUS")){
            enumNonTerminal = NonTerminal.PLUS;
        } else if(value.equals("DASH")){
            enumNonTerminal = NonTerminal.DASH;
        } else if(value.equals("COMMA")){
            enumNonTerminal = NonTerminal.COMMA;
        } else if(value.equals("LPAREN")){
            enumNonTerminal = NonTerminal.LPAREN;
        } else if(value.equals("RPAREN")){
            enumNonTerminal = NonTerminal.RPAREN;
        } else {
            System.err.println("ERR: expected valid nonterminal, received: "+value);
        }
    }//end of calculateEnumeratedValue

    @Override
    public String toString(){
        return(enumNonTerminal+" ("+nonTerminal+") :" + value + " : "+startLoc+"-"+endLoc);
    }
}
