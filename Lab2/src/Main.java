import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    static Scanner inputScanner = new Scanner(System.in);
    static LexicalScanner scanner;

    static {
        try {
            scanner = new LexicalScanner("src/programs/p1.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static FA fa;

    static {
        try {
            fa = new FA("src/specifications/FA.in");
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
            case "scan":
                scanner.scan();
                break;
            case "display_fa":
                while (true) {
                    printFAMenu();
                    String faMenuOption = faMenuOptionsMap.get(inputScanner.nextInt());
                    if (faMenuOption == "go_back")
                        break;
                    handleDisplayFAMenu(faMenuOption);
                }
                break;
            case "check_seq":
                System.out.print("Input sequence: ");
                String sequence = inputScanner.next();
                System.out.println(fa.getMovesForSequence(sequence));
                break;
            case "quit":
                exit(0);
                break;
            default:
                System.out.println("Invalid option");
        }
        System.out.println("-------------------------------------------");
    }

    public static void handleDisplayFAMenu(String option) {
        System.out.println("-------------------------------------------");

        switch (option) {
            case "states":
                System.out.println(fa.statesToString());
                break;
            case "alphabet":
                System.out.println(fa.alphabetToString());
                break;
            case "transitions":
                System.out.println(fa.transitionsToString());
                break;
            case "initial_state":
                System.out.println(fa.initialStateToString());
                break;
            case "final_states":
                System.out.println(fa.finalStatesToString());
                break;
            default:
                System.out.println("Invalid option");
        }
        System.out.println("-------------------------------------------");

    }

    public static void main(String[] args) throws IOException {
        int selectedOption;

        while (true) {
            printMenu();
            selectedOption = inputScanner.nextInt();
            handleSelectedMenuOption(menuOptionsMap.get(selectedOption));
        }
    }
}