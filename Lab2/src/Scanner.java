import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scanner {
    String COMMENT_CHARACTER = "#";
    String STRING_DEIMITER = "\"";
    String CHARACTER_DELIMITER = "'";
    List<String> separators = Arrays.asList(
            "(", ")",
            "[", "]",
            "{", "}",
            ";", " ", ",", "\n", "\'", "\"", "#"
    );
    List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "=", "<", "<=", "==>", ">", "===", "+=", "-=");
    List<String> reservedWord = Arrays.asList(
            "int", "alpha", "arrr", "fibre", "make", "if", "fi", "of", "prgr",
            "read", "show", "define", "now", "persistent", "for", "while", "and",
            "or", "not", "starts", "from", "transforms", "stops", "at", "stdin", "stdout");

    String delimiters = separators.stream().reduce("", (a, b) -> a + b) +
            operators.stream().reduce("", (a, b) -> a + b);

    private SymbolTable<String> symbolTable;
    private PIF pif;
    private String programPath;
    private List<String> programLines;

    public Scanner(String programPath) throws FileNotFoundException {
        symbolTable = new SymbolTable<>();
        pif = new PIF();

        this.programPath = programPath;
        var bufferedReader = new BufferedReader(new FileReader(programPath));
        programLines = bufferedReader.lines().toList();
    }

    private boolean isIdentifier(String token) {
        return Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$").matcher(token).matches();
    }

    private void skipComment(StringTokenizer tokenizer) {
        while (tokenizer.hasMoreTokens())
            //stop when the current token is equal to the comment char
            if (tokenizer.nextToken().equals(COMMENT_CHARACTER))
                return;
    }

    public String getStringConstant(StringTokenizer tokenizer) {
        var stringBuilder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String currentToken = tokenizer.nextToken();
            if (currentToken.equals(" "))
                continue;
            if (currentToken.equals(STRING_DEIMITER))
                return stringBuilder.toString();
            stringBuilder.append(currentToken);
        }
        return "\"%s\"".formatted(stringBuilder.toString());
    }

    private boolean isIntegerConstant(String token) {
        return Pattern.compile("^0|([+-]?[1-9][0-9]*)$").matcher(token).matches();
    }

    public void scan() {
        var lexicalErrorsStringBuilder = new StringBuilder();
        for (int lineIndex = 0; lineIndex < programLines.size(); lineIndex++) {
            String programLine = programLines.get(lineIndex);
            StringTokenizer tokenizer = new StringTokenizer(programLine, delimiters, true);
            while (tokenizer.hasMoreTokens()) {
                String currentToken = tokenizer.nextToken();
                if (currentToken.equals(" "))
                    continue;
                if (currentToken.equals(COMMENT_CHARACTER))
                    skipComment(tokenizer);
                else if (reservedWord.contains(currentToken) || operators.contains(currentToken) || separators.contains(currentToken))
                    pif.addOperatorSeparatorReservedWord(currentToken);
                else if (isIdentifier(currentToken))
                    pif.addIdentifierOrConstant(currentToken, symbolTable.add(currentToken));
                else if (currentToken.equals(STRING_DEIMITER)) {
                    String stringConstant = getStringConstant(tokenizer);
                    pif.addIdentifierOrConstant(stringConstant, symbolTable.add(stringConstant));
                } else if (currentToken.equals(CHARACTER_DELIMITER)) {
                    String charConstant = "'%s'".formatted(tokenizer.nextToken());
                    pif.addIdentifierOrConstant(charConstant, symbolTable.add(charConstant));
                } else if (isIntegerConstant(currentToken))
                    pif.addIdentifierOrConstant(currentToken, symbolTable.add(currentToken));
                else
                    lexicalErrorsStringBuilder.append(("Lexical error on line %s, token %s is not " +
                            "an reserved word, operator, separator, identifier or constant\n").formatted(lineIndex, currentToken));
            }
        }
        if (lexicalErrorsStringBuilder.isEmpty())
            System.out.println("Lexical correct");
        else
            System.out.println(lexicalErrorsStringBuilder);
        System.out.println(pif);
    }
}