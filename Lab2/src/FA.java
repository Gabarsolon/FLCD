import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FA {
    Set<String> states;
    Set<String> alphabet;
    Map<Pair<String, Character>, Object> transitions;
    String initialState;
    Set<String> finalStates;
    boolean isDFA = true;

    public FA(String faFilePath) throws IOException {
        var bufferedReader = new BufferedReader(new FileReader(faFilePath));

        String statesLine = bufferedReader.readLine().replace("\n", "");
        states = Set.of(statesLine.split(","));

        String alphabetLine = bufferedReader.readLine().replace("\n", "");
        alphabet = Set.of(alphabetLine.split(","));

        transitions = new HashMap<>();
        String transition = bufferedReader.readLine().replace("\n", "");
        while (!transition.equals("transition_set_stop")) {
            String[] transitionElements = transition.split(",");
            String currentState = transitionElements[0];
            Character alphabetElement = transitionElements[1].charAt(0);
            String nextState = transitionElements[2];

            transitions.compute(new Pair<>(currentState, alphabetElement), (key, previousNextStates) -> {
                        if (previousNextStates == null)
                            return nextState;
                        else if (previousNextStates instanceof String){
                            isDFA = false;
                            return new ArrayList<>(Arrays.asList(previousNextStates, nextState));
                        }
                        List<String> states = (List<String>) previousNextStates;
                        states.add(nextState);
                        return states;
                    }
            );

            transition = bufferedReader.readLine().replace("\n", "");
        }

        initialState = bufferedReader.readLine().replace("\n", "");

        String finalStatesLine = bufferedReader.readLine().replace("\n", "");
        finalStates = Set.of(finalStatesLine.split(","));

        bufferedReader.close();
    }

    public boolean checkValidSequence(String sequence){
        if(!isDFA)
            return false;
        String currentState = initialState;
        for(var currentTerminal : sequence.toCharArray()){
            String nextState = (String) transitions.get(new Pair<>(currentState, currentTerminal));
            if(nextState == null)
                return false;
            currentState = nextState;
        }
        return finalStates.contains(currentState);
    }
    public String statesToString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Q = ");
        stringBuilder.append(states.toString().replace("[", "{").replace("]", "}"));
        return stringBuilder.toString();
    }

    public String alphabetToString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Σ = ");
        stringBuilder.append(alphabet.toString().replace("[", "{").replace("]", "}"));
        return stringBuilder.toString();
    }

    public String transitionsToString() {
        var stringBuilder = new StringBuilder();
        transitions.forEach((state_alphabet_pair, nextState) -> {
            String currentState = state_alphabet_pair.getValue0();
            Character alphabetElement = state_alphabet_pair.getValue1();
            stringBuilder.append("δ(%s,%s) = %s\n".formatted(currentState, alphabetElement, nextState));
        });

        //delete the last new line
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        return stringBuilder.toString();
    }

    public String initialStateToString() {
        return "q0 = %s".formatted(initialState);
    }

    public String finalStatesToString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("F = ");
        stringBuilder.append(finalStates.toString().replace("[", "{").replace("]", "}"));
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "FA{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", transitions=" + transitions +
                ", initialState='" + initialState + '\'' +
                ", finalStates=" + finalStates +
                '}';
    }
}
