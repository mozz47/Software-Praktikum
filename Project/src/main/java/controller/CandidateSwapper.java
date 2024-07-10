package controller;

import model.Pair;
import model.Participant;
import model.SpinfoodEvent;

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

    public static Pair swap(Pair selected) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.getSwapCandidate1() != null && event.getSwapCandidate2() != null) {
            return new Pair(event.getSwapCandidate1(), event.getSwapCandidate2(), false);
        }
        // if not both swap candidates are chosen, do nothing
        return null;
    }
}
