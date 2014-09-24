package cwru.rac158.hw04;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Main {

    /* Here's the newly left-factored grammar I'm using
    Stmts : Stmt Stmts'
    Stmts': Stmts
      | e
      ;
    Stmt : PRINT Expr
      | VAR NAME EQUAL Expr
      | NAME EQUAL Expr
      | Expr
      ;
    Expr : Atom Expr'
    Expr': PLUS Expr
      | DASH Expr
      | e
      ;
    Params : Expr Params*
      | e
      ;
    Params* : COMMA Expr Params*
      | e
      ;
    Atom : NUMBER
      | NAME NAME'
      ;
    Name': LPAREN Params RPAREN
      | e
      ;
     */

    public static void main(String[] argv) {
        String[] args = cannedMain(argv);
        String[] stringTokens = invokeLexer(args);
        Token[] tokens = tokenize(stringTokens);
    }

    /**
     * you give me the tokens in string form
     * I parse them into a meaningful object, and return them
     * @param stringTokens input token as a string
     * @return output tokens in as meaningful data
     */
    private static Token[] tokenize(String[] stringTokens){
        ArrayList<Token> returnVar = new ArrayList<Token>();
        //t = NONTERMINAL:VALUE (!-STARTLOC),(!-ENDLOC)
        //! == do not care(?)
        //split first at ':' before is the nonterminal
        //t[0] = NONTERMINAL
        //t[1] = VALUE (!-STARTLOC),(!-ENDLOC)
        //then at ' ' before is the value
        //u[0] = VALUE
        //u[1] = (!-STARTLOC),(!-ENDLOC)
        //then at '-' before is part of startloc, after is part of endloc
        //v[0] = (!-STARTLOC)
        //v[1] = (!-ENDLOC)
        //each of the '-' splits are split at ',' after is the startloc/endloc
        //w1[0] = (!
        //w1[1] = STARTLOC)
        //w2[0] = (!
        //w2[1] = ENDLOC)

        for(String s:stringTokens){
            Token token = new Token();
            String[] nonterminal = s.split(":");
            token.setNonTerminal(nonterminal[0]);
            String[] value = nonterminal[1].split(" ");//TODO: check if this should be "\ "
            token.setValue(value[0]);

        }
        return returnVar.toArray(new Token[returnVar.size()]);
    }

    private static String[] invokeLexer(String[] inputString) {
        Runtime rt = Runtime.getRuntime();
        ArrayList<String> returnVar = new ArrayList<String>();
        String toLex = flattenStringArray(inputString);

        try {
            Process pr = rt.exec(new String[]{"./hw04-lex",toLex});//run the command
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line;//little helper variable
            while ((line = input.readLine()) != null) {
                returnVar.add(line);//build the array of the output
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        return returnVar.toArray(new String[returnVar.size()]);
    }

    /**
     * give me a string array
     * I give you a '\n' delimited string
     * @param toCondense string array to condense
     * @return single string object
     */
    private static String flattenStringArray(String[] toCondense){
        StringBuilder returnVar = new StringBuilder();
        for(int i = 0; i<toCondense.length; i++){
            returnVar.append(toCondense[i]);
            if(i + 1 < toCondense.length) returnVar.append('\n');
        }
        return returnVar.toString();
    }


    private static String[] cannedMain(String[] argv) {
        final Option helpOpt = new Option("h", "help", false, "Print this message");
        final Option exampleArgOpt = new Option("a", "arg-example", true, "Example option which accepts and argument");
        final Options options = new Options();
        options.addOption(helpOpt);
        options.addOption(exampleArgOpt);

        String[] args = null;

        try {
            GnuParser parser = new GnuParser();
            CommandLine line = parser.parse(options, argv);
            args = line.getArgs();

        } catch (final MissingOptionException e) {
            System.err.println(e.getMessage());
            Usage(options);
        } catch (final UnrecognizedOptionException e) {
            System.err.println(e.getMessage());
            Usage(options);
        } catch (final ParseException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return args;

    }

    public static void Usage(Options options) {
        new HelpFormatter().printHelp("hw04", options, true);
        System.exit(1);
    }
}

