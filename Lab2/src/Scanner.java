import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    List<String> tokens;
    public Scanner() throws IOException {
        tokens = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("/rules/"));
        while(br.readLine()!=null){
//            tokens.add();
        }
    }
}
