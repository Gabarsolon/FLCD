import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Scanner {
    List<String> separators = Arrays.asList(
            "(", ")",
            "[", "]",
            "{", "}",
            ";", " ", ",", "\n", "\'", "\"", "#"
            );
    List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "=", "<", "<=", "==>", ">", "===", "+=", "-=");
    List<String> keyWords = Arrays.asList(
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

    private void readFile() throws FileNotFoundException {

    }
}