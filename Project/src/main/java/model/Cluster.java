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
}
