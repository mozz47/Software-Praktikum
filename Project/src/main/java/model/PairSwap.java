package model;

public class PairSwap {
    public Participant swappedIn1;
    public Participant swappedIn2;

    public Pair swappedOut;

    public PairSwap(Participant swappedIn1, Participant swappedIn2, Pair swappedOut) {
        this.swappedIn1 = swappedIn1;
        this.swappedIn2 = swappedIn2;
        this.swappedOut = swappedOut;
    }
}
