package model;

/**
 * Represents a group consisting of three pairs.
 */
public class Group {
    public Pair pair1;
    public Pair pair2;
    public Pair pair3;

    /**
     * Simple constructor for Group.
     */
    public Group(Pair pair1, Pair pair2, Pair pair3) {
        this.pair1 = pair1;
        this.pair2 = pair2;
        this.pair3 = pair3;
    }
}
