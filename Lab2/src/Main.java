public class Main {
    public static void main(String[] args) {
        SymbolTable<String, Integer> symbolTable = new SymbolTable<>();
        System.out.println(symbolTable.isEmpty());
        symbolTable.add("a", 0);
        symbolTable.add("b", 1);
        symbolTable.add("c", 2);
        symbolTable.add("d", 3);
        symbolTable.add("3", 4);
        symbolTable.add("Message", 5);
        System.out.println(symbolTable.size());
        System.out.println(symbolTable.get("b"));
        System.out.println(symbolTable.remove("b"));
        System.out.println(symbolTable.remove("b"));
        System.out.println(symbolTable.get("b"));
        System.out.println(symbolTable.get("3"));
        System.out.println(symbolTable.add("a", 6));
        System.out.println(symbolTable.get("a"));
    }
}