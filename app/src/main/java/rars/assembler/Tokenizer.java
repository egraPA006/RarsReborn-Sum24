
package rars.assembler;

import rars.Globals;
import rars.simulator.RiscvProgram;
import rars.simulator.SourceLine;

import java.util.ArrayList;
public class Tokenizer {
    private RiscvProgram program;
    //private HashMap<String, String> equivalents; // DPS 11-July-2012
    //private ErrorList errors;
    // The 8 escaped characters are: single quote, double quote, backslash, newline (linefeed),
    // tab, backspace, return, form feed.  The characters and their corresponding decimal codes:
    // TODO: potentially make this automatic
    private static final String escapedCharacters = "'\"\\ntbrf0";
    private static final String[] escapedCharactersValues = {"39", "34", "92", "10", "9", "8", "13", "12", "0"};

    public Tokenizer(RiscvProgram p) {
        this.program = p;
    }

    public void tokenize() {
        ArrayList<ArrayList<Token>> ans = new ArrayList<>();
        for (int i = 0 ; i < program.preprocessedSourceCode.size(); i ++) {
            SourceLine toProcess = program.preprocessedSourceCode.get(i);
            ans.add(tokenizeLine(toProcess));

        }
        program.preprocessedTokenisedCode = ans;
    }
    private ArrayList<Token> tokenizeLine(SourceLine line) {
        ArrayList<Token> ans = new ArrayList<>();
        ArrayList<String> preAns = new ArrayList<>();
        boolean hasComment = false;
        String value = line.value;
        String buildWord = ""; //
        for (int i = 0 ; i < value.length(); i ++) {
            char c = value.charAt(i);
            if (c == '#') {
                if (i == 0) {  // the whole line is comment
                    return null;
                }
                else {  // all what we have before should mean something
                    if (!buildWord.isEmpty()) {
                        preAns.add(buildWord);
                        buildWord = "";
                        break;  // meet comment - stop parse line
                    }
                    break;  // if string before was empty - just stop
                }
            }
            //TODO: check ",," case error
            //TODO: optimization
            if (c == '(' &&  !buildWord.isEmpty()) {
                preAns.add(buildWord);
                buildWord = "(";
                continue;
            }
            if (c == ' ' || c ==',' || c == '\t') {
                if (!(buildWord.equals(" ") ||buildWord.equals("\t")) && (buildWord.length()>0)){
                    preAns.add(buildWord);
                }
                buildWord = "";
                continue;

            }
            buildWord = buildWord + c;

        }
       if (!(buildWord.isEmpty())) preAns.add(buildWord);

        //Now we determine TokenType
            establishToken(preAns,ans);



        return ans;
    }
    private void establishToken(ArrayList<String> preAns, ArrayList<Token>ans) {
        boolean haveLabel = false;
        for (int i = 0 ; i < preAns.size(); i ++) {
            String word = (preAns.get(i));
            if (word.endsWith(":") & (i == 0)) {
                ans.add(new Token(TokenType.LABEL_INIT, word));
                continue;
            }
            if (word.charAt(0) == '.' & (i == 0)) {
                ans.add(new Token(TokenType.DIRECTIVE, word));
                continue;
            }
            if (Globals.registerTable.containsKey(word)) {
                ans.add(new Token(TokenType.REGISTER, word));
                continue;
            }
            if (Globals.instructionMap.containsKey(word)) {
                ans.add(new Token(TokenType.INSTRUCTION, word));
                continue;
            }
            if (isNumeric(word)) {
                ans.add(new Token(TokenType.CONSTANT, word));
                continue;
            }
            if (word.charAt(0) == '(' && word.endsWith(")")) {
                ans.add(new Token(TokenType.OPEN_BRACKET, "(" ));
                String t = "";
                for (int j = 1 ; j < word.length()-1; j ++) {
                    char letter = word.charAt(j);
                    t +=  letter;
                }
                // determining what is in the brackets
                if (Globals.registerTable.containsKey(t)) {
                    ans.add(new Token(TokenType.REGISTER, t));
                    ans.add(new Token(TokenType.CLOSE_BRACKET, ")" ));
                }
                else  if (isNumeric(t)) {
                    ans.add(new Token(TokenType.CONSTANT, t));  //TODO: Tell Egor that we cant have labels, consists of numbers
                    ans.add(new Token(TokenType.CLOSE_BRACKET, ")" ));
                }
                else {
                    ans.add(new Token(TokenType.LABEL, t));
                    ans.add(new Token(TokenType.CLOSE_BRACKET, ")"));
                }
            }
            else {
                ans.add(new Token(TokenType.LABEL, word));
            }



        } //. : intr
    }
    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    private  boolean establishLabel(String word, ArrayList<String> preAns, ArrayList<Token>ans) {
//            if (word.endsWith(":")) {
//                ans.add(new Token(TokenType.LABEL_INIT, word));  //TODO: add Error Handling
//               return true;
//        }
//            return false;
//    }
//    private  boolean establishOffSet(String word, ArrayList<String> preAns, ArrayList<Token>ans) {
//        String off = "";
//        String val = "";
//        int i = 0;
//        while (i < word.length()) {
//
//            if (word.charAt(i) == '(') {
//                while (word.charAt(i) != ')' && i < word.length() - 1) {  // -1 to not cause
//                                                                        // exception
//                    val += word.charAt(i);
//                    i ++;
//                }
//                val += word.charAt(i);  // to add last symbol )
//                break;
//            }
//
//            off += word.charAt(i);
//            i++;
//        }
//        if (!val.endsWith(")")) return false;  // exception: wrong Inputs!!
//        ans.add(new Token(TokenType.OFFSET, off));
//        ans.add(new Token(TokenType.OFFSET_ADRESS, val));
//        return true;
//    }
//    private  boolean establishInstruction(String word, ArrayList<String> preAns, ArrayList<Token>ans) {
////        if (Globals.allInstructions.containsKey(word)) {
//            ans.add(new Token(TokenType.INSTRUCTION, word)); //TODO: add Handling : No such instruction
//            return true;
////        }
//    }
//    private  boolean establishRegister(String word, ArrayList<String> preAns, ArrayList<Token>ans) {
//        if (Globals.registerTable.containsKey(word)) {
//            ans.add(new Token(TokenType.REGISTER, word)); //TODO: add Handling : No such register
//        } else {
//            ans.add(new Token(TokenType.LABEL, word));
//        }
//        return true;
////        }
////        return false;
//
//    }
//    private  boolean establishConstant(String word, ArrayList<String> preAns, ArrayList<Token>ans) {
//        if (isNumeric(word)) {
//            ans.add(new Token(TokenType.CONSTANT, word));
//            return true;
//        }
//
//        return false;
//    }


}

