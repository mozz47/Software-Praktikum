package view;

import model.Group;
import model.Pair;
import model.Participant;

import java.util.List;

public interface PairDisplayCallback {
    void printToConsole(String message);

    void displayPairs(List<Pair> pairs);
    void displayGroups(List<Group> groups);

    void displaySuccessors(List<Participant> successors);
}
