package cwru.rac158.hw04;
/*
This is the main class from which the code is controlled.
The language evaluation and recursion is all done in Grammar
This is mostly just refining the input into a useful form

System.exit(2) indicates an error while invoking the lexer
System.exit(3) indicates a bad string given (should probs throw, not exit)
System.exit(4) indicates an error while reading the input

R. Aidan Campbell
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.StringBuilder;

class Main {

    /**
     * you give me a string on stdin
     * I tell you if it's in the language
     * @param argv *unused*
     */
    public static void main(String[] argv) {
        String inputString = null;
        try {
            inputString = readInput();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(4);
        }
        String[] stringTokens = invokeLexer(inputString);
        Token[] tokens = tokenize(stringTokens);
        Grammar.prepGrammar(tokens, inputString);

        if(Grammar.evalGrammar()){
            System.out.println("match");
        } else {
            System.out.println("no match");
        }

    }

    /**
     * makes testing code easy.
     * Copies main method, but receives input.
     *
     * @param inputString string to lex
     * @return whether inputString is in the language
     */
    public static boolean testHarness(String inputString){
        String[] stringTokens = invokeLexer(inputString);
        Token[] tokens = tokenize(stringTokens);
        Grammar g = new Grammar();
        g.prepGrammar(tokens, inputString);
        return g.evalGrammar();
    }

    /**
     * Tim-blessed code.
     * @return text from the standard in
     * @throws IOException
     */
     private static String readInput() throws IOException {
        InputStreamReader stdin = new InputStreamReader(System.in);
        StringBuilder sb = new StringBuilder();
        char[] chars = new char[4096];
        int read = readChars(stdin, sb, chars);
        while (read > -1) {
            read = readChars(stdin, sb, chars);
        }
        return sb.toString();
    }

    /**
     * Tim-blessed code.
     * @param stdin given ISR
     * @param sb given stringbuilder
     * @param chars given chararray
     * @return the char that was read
     * @throws IOException
     */
    private static int readChars(
            InputStreamReader stdin, StringBuilder sb, char[] chars)
            throws IOException {
        int read = stdin.read(chars);
        if (read > 0) {
            sb.append(chars, 0, read);
        }
        return read;
    }

    /**
     * you give me the tokens in string form
     * I parse them into a meaningful object, and return them
     * @param stringTokens input token as a string
     * @return output tokens in as meaningful data
     *
     * Complexity: 2
     */
    private static Token[] tokenize(String[] stringTokens){
        ArrayList<Token> returnVar = new ArrayList<Token>();
        //t = NONTERMINAL:VALUE (!-STARTLOC),(!-ENDLOC)
        //! == do not care(?)
        //split first at ':' before is the nonterminal
        //t[0] = NONTERMINAL
        //t[1] = VALUE (!,STARTLOC)-(!,ENDLOC)
        //then at ' ' before is the value
        //u[0] = VALUE
        //u[1] = (!,STARTLOC)-(!,ENDLOC)
        //u[1] is prepped to be (!,STARTLOC-(!,ENDLOC
        //then at '-' before is part of startloc, after is part of endloc
        //v[0] = (!,STARTLOC
        //v[1] = (!,ENDLOC)
        //each of the ',' splits are split at '-' after is the startloc/endloc
        //w1[0] = (!
        //w1[1] = STARTLOC
        //w2[0] = (!
        //w2[1] = ENDLOC
        for(String s:stringTokens){
            Token token = new Token();
            if(s.indexOf(":") == -1){
                System.err.println("Bad input text given. No match.");
                System.exit(3);
            }
            String[] nonterminal = s.split(":");
            token.setNonTerminal(nonterminal[0]);
            String[] value = nonterminal[1].split(" ");
            token.setValue(value[0]);
            value[1] = value[1].replaceAll("\\)","");//easier parsing
            String[] intermediateStep = value[1].split("-");
            String[] startLoc = intermediateStep[0].split(",");
            String[] endLoc = intermediateStep[1].split(",");
            try {
                //-1 because lexer is 1-based, and we're 0-based
                token.setStartLoc(Integer.parseInt(startLoc[1]) - 1);
                token.setEndLoc(Integer.parseInt(endLoc[1]) - 1);
            } catch(NumberFormatException e){
                System.err.println("Malformed number from lexer.");
            }
            returnVar.add(token);
        }
        return returnVar.toArray(new Token[returnVar.size()]);
    }

    /**
     * you give me an input string, I shove it through the lexer
     * and I give you the valid output of the lexer
     *
     * NOTE: does not return any unconsumed text.
     *
     * @param inputString string to lex
     * @return the lexer's output, with each line as an element
     *
     * Complexity: 1
     */
    private static String[] invokeLexer(String inputString) {
        //thanks, Tim
        Runtime rt = Runtime.getRuntime();
        ArrayList<String> returnVar = new ArrayList<String>();
        try {
            Process pr = rt.exec(new String[]{"./hw04-lex"});
            BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
            stdin.write(inputString);
            stdin.close();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;//little helper variable
            while ((line = stdout.readLine()) != null) {
                returnVar.add(line);//build the array of the output
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        return returnVar.toArray(new String[returnVar.size()]);
    }
}

