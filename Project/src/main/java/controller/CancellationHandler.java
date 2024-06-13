package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the cancellation of participants in the SpinfoodEvent.
 * It maintains lists of participants, pairs, groups, and waiting lists.
 * It also provides methods to cancel participants, process cancellations, and update groups.
 */
public class CancellationHandler {

    /**
     * Main functionality method of the class, handles all the cancellations by trying to find successors that
     * can replace the cancelled participants whilst still having valid pairs and groups.
     */
    public static void handleAllCancellations() {
        // read csv
        List<Participant> cancelledParticipants = Reader.getParticipants(Reader.getFilePath("Select Teilnehmer.csv"));
        // handle each cancellation
        if (cancelledParticipants == null || cancelledParticipants.isEmpty()) {
            System.out.println("No one cancelled.");
            return;
        }
        for (Participant p : cancelledParticipants) {
            handleCancellation(p);
        }
    }

    private static void handleCancellation(Participant p) {
        // check if participant registered alone
        boolean registeredAlone = p.partner == null;
        // if registered alone: handleSingleParticipantCancellation
        if (registeredAlone) {
            handleSingleParticipantCancellation(p);
        }
        // else handlePairCancellation
        else {
            handlePairCancellation(p);
        }
    }

    private static void handlePairCancellation(Participant p) {
        // check if there is a pair of successors that can replace the cancelled pair
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        Pair cancelledPair = null;
        for (Pair pair : event.getPairList()) {
            if ((pair.participant1.probablyEquals(p) && pair.participant2.probablyEquals(p.partner))
                    || (pair.participant2.probablyEquals(p) && pair.participant1.probablyEquals(p.partner))) {
                cancelledPair = pair;
            }
        }
        if (cancelledPair == null) {
            System.out.println("Cancelled pair not in pair list");
            return;
        }

        // match successors into pairs
        List<Participant> successors = new ArrayList<>(event.getSuccessors());
        boolean[] used = new boolean[successors.size()];
        List<Pair> joinedPairs = new ArrayList<>();

        for (int i = 0; i < successors.size(); i++) {
            if (used[i]) {
                continue;
            }
            for (int j = 0; j < successors.size(); j++) {  // Start from 0 to check all combinations
                if (used[j]) {
                    continue;
                }
                if (i == j) {
                    continue;
                }
                Pair pair = new Pair(successors.get(i), successors.get(j), false);
                boolean pairValidHardConstraints = pair.isValid();
                if (pairValidHardConstraints) { //only hard constraints
                    joinedPairs.add(pair);
                    used[i] = true;
                    used[j] = true;
                    break;
                }
            }
        }

        // try to replace cancelled pair with each joined pair
        boolean foundValidPair = false;
        for (Pair newPair : joinedPairs) {
            cancelledPair = newPair;
            if (isValidCluster(cancelledPair.cluster)) {
                // found valid cluster
                Participant participant1 = cancelledPair.participant1;
                Participant participant2 = cancelledPair.participant2;
                event.getSuccessors().remove(participant1);
                event.getSuccessors().remove(participant2);
                foundValidPair = true;
            }
        }
        if (!foundValidPair) {
            // delete whole nine pair formation
            cancelledPair.ninePairFormation.deleteAll();
        }
    }

    private static boolean isValidCluster(Cluster cluster) {
        return isValidGroup(cluster.groupA) && isValidGroup(cluster.groupB) && isValidGroup(cluster.groupC);
    }

    private static boolean isValidGroup(Group group) {
        FoodPreference p1 = group.pair1.getMainFoodPreference();
        FoodPreference p2 = group.pair2.getMainFoodPreference();
        FoodPreference p3 = group.pair3.getMainFoodPreference();
        int meatyCount = (p1 == FoodPreference.MEAT ? 1 : 0) + (p2 == FoodPreference.MEAT ? 1 : 0) + (p3 == FoodPreference.MEAT ? 1 : 0);
        int egaliCount = (p1 == FoodPreference.NONE ? 1 : 0) + (p2 == FoodPreference.NONE ? 1 : 0) + (p3 == FoodPreference.NONE ? 1 : 0);
        return meatyCount != 2 || egaliCount == 1;
    }

    private static void handleSingleParticipantCancellation(Participant p) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        // find participant that was left behind
        Participant partnerLess = null;
        Pair partnerLessPair = null;
        for (Pair pair : event.getPairList()) {
            if (pair.participant1.probablyEquals(p)) {
                partnerLess = pair.participant2;
                partnerLessPair = pair;
            }
            if (pair.participant2.probablyEquals(p)) {
                partnerLess = pair.participant1;
                partnerLessPair = pair;
            }
        }
        if (partnerLess == null) {
            System.out.println("Cancelled participant not in a pair");
            return;
        }

        // find participant from successors that can build valid pair with partnerLess
        boolean foundPartner = false;
        for (Participant maybePartner : event.getSuccessors()) {
            //try to match with someone from successors
            partnerLess = maybePartner;
            if (partnerLessPair.isValid() && isValidCluster(partnerLessPair.cluster)) {
                foundPartner = true;
            }
        }
        if (!foundPartner) {
            // delete whole nine pair formation
            partnerLessPair.ninePairFormation.deleteAll();
            return;
        }
    }

}