import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FA {
    private Set<String> states;
    private Set<String> alphabet;
    private Map<Pair<String, String>, List<String>> transitions;
    private String initialState;
    private Set<String> finalStates;
    private boolean isDFA = true;

    public FA(String faFilePath) throws IOException {
        var bufferedReader = new BufferedReader(new FileReader(faFilePath));

        String statesLine = bufferedReader.readLine().strip();
        states = Set.of(statesLine.split(","));

        String alphabetLine = bufferedReader.readLine().strip();
        alphabet = Set.of(alphabetLine.split(","));

        transitions = new HashMap<>();
        String transition = bufferedReader.readLine().strip();
        while (!transition.equals("transition_set_stop")) {
            String[] transitionElements = transition.split(",");
            String currentState = transitionElements[0];
            String alphabetElement = transitionElements[1];
            String nextState = transitionElements[2];

            transitions.compute(new Pair<>(currentState, alphabetElement), (key, previousNextStates) -> {
                        if (previousNextStates == null) {
                            return new ArrayList<>(Arrays.asList(nextState));
                        }
                        isDFA = false;
                        previousNextStates.add(nextState);
                        return previousNextStates;
                    }
            );

            transition = bufferedReader.readLine().strip();
        }

        initialState = bufferedReader.readLine().strip();

        String finalStatesLine = bufferedReader.readLine().strip();
        finalStates = Set.of(finalStatesLine.split(","));

        bufferedReader.close();
    }

    private List<String> getNextState(String currentState, String currentAlphabetElement) {
        for (var transition : transitions.entrySet()) {
            String state = transition.getKey().getValue0();
            String alphabetElement = transition.getKey().getValue1();
            List<String> nextState = transition.getValue();

            if (state.equals(currentState)) {
                if (alphabetElement.equals(currentAlphabetElement)) {
                    return nextState;
                } else if (alphabetElement.length() > 1) {
                    char rangeStartCharacter = alphabetElement.charAt(0);
                    char rangeEndCharacter = alphabetElement.charAt(2);
                    if (currentAlphabetElement.charAt(0) >= rangeStartCharacter && currentAlphabetElement.charAt(0) <= rangeEndCharacter)
                        return nextState;
                }
            }
        }
        return null;
    }

    private String getMovesForSequenceRecursive(String currentState, String sequence) {
        if (sequence.isEmpty()) {
            if (finalStates.contains(currentState))
                return "(%s, ε)".formatted(currentState);
            else
                return null;
        }

        char currentAlphabetElement = sequence.charAt(0);
        List<String> potentialNextStateList = getNextState(currentState, String.valueOf(currentAlphabetElement));
        if (potentialNextStateList == null)
            return null;
        for (var potentialNextState : potentialNextStateList) {
            String goNextSequence = getMovesForSequenceRecursive(potentialNextState, sequence.substring(1));
            if (goNextSequence != null) {
                return "(%s, %s)|-".formatted(currentState, sequence) + goNextSequence;
            }
        }
        return null;
    }

    public String getMovesForSequenceDFA(String sequence) {
        if (!isDFA)
            return "The FA is not deterministic (DFA)";
        var stringBuilder = new StringBuilder();
        String currentState = initialState;
        int sequenceIndex = 0;
        for (Character currentSequenceCharacter : sequence.toCharArray()) {
            stringBuilder.append("(%s, %s)|-".formatted(currentState, sequence.substring(sequenceIndex)));
//            List<String> nextStates = transitions.get(new Pair<>(currentState, currentSequenceCharacter.toString()));
            List<String> nextStates = getNextState(currentState, currentSequenceCharacter.toString());
            if (nextStates == null)
                break;
            currentState = nextStates.get(0);
            sequenceIndex++;
        }
        if (sequenceIndex == sequence.length() && finalStates.contains(currentState))
            stringBuilder.append("(%s, ε)".formatted(currentState));
        else
            return "Sequence is not valid";
        return stringBuilder.toString();
    }

    public String getMovesForSequence(String sequence) {
        String checkValidSequenceResult;
        if(isDFA)
            checkValidSequenceResult = getMovesForSequenceDFA(sequence);
        else
            checkValidSequenceResult = getMovesForSequenceRecursive(initialState, sequence);
        if (checkValidSequenceResult == null)
            return "Sequence is not valid";
        return checkValidSequenceResult;
    }

    public boolean isSequenceValid(String sequence) {
        return getMovesForSequenceRecursive(initialState, sequence) != null;
    }

    public boolean isDFA(){
        return isDFA;
    }

    public String statesToString() {
        return "Q = " +
                states.toString().replace("[", "{").replace("]", "}");
    }

    public String alphabetToString() {
        return "Σ = " +
                alphabet.toString().replace("[", "{").replace("]", "}");
    }

    public String transitionsToString() {
        var stringBuilder = new StringBuilder();
        transitions.forEach((state_alphabet_pair, nextState) -> {
            String currentState = state_alphabet_pair.getValue0();
            String alphabetElement = state_alphabet_pair.getValue1();
            stringBuilder.append("δ(%s,%s) = %s\n".formatted(currentState, alphabetElement, nextState.toString().replace("[", "").replace("]", "")));
        });

        //delete the last new line
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        return stringBuilder.toString();
    }

    public String initialStateToString() {
        return "q0 = %s".formatted(initialState);
    }

    public String finalStatesToString() {
        return "F = " +
                finalStates.toString().replace("[", "{").replace("]", "}");
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
