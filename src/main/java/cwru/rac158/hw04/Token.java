package cwru.rac158.hw04;

/**
 * Created by R. Aidan Campbell on 9/24/14.
 *
 * Locations are entirely unused.  Since they were given by the
 * lexer, they may be of use later.
 *
 * Forward progress is made by consuming the entire token, without
 * regard to the string or its position. Assuming everything is generated
 * correctly, the string/token lineup is perfect.
 */
public class Token {
    private String nonTerminal;
    private String value;
    private int startLoc;
    private int endLoc;
    private NonTerminal enumNonTerminal; //only actually useful variable

    enum NonTerminal {
        VAR, PRINT, NAME, EQUAL, PLUS, DASH,
        COMMA, LPAREN, RPAREN, NUMBER}

    /**
     *
     * @return the enumerated version of the nonTerminal
     */
    public NonTerminal getEnumNonTerminal(){
        return enumNonTerminal;
    }

    /**
     * sets the token's nonterminal
     * @param givennonTerminal
     */
    public void setNonTerminal(String givennonTerminal) {
        givennonTerminal = givennonTerminal.trim();
        givennonTerminal = givennonTerminal.replaceAll("\\)","");
        givennonTerminal = givennonTerminal.replaceAll("\\(","");
        this.nonTerminal = givennonTerminal;
    }

    /**
     * set the token's value.
     * the enum is calculated thereafter
     * @param value value as a string
     */
    public void setValue(String value) {
        this.value = value;
        calculateEnumeratedValue();
    }

    /**
     *
     * @return location where token begins
     */
    public int getStartLoc() {
        return startLoc;
    }

    /**
     * location where token begins
     * @param startLoc location in string
     */
    public void setStartLoc(int startLoc) {
        this.startLoc = startLoc;
    }

    /**
     *
     * @return location where token ends
     */
    public int getEndLoc() {
        return endLoc;
    }

    /**
     * location where the token ends
     * @param endLoc location in string
     */
    public void setEndLoc(int endLoc) {
        this.endLoc = endLoc;
    }

    /**
     * enums, because we have a definitive
     * number of options to choose from
     *
     * Compexity: 10. artificially high.
     */
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
