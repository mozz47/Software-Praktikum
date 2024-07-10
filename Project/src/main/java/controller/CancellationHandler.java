package controller;

import model.*;
import view.DisplayCallback;
import view.SpinfoodFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public static void handleAllCancellations(DisplayCallback callback) {
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
        callback.displaySuccAndPairs();
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
        Pair oldPair = null;
        for (Pair pair : event.getPairList()) {
            if ((pair.participant1.probablyEquals(p) && pair.participant2.probablyEquals(p.partner))
                    || (pair.participant2.probablyEquals(p) && pair.participant1.probablyEquals(p.partner))) {
                oldPair = pair;
            }
        }
        if (oldPair == null) {
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
        for (Pair newPair : joinedPairs) {
            if (isValidCluster(oldPair.cluster)) {
                // found valid cluster
                newPair.cluster = oldPair.cluster;

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
                for (Pair p2 : event.getPairList()) {
                    if (p2.cluster != null && p2.cluster.containsPair(oldPair)) {
                        //we have to replace oldPair in all Groups
                        p2.cluster.replacePair(oldPair, newPair);
                    }
                }

                // delete from participants
                for (Participant p3 : event.participants) {
                    if (Objects.equals(p3.name, oldPair.participant1.name)) {
                        event.participants.remove(p3);
                        break;
                    }
                }
                for (Participant p3 : event.participants) {
                    if (Objects.equals(p3.name, oldPair.participant2.name)) {
                        event.participants.remove(p3);
                        break;
                    }
                }

                // delete new pair from successors
                for (Participant p3 : event.getSuccessors()) {
                    if (Objects.equals(p3.name, newPair.participant1.name)) {
                        event.participants.remove(p3);
                        break;
                    }
                }
                for (Participant p3 : event.getSuccessors()) {
                    if (Objects.equals(p3.name, newPair.participant2.name)) {
                        event.participants.remove(p3);
                        break;
                    }
                }

                // delete from pair list
                event.getPairList().remove(oldPair);
                return;
            }
        }
        // delete whole nine pair formation
        oldPair.ninePairFormation.deleteAll();
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
        Pair oldPair = null;
        for (Pair pair : event.getPairList()) {
            if (pair.participant1.probablyEquals(p)) {
                partnerLess = pair.participant2;
                oldPair = pair;
            }
            if (pair.participant2.probablyEquals(p)) {
                partnerLess = pair.participant1;
                oldPair = pair;
            }
        }
        if (partnerLess == null) {
            System.out.println("Cancelled participant not in a pair");
            return;
        }

        // find participant from successors that can build valid pair with partnerLess
        for (Participant maybePartner : event.getSuccessors()) {
            //try to match with someone from successors

            Pair newPair = new Pair(partnerLess, maybePartner, false);
            if (newPair.isValid()) {
                newPair.cluster = oldPair.cluster;

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
                for (Pair p2 : event.getPairList()) {
                    if (p2.cluster != null && p2.cluster.containsPair(oldPair)) {
                        //we have to replace oldPair in all Groups
                        p2.cluster.replacePair(oldPair, newPair);
                    }
                }

                System.out.println(p.name);
                System.out.println("Hallo");
                for (Participant p3 : event.participants) {
                    if (Objects.equals(p3.name, p.name)) {
                        event.participants.remove(p3);
                        break;
                    }
                }
                event.getPairList().remove(oldPair);
                event.getSuccessors().remove(maybePartner);
                return;
            }
            // if no match is found we have to delete the whole nine pair formation
            oldPair.ninePairFormation.deleteAll();
        }
    }

}