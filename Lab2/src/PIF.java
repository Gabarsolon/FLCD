import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class PIF {
    final Integer OPERATOR_SEPARATOR_RESERVEDWORD_POSITION = -1;
    private List<Pair<String, Integer>> data = new ArrayList<>();

    public void addIdentifierOrConstant(String token, Integer positionInSymbolTable) {
        data.add(new Pair<>(token, positionInSymbolTable));
    }

    public void addOperatorSeparatorReservedWord(String token) {
        data.add(new Pair<>(token, OPERATOR_SEPARATOR_RESERVEDWORD_POSITION));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (var pair : data) {
            stringBuilder.append("%-20s %3d\n".formatted(pair.getValue0(), pair.getValue1()));
        }
        return stringBuilder.toString();
    }
}
