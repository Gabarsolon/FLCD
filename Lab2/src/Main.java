public class Main {
    public static void main(String[] args) {
        SymbolTable<String>symbolTable = new SymbolTable<>();
        System.out.println(symbolTable.isEmpty());
        symbolTable.add("a");
        symbolTable.add("b");
        symbolTable.add("c");
        symbolTable.add("d");
        symbolTable.add("3");
        symbolTable.add("\"Message\"");
        System.out.println(symbolTable.size());
        System.out.println(symbolTable.get("b"));
        System.out.println(symbolTable.remove("b"));
        System.out.println(symbolTable.remove("b"));
        System.out.println(symbolTable.get("b"));
        System.out.println(symbolTable.get("3"));
        System.out.println(symbolTable.add("a"));
        System.out.println(symbolTable.get("a"));
        PIF pif = new PIF();
        System.out.println(pif);
        pif.addIdentifierOrConstant("a", symbolTable.add("a"));
        pif.addOperatorSeparatorReservedWord("while");
        System.out.println(pif);
    }
}