package cwru.rac158.hw04;
//System.exit(2) indicates an error while invoking the lexer
//System.exit(3) indicates a bad string given (should probs throw, not exit)

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
static boolean debug = true;
    public static void main(String[] argv) {
        String[] args = cannedMain(argv);
        String inputString = flattenStringArray(args);
        if(debug) inputString = "var x = 0";
        if(debug) System.out.println("input string: "+inputString);
        String[] stringTokens = invokeLexer(args);
        Token[] tokens = tokenize(stringTokens);
        if(Grammar.evalGrammar()){
            System.out.println("match");
        } else{
            System.out.println("no match");
        }
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
        //each of the ',' splits are split at '-' after is the startloc/endloc
        //w1[0] = (!
        //w1[1] = STARTLOC)
        //w2[0] = (!
        //w2[1] = ENDLOC)
        for(String s:stringTokens){
            Token token = new Token();
            if(s.indexOf(":") == -1){
                System.err.println("Bad input text given. No match.");
                System.exit(3);
            }
            String[] nonterminal = s.split(":");
            token.setNonTerminal(nonterminal[0]);
            String[] value = nonterminal[1].split(" ");//TODO: check if this should be "\ "
            token.setValue(value[0]);
            String[] intermediateStep = value[1].split(",");
            intermediateStep[0] = intermediateStep[0].replaceAll("\\)","");//easier int parsing
            String[] startLoc = intermediateStep[0].split("-");
            try {
                token.setStartLoc(Integer.parseInt(startLoc[1]) - 1);//-1 because lexer is 1-based, and we're 0-based
                String[] endLoc = intermediateStep[1].split("-");
                token.setEndLoc(Integer.parseInt(endLoc[1]) - 1);//-1 because lexer is 1-based, and we're 0-based
            } catch(NumberFormatException e){
                System.err.println("Malformed number from lexer.");
            }
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

