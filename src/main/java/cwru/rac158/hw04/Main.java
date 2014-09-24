package cwru.rac158.hw04;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

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

