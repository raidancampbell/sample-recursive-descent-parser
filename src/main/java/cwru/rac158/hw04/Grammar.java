package cwru.rac158.hw04;

/**
 * Created by student on 9/24/14.
 * System.exit(1) indicates running off of input while parsing
 */

/* Here's the newly left-factored grammar I'm using
    Stmts : Stmt Stmts'
    Stmts': Stmts
      | e
    Stmt : PRINT Expr
      | VAR NAME EQUAL Expr
      | NAME EQUAL Expr
      | Expr
    Expr : Atom Expr'
    Expr': PLUS Expr
      | DASH Expr
      | e
    Params : Expr Params*
      | e
    Params* : COMMA Expr Params*
      | e
    Atom : NUMBER
      | NAME Atom'
    Atom': LPAREN Params RPAREN
      | e
*/

public class Grammar {
    static Token[] tokens;
    static String input;
    static int currentToken;

    public static void prepGrammar(Token[] givenTokens, String givenInput){
        tokens = givenTokens;
        input = givenInput;
        currentToken = 0;
    }

    public static boolean evalGrammar(){
        return stmts();
    }

    private static boolean stmts(){
        //Stmts : Stmt Stmts'
        if(stmt() && stmts_()) return true;
        return false;
    }

    private static boolean stmts_(){
        //Stmts': Stmts
        //      | e
        if(stmts()) return true;
        //empty
        return false;
    }

    private static boolean stmt(){
        //Stmt : PRINT Expr
        //       | VAR NAME EQUAL Expr
        //       | NAME EQUAL Expr
        //       | Expr
        if(consume(Token.NonTerminal.PRINT)){
            return true;
        } else if(consume(Token.NonTerminal.VAR)){
            return true;
        } else if(consume(Token.NonTerminal.NAME)){
            return true;
        }else return expr();

    }

    private static boolean expr(){
        //Expr : Atom Expr'
        return atom() && expr_();

    }

    private static boolean expr_(){
        //Expr': PLUS Expr
        //      | DASH Expr
        //      | e
        if(consume(Token.NonTerminal.PLUS)){
            return true;
        } else if(consume(Token.NonTerminal.DASH)){
            return true;
        }
        //empty set
        return true;
    }

    private static boolean params(){
        //Params : Expr Params*
        //       | e
        if(expr() && params_()) return true;
        //empty
        return true;
    }

    private static boolean params_(){
        //Params* : COMMA Expr Params*
        //        | e
        if(consume(Token.NonTerminal.COMMA)&& expr() && params_()) return true;
        //empty
        return true;
    }

    private static boolean atom(){
        //Atom : NUMBER
        //     | NAME Atom'
        if(consume(Token.NonTerminal.NUMBER)) return true;
        else return consume(Token.NonTerminal.NAME) && atom_();
    }

    private static boolean atom_(){
        //Atom': LPAREN Params RPAREN
        //      | e
        if(consume(Token.NonTerminal.LPAREN) && params() && consume(Token.NonTerminal.RPAREN)) return true;
        //empty
        return true;
    }

    private static boolean consume(Token.NonTerminal nt){
        if(currentToken >= tokens.length){
            System.err.println("Expected: " +nt+", but ran off input");
            System.exit(1);
        }
        if( nt == tokens[currentToken].getEnumNonTerminal()){
            currentToken++;
            return true;
        }
        return false;
    }

}
