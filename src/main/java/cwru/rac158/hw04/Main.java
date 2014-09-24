package cwru.rac158.hw04;
//System.exit(2) indicates an error while invoking the lexer
//System.exit(3) indicates a bad string given (should probs throw, not exit)
//System.exit(4) indicates an error while reading the input


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.StringBuilder;

class Main {
static boolean debug = true;

    public static void main(String[] argv) {
        String inputString = null;
        try {
            inputString = readInput();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(4);
        }
        if(debug) System.out.println("input string: "+inputString);
        String[] stringTokens = invokeLexer(inputString);
        Token[] tokens = tokenize(stringTokens);
        if(debug) for(Token t: tokens) System.out.println("TOKEN: "+t.toString());
        Grammar.prepGrammar(tokens, inputString);
        if(debug) System.out.println("Grammar prepped");

        if(Grammar.evalGrammar()){
            System.out.println("match");
        } else {
            System.out.println("no match");
        }
    }

    static String readInput() throws IOException {
        InputStreamReader stdin = new InputStreamReader(System.in);
        StringBuilder sb = new StringBuilder();
        char[] chars = new char[4096];
        int read = readChars(stdin, sb, chars);
        while (read > -1) {
            read = readChars(stdin, sb, chars);
        }
        return sb.toString();
    }

    static int readChars(
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
            value[1] = value[1].replaceAll("\\)","");//easier parsing
            String[] intermediateStep = value[1].split("-");
            String[] startLoc = intermediateStep[0].split(",");
            String[] endLoc = intermediateStep[1].split(",");
            try {
                System.out.println(endLoc[1]);
                token.setStartLoc(Integer.parseInt(startLoc[1]) - 1);//-1 because lexer is 1-based, and we're 0-based
                token.setEndLoc(Integer.parseInt(endLoc[1]) - 1);//-1 because lexer is 1-based, and we're 0-based
            } catch(NumberFormatException e){
                System.err.println("Malformed number from lexer.");
            }
            returnVar.add(token);
        }
        return returnVar.toArray(new Token[returnVar.size()]);
    }

    private static String[] invokeLexer(String inputString) {
        Runtime rt = Runtime.getRuntime();
        ArrayList<String> returnVar = new ArrayList<String>();
        try {
            Process pr = rt.exec(new String[]{"./hw04-lex"});
            BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
            stdin.write(inputString);
            stdin.close();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            if(debug) System.out.println("Invocation string: ./hw04-lex");
            String line;//little helper variable
            while ((line = stdout.readLine()) != null) {
                returnVar.add(line);//build the array ]of the output
                if(debug) System.out.println("OUTPUT LINE: "+line);
            }
            if(debug)System.out.println("Finished reading output.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        return returnVar.toArray(new String[returnVar.size()]);
    }

    /**
     * give me a string array
     * I give you a string delimited by the given char
     * @param toCondense string array to condense
     * @param appendation the char to delimit a new element of toCondense
     * @return single string object
     */
    private static String flattenStringArray(String[] toCondense, char appendation){
        StringBuilder returnVar = new StringBuilder();
        for(int i = 0; i<toCondense.length; i++){
            returnVar.append(toCondense[i]);
            if(i + 1 < toCondense.length) returnVar.append(appendation);
        }
        return returnVar.toString();
    }
}

