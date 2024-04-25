package model;

public class Pair {
    public Participant participant1;
    public Participant participant2;

    public int getAgeDifference() {
        return Math.abs(participant1.age - participant2.age);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "participant1=" + participant1 +
                ", participant2=" + participant2 +
                '}';
    }
}
