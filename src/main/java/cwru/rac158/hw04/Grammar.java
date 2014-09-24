package cwru.rac158.hw04;

/**
 * Created by R. Aidan Campbell on 9/24/14.
 *
 * Here's where the language is defined, and recursively evaluated
 * each method has its production commented with it, for easy(ier) reading
 *
 * Subtle issues I encountered:
 * If the consume failed, everything consumed from the current definition must be vomited
 * after a vomit you must return false if there's an epsilon transition in the definition
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

    //Complexity: 1
    public static boolean evalGrammar(){
        if(stmts())
        {//if there was a successful return,
            //make sure everything was consumed
            //so that it didn't just epsilon out
            return currentToken == tokens.length;
        }
        return false;
    }

    //Complexity: 1
    private static boolean stmts(){
        //Stmts : Stmt Stmts'
        if(stmt() && stmts_()) {
            return true;
        }
        return false;
    }

    //Complexity: 1
    private static boolean stmts_(){
        //Stmts': Stmts
        //      | e
        if(stmts()){
            return true;
        }
        //empty
        return true;
    }

    //Complexity: 9
    //but I hope the comments make it
    //easier to swallow
    private static boolean stmt(){
        //Stmt : PRINT Expr
        //       | VAR NAME EQUAL Expr
        //       | NAME EQUAL Expr
        //       | Expr
        if(consume(Token.NonTerminal.PRINT)){
            if(expr()) return true;
            vomit();//vomit PRINT back up
        }
        if(consume(Token.NonTerminal.VAR)){
            if(consume(Token.NonTerminal.NAME)){
                if(consume(Token.NonTerminal.EQUAL)){
                    if(expr()) return true;
                    vomit();//vomit EQUAL back up
                }
                vomit();//vomit NAME back up
            }
            vomit();//vomit VAR back up
        }
        if(consume(Token.NonTerminal.NAME)){
            if(consume(Token.NonTerminal.EQUAL)){
                if(expr()) return true;
                vomit();//vomit EQUAL back up
            }
            vomit();//vomit NAME back up
        }
        return expr();
    }

    //Complexity: 1
    private static boolean expr(){
        //Expr : Atom Expr'
        return atom() && expr_();

    }

    //complexity: 4
    private static boolean expr_(){
        //Expr': PLUS Expr
        //      | DASH Expr
        //      | e
        if(consume(Token.NonTerminal.PLUS)){
            if(expr()) return true;
            vomit();//vomit PLUS back up
            return false;
        } else if(consume(Token.NonTerminal.DASH)){
            if(expr())return true;
            vomit();//vomit DASH back up
            return false;
        }
        //empty set
        return true;
    }

    //Complexity: 2
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
        if (consume(Token.NonTerminal.COMMA)) {
            if (expr() && params_()) return true;
            vomit();//vomit COMMA back up
            return false;
        }
        //empty
        return true;
    }

    //Complexity: 3
    private static boolean atom(){
        //Atom : NUMBER
        //     | NAME Atom'
        if(consume(Token.NonTerminal.NUMBER)){
            return true;
        }
        if(consume(Token.NonTerminal.NAME)){
            if(atom_()) return true;
            vomit();
        }
        return false;
    }

    //Complexity: 3
    private static boolean atom_(){
        //Atom': LPAREN Params RPAREN
        //      | e
        if(consume(Token.NonTerminal.LPAREN)){
            if(params()){
                if(consume(Token.NonTerminal.RPAREN)) return true;
            }
            vomit();
            return false;
        }
        //empty
        return true;
    }

    /**
     * moves the token we're focusing on forward
     * @param nt expected token for successful consumption
     * @return whether consumption was successful
     *
     * complexity: 2
     */
    private static boolean consume(Token.NonTerminal nt){
        if(currentToken >= tokens.length){
            return false;
        } else if( nt == tokens[currentToken].getEnumNonTerminal()){
            currentToken++;
            return true;
        }
        return false;
    }

    /**
     * we consumed something we shouldn't have
     * so we unconsume it.
     */
    private static void vomit(){
        currentToken--;
    }
}//end of class