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

    private boolean isIdentifier(String token){
        return Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$").matcher(token).matches();
    }

    private void skipComment(StringTokenizer tokenizer){
        while(tokenizer.hasMoreTokens())
            //stop when the current token is equal to the comment char
            if(tokenizer.nextToken().equals(COMMENT_CHARACTER))
                return;
    }

    public void scan(){
        String delimiters = separators.stream().reduce("", (a,b) -> a+b);

        int index = 0;
        for(var programLine : programLines){
            StringTokenizer tokenizer = new StringTokenizer(programLine, delimiters, true);
            while(tokenizer.hasMoreTokens()){
                String currentToken = tokenizer.nextToken();
                if(currentToken.equals(" "))
                    continue;
                if(currentToken.equals(COMMENT_CHARACTER))
                    skipComment(tokenizer);
                if(reservedWord.contains(currentToken) || operators.contains(currentToken) || separators.contains(currentToken))
                    pif.addOperatorSeparatorReservedWord(currentToken);
                else if(isIdentifier(currentToken))
                    pif.addIdentifierOrConstant(currentToken, symbolTable.add(currentToken));
            }
        }
        System.out.println(pif);
    }
}