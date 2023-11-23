import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;
//1.c
public class Main {
    static Scanner inputScanner = new Scanner(System.in);
    static LexicalScanner scanner;

    static {
        try {
            scanner = new LexicalScanner("src/programs/p1err.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static FA fa;

    static {
        try {
            fa = new FA("src/specifications/FA_integer_constant.in");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Integer, String> menuOptionsMap = Map.of(
            1, "scan",
            2, "display_fa",
            3, "check_seq",
            0, "quit"
    );
    public static Map<Integer, String> faMenuOptionsMap = Map.of(
            1, "states",
            2, "alphabet",
            3, "transitions",
            4, "initial_state",
            5, "final_states",
            0, "go_back"
    );

    public static void printMenu() {
        System.out.println("1.Scan program file");
        System.out.println("2.Display FA elements");
        System.out.println("3.Check valid sequence");
        System.out.println("0.Quit");
        System.out.print("Pick an option: ");
    }

    public static void printFAMenu() {
        System.out.println("1.States");
        System.out.println("2.Alphabet");
        System.out.println("3.Transitions");
        System.out.println("4.Initial state");
        System.out.println("5.Final states");
        System.out.println("0.Go back");
        System.out.print("Pick an option: ");
    }

    public static void handleSelectedMenuOption(String option) throws IOException {
        System.out.println("-------------------------------------------");

        switch (option) {
            case "scan" -> scanner.scan();
            case "display_fa" -> {
                while (true) {
                    try{
                        printFAMenu();
                        String faMenuOption = faMenuOptionsMap.get(inputScanner.nextInt());
                        if (faMenuOption == "go_back")
                            break;
                        handleDisplayFAMenu(faMenuOption);
                    }
                    catch (Exception exception){
                        System.out.println("Invalid option");
                        System.out.println("-------------------------------------------");
                    }
                }
            }
            case "check_seq" -> {
                System.out.print("Input sequence: ");
                String sequence = inputScanner.next();
                System.out.println(fa.getMovesForSequence(sequence));
            }
            case "quit" -> exit(0);
            default -> System.out.println("Invalid option");
        }
        System.out.println("-------------------------------------------");
    }

    public static void handleDisplayFAMenu(String option) {
        System.out.println("-------------------------------------------");

        switch (option) {
            case "states" -> System.out.println(fa.statesToString());
            case "alphabet" -> System.out.println(fa.alphabetToString());
            case "transitions" -> System.out.println(fa.transitionsToString());
            case "initial_state" -> System.out.println(fa.initialStateToString());
            case "final_states" -> System.out.println(fa.finalStatesToString());
            default -> System.out.println("Invalid option");
        }
        System.out.println("-------------------------------------------");

    }

    public static void main(String[] args) throws IOException {
        int selectedOption;

        while (true) {
            try{
                printMenu();
                selectedOption = inputScanner.nextInt();
                handleSelectedMenuOption(menuOptionsMap.get(selectedOption));
            }catch (Exception exception){
                System.out.println("Invalid option");
                System.out.println(exception);
                System.out.println("-------------------------------------------");
            }
        }
    }
}