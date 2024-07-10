package controller;

import model.*;

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

    public static void swap(Pair oldPair) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getSwapCandidate1() == null && event.getSwapCandidate2() == null) {
            throw new RuntimeException("You need two successors to swap");
        }
        Pair newPair = new Pair(event.getSwapCandidate1(), event.getSwapCandidate2(), false);
        if (!newPair.isValid()) {
            throw new RuntimeException("Pair from successors to replace is not valid"); //TODO?
        }
        // move old pair to successors
        event.getSuccessors().add(oldPair.participant1);
        event.getSuccessors().add(oldPair.participant2);
        // save swap to stack
        event.getPairSwapStack().push(new PairSwap(event.getSwapCandidate1(), event.getSwapCandidate2(), oldPair));

        //Integrity checks -> at least need to have a kitchen -> throw warning, that constellations will be broken (for example maybe meaties in vegan group)
        //TODO

        //PairList Update
        event.getPairList().remove(oldPair);
        event.getPairList().add(newPair);

        //update all groups

        for (Group g : event.getGroupList()) {
            if (g.pair1 == oldPair) {
                g.pair1 = newPair;
            }
            if (g.pair2 == oldPair) {
                g.pair2 = newPair;
            }
            if (g.pair3 == oldPair) {
                g.pair3 = newPair;
            }
            if (g.pairWithKitchen == oldPair) {
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

    public static void swap(Participant oldParticipant) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getSwapCandidate1() == null) {
            throw new RuntimeException("You need successor to swap");
        }

    }
}
