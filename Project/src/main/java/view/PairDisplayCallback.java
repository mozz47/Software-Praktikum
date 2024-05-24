package view;

import model.Pair;

import java.util.List;

public interface PairDisplayCallback {
    void printToConsole(String message);

    void displayPairs(List<Pair> pairs);
}
