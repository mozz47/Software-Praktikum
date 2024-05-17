package model;

/**
 * A Cluster is the matching of a pair P1 with all 6 other pairs it encounters throughout the event.
 * The pair P1 meets pairs A1 and A2 at the starter, B1 and B2 at the main course, C1 and C2 at the dessert.
 */
public class Cluster {
    Pair P1;

    // starter
    Pair A1;
    Pair A2;

    // main course
    Pair B1;
    Pair B2;

    // dessert
    Pair C1;
    Pair C2;

    /**
     * returns the starter group.
     */
    public Group getGroupA() {
        return new Group(P1, A1, A2);
    }

    /**
     * returns the main course group.
     */
    public Group getGroupB() {
        return new Group(B1, P1, B2);
    }

    /**
     * returns the dessert group.
     */
    public Group getGroupC() {
        return new Group(C1, C2, P1);
    }
}
