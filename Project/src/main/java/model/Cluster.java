package model;

import java.util.ArrayList;
import java.util.List;

/**
 * A Cluster is the matching of a pair P1 with all 6 other pairs it encounters throughout the event.
 * The pair P1 meets pairs A1 and A2 at the starter, B1 and B2 at the main course, C1 and C2 at the dessert.
 */
public class Cluster {
    public Group groupA;  // starter group
    public Group groupB;  // main course group
    public Group groupC;  // dessert group

    /**
     * Constructor
     *
     * @param groupA starter
     * @param groupB main course
     * @param groupC dessert
     */
    public Cluster(Group groupA, Group groupB, Group groupC) {
        this.groupA = groupA;
        this.groupB = groupB;
        this.groupC = groupC;
    }

    /**
     * Returns groups as list.
     *
     * @return the list
     */
    public List<Group> getGroups() {
        List<Group> out = new ArrayList<>();
        out.add(groupA);
        out.add(groupB);
        out.add(groupC);
        return out;
    }

    /**
     * Identifies the meal that the given pair has to cook in this cluster.
     * @param pair the pair to check
     * @return the meal type that the pair has to cook, or -1 if the pair does not have to cook
     */
    public int getCookingMeal(Pair pair) {
        if (groupA.pairWithKitchen.equals(pair)) {
            return 1; // Starter
        } else if (groupB.pairWithKitchen.equals(pair)) {
            return 2; // Main Course
        } else if (groupC.pairWithKitchen.equals(pair)) {
            return 3; // Dessert
        }
        return -1; // This pair does not have to cook !should never be the case!
    }

    @Override
    public String toString() {
        return "Starter: Gruppe " + groupA.id + "\n" +
                "Mean Course: Gruppe " + groupB.id + "\n" +
                "Dessert: Gruppe " + groupC.id;
    }
}
