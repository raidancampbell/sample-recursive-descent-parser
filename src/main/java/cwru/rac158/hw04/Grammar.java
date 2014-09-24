package cwru.rac158.hw04;

/**
 * Created by student on 9/24/14.
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
      | NAME NAME'
    Name': LPAREN Params RPAREN
      | e
*/

public class Grammar {
    Token[] tokens;
    String input;

    public void prepGrammar(Token[] givenTokens, String givenInput){
        this.tokens = givenTokens;
        this.input = givenInput;
    }

}
