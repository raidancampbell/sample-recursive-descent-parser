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

    public void setNonTerminal(String givennonTerminal) {
        givennonTerminal = givennonTerminal.trim();
        givennonTerminal = givennonTerminal.replaceAll("\\)","");
        givennonTerminal = givennonTerminal.replaceAll("\\(","");
        this.nonTerminal = givennonTerminal;
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
        if(nonTerminal.equals("PRINT")){
            enumNonTerminal = NonTerminal.PRINT;
        } else if(nonTerminal.equals("NAME")){
            enumNonTerminal = NonTerminal.NAME;
        } else if(nonTerminal.equals("VAR")){
            enumNonTerminal = NonTerminal.VAR;
        } else if(nonTerminal.equals("EQUAL")){
            enumNonTerminal = NonTerminal.EQUAL;
        } else if(nonTerminal.equals("NUMBER")){
            enumNonTerminal = NonTerminal.NUMBER;
        } else if(nonTerminal.equals("PLUS")){
            enumNonTerminal = NonTerminal.PLUS;
        } else if(nonTerminal.equals("DASH")){
            enumNonTerminal = NonTerminal.DASH;
        } else if(nonTerminal.equals("COMMA")){
            enumNonTerminal = NonTerminal.COMMA;
        } else if(nonTerminal.equals("LPAREN")){
            enumNonTerminal = NonTerminal.LPAREN;
        } else if(nonTerminal.equals("RPAREN")){
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
