package cwru.rac158.hw04;

/**
 * Created by R. Aidan Campbell on 9/24/14.
 *
 * Notes:
 * A symbol should only be consumed once a keyword (represented in caps) is hit.
 *
 * Parentheses currently not working.
 *
 * If the consume failed, everything from the current definition must be regurgitated
 *
 * after a vomit you must return false if there's an epsilon transition
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
            if(expr()) return true;
            vomit();//vomit PLUS back up
            return false;
        } else if(consume(Token.NonTerminal.DASH)){
            if(Main.debug)System.out.println("expr': dash expr");
            if(expr())return true;
            vomit();//vomit DASH back up
            return false;
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
        if (consume(Token.NonTerminal.COMMA)) {
            if (expr() && params_()) return true;
            vomit();//vomit COMMA back up
            return false;
        }
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
        if(consume(Token.NonTerminal.NAME)){
            if(atom_()) return true;
            vomit();
        }
        return false;
    }

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
        if(Main.debug)System.out.println("atom_: e");
        return true;
    }

    private static boolean consume(Token.NonTerminal nt){
        if(currentToken >= tokens.length){
            return false;
        } else if( nt == tokens[currentToken].getEnumNonTerminal()){
            currentToken++;
            return true;
        }
        return false;
    }

    private static void vomit(){
        currentToken--;
    }

}
