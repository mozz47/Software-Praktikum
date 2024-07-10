package controller;

import model.*;

import java.util.Objects;

public class CandidateSwapper {

    public static void chooseSuccessor(Participant chosen) {
        if (chosen == null) {
            System.out.println("No one chosen");
            return;
        }
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getSwapCandidate1() == null) {
            event.setSwapCandidate1(chosen);
        }
        else if (event.getSwapCandidate2() == null) {
            if (chosen.probablyEquals(event.getSwapCandidate1())) {
                System.out.println("already chosen");
                return;
            }
            event.setSwapCandidate2(chosen);
        }
        // if full, do nothing
    }

    public static void swap(Pair oldPair, boolean isUndo) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getSwapCandidate1() == null && event.getSwapCandidate2() == null) {
            throw new RuntimeException("You need two successors to swap");
        }
        Pair newPair = new Pair(event.getSwapCandidate1(), event.getSwapCandidate2(), false);
        if (!newPair.isValid()) {
            throw new RuntimeException("Pair from successors to replace is not valid"); //TODO?
        }

        // set cluster
        newPair.cluster = oldPair.cluster;

        // move old pair to successors
        event.getSuccessors().add(oldPair.participant1);
        event.getSuccessors().add(oldPair.participant2);

        //remove from new pair from successors
        event.getSuccessors().remove(event.getSwapCandidate1());
        event.getSuccessors().remove(event.getSwapCandidate2());

        // save swap to stack
        if (!isUndo) {
            event.getPairSwapStack().push(new PairSwap(event.getSwapCandidate1(), event.getSwapCandidate2(), oldPair));
        }
        else {
            event.getPairSwapRedoStack().push(new PairSwap(event.getSwapCandidate1(), event.getSwapCandidate2(), oldPair));
        }

        //Integrity checks -> at least need to have a kitchen -> throw warning, that constellations will be broken (for example maybe meaties in vegan group)
        //TODO

        //PairList Update
        event.getPairList().add(newPair);

        //update all groups
        for (Group g : event.getGroupList()) {
            if (Objects.equals(g.pair1.participant1.name, oldPair.participant1.name) && Objects.equals(g.pair1.participant2.name, oldPair.participant2.name)) {
                g.pair1 = newPair;
            }
            if (Objects.equals(g.pair2.participant1.name, oldPair.participant1.name) && Objects.equals(g.pair2.participant2.name, oldPair.participant2.name)) {
                g.pair2 = newPair;
            }
            if (Objects.equals(g.pair3.participant1.name, oldPair.participant1.name) && Objects.equals(g.pair3.participant2.name, oldPair.participant2.name)) {
                g.pair3 = newPair;
            }
            if (Objects.equals(g.pairWithKitchen.participant1.name, oldPair.participant1.name) && Objects.equals(g.pairWithKitchen.participant2.name, oldPair.participant2.name)) {
                g.pairWithKitchen = newPair;
            }
        }

        //update all Clusters
        for (Pair p : event.getPairList()) {
            if (p.cluster != null && p.cluster.containsPair(oldPair)) {
                //we have to replace oldPair in all Groups
                p.cluster.replacePair(oldPair, newPair);
            }
        }
    }

    public static void undo() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getPairSwapStack().size() == 0) {
            System.out.println("No swap to undo");
            return;
        }
        PairSwap swap = event.getPairSwapStack().pop();
        event.setSwapCandidate1(swap.swappedOut.participant1);
        event.setSwapCandidate2(swap.swappedOut.participant2);
        for (Pair p : event.getPairList()) {
            if (p.participant1.name.equals(swap.swappedIn1.name) && p.participant2.name.equals(swap.swappedIn2.name)) {
                System.out.println("Undo");
                swap(p, true);
                return;
            }
        }
        System.out.println("Undo failed");
    }

    public static void redo() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getPairSwapRedoStack().size() == 0) {
            System.out.println("No swap to redo");
            return;
        }
        PairSwap swap = event.getPairSwapRedoStack().pop();
        event.setSwapCandidate1(swap.swappedOut.participant1);
        event.setSwapCandidate2(swap.swappedOut.participant2);
        for (Pair p : event.getPairList()) {
            if (p.participant1.name.equals(swap.swappedIn1.name) && p.participant2.name.equals(swap.swappedIn2.name)) {
                System.out.println("Redo");
                swap(p, false);
                return;
            }
        }
        System.out.println("Redo failed");
    }
}
