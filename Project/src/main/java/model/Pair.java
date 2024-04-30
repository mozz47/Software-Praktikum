package model;

public class Pair {
    public Participant participant1;
    public Participant participant2;

    /**
     * returns age difference of the two participant in absolute terms
     */
    public int getAgeDifference() {
        return Math.abs(participant1.age - participant2.age);
    }

    /**
     * returns human-readable string version of the pair
     */
    @Override
    public String toString() {
        return "Pair{" +
                "participant1=" + participant1 +
                ", participant2=" + participant2 +
                '}';
    }
}
