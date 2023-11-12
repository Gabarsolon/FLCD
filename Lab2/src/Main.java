import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        var scanner = new Scanner("src/programs/p2.txt");
//        scanner.scan();
        var fa = new FA("src/specifications/FA.in");
        System.out.println(fa.statesToString());
        System.out.println(fa.alphabetToString());
        System.out.println(fa.transitionsToString());
        System.out.println(fa.initialStateToString());
        System.out.println(fa.finalStatesToString());
    }
}