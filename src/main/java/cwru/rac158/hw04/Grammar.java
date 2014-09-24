package cwru.rac158.hw04;

/**
 * Created by R. Aidan Campbell on 9/24/14.
 *
 * Notes:
 * A symbol should only be consumed once a keyword (represented in caps) is hit.
 *
 * Parentheses currently not working.
 *
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
        if(stmt() && stmts_()) {
            if(Main.debug)System.out.println("Stmts: Stmt Stmts'");
            return true;
        }
        return false;
    }

    private static boolean stmts_(){
        //Stmts': Stmts
        //      | e
        if(stmts()){
            if(Main.debug)System.out.println("Stmts': Stmts'");
            return true;
        }
        //empty
        if(Main.debug)System.out.println("Stmts': e'");
        return true;
    }

    private static boolean stmt(){
        //Stmt : PRINT Expr
        //       | VAR NAME EQUAL Expr
        //       | NAME EQUAL Expr
        //       | Expr
        if(consume(Token.NonTerminal.PRINT) && expr()){
            if(Main.debug)System.out.println("stmt: print expr'");
            return true;
        } else if(consume(Token.NonTerminal.VAR) && consume(Token.NonTerminal.NAME)
                && consume(Token.NonTerminal.EQUAL) && expr()){
            if(Main.debug)System.out.println("Stmt: var name equal expr");
            return true;
        } else if(consume(Token.NonTerminal.NAME) && consume(Token.NonTerminal.EQUAL) && expr()){
            if(Main.debug)System.out.println("Stmt: name equal expr");
            return true;
        }else
        if(Main.debug)System.out.println("Stmt: expr");
        return expr();
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
            if(Main.debug)System.out.println("expr': plus expr");
            return expr();
        } else if(consume(Token.NonTerminal.DASH)){
            if(Main.debug)System.out.println("expr': dash expr");
            return expr();
        }
        if(Main.debug)System.out.println("expr: e");
        //empty set
        return true;
    }

    private static boolean params(){
        //Params : Expr Params*
        //       | e
        if(expr() && params_()) return true;
        //empty
        if(Main.debug)System.out.println("params: e");
        return true;
    }

    private static boolean params_(){
        //Params* : COMMA Expr Params*
        //        | e
        if(consume(Token.NonTerminal.COMMA)&& expr() && params_()) return true;
        //empty
        if(Main.debug)System.out.println("params': e");
        return true;
    }

    private static boolean atom(){
        //Atom : NUMBER
        //     | NAME Atom'
        if(consume(Token.NonTerminal.NUMBER)){
            if(Main.debug)System.out.println("atom: number");
            return true;
        }
        else return consume(Token.NonTerminal.NAME) && atom_();
    }

    private static boolean atom_(){
        //Atom': LPAREN Params RPAREN
        //      | e
        if(consume(Token.NonTerminal.LPAREN) && params() && consume(Token.NonTerminal.RPAREN)) return true;
        //empty
        if(Main.debug)System.out.println("atom_: e");
        return true;
    }

    private static boolean consume(Token.NonTerminal nt){
        if(currentToken >= tokens.length){
            if(Main.debug) System.err.println("currentToken: "+ currentToken+"\t\ttokens.length: "+tokens.length);
            System.err.println("Expected: " +nt+", but ran off input");
            //TODO: remove this section of the if, and replace with return false
        } else if( nt == tokens[currentToken].getEnumNonTerminal()){
            currentToken++;
            return true;
        }
        return false;
    }

}
