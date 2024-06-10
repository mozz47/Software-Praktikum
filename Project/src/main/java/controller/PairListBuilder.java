package controller;

import model.*;

import java.util.*;

/**
 * Class to handle Pairs
 */
public class PairListBuilder {

    private static boolean[] used;
    private static List<Participant> successors;

    static List<Pair> getRegisteredTogetherPairs() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        List<Pair> pairList = new ArrayList<>();
        HashSet<String> processedParticipantsIds = new HashSet<>();

        for (Participant participant : Objects.requireNonNull(event.participants)) {
            if (processedParticipantsIds.contains(participant.id)) { // already processed with previous Participant
                continue;
            }

            // create Pair if registered together, skip if registered alone
            if (participant.partner != null) { // has Partner
                Pair pair = new Pair(participant, participant.partner, true);
                pairList.add(pair);
                processedParticipantsIds.add(participant.id);
                processedParticipantsIds.add(participant.partner.id);
                //System.out.println(pair.shortString());
            }
        }
        return pairList;
    }

    static List<Participant> getRegisteredAloneParticipants() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        List<Participant> pairList = new ArrayList<>();

        for (Participant participant : Objects.requireNonNull(event.participants)) {
            if (participant.partner == null) {
                pairList.add(participant);
            }
        }
        return pairList;
    }

    static List<Pair> getGeneratedPairs(PairPairingConstraints constraints) {
        List<Participant> participants = getRegisteredAloneParticipants();
        List<Pair> joinedPairs = new ArrayList<>();
        used = new boolean[participants.size()];
        successors = new ArrayList<>();
        int i;
        int j;
        while (!constraints.areConstraintsFullyRelaxed()) {
            for (i = 0; i < participants.size(); i++) {
                if (used[i]) {
                    continue;
                }
                for (j = 0; j < participants.size(); j++) {  // Start from 0 to check all combinations
                    if (used[j]) {
                        continue;
                    }
                    if (i == j) {
                        continue;
                    }
                    System.out.println("Trying to pair: " + participants.get(i).name + " and " + participants.get(j).name);
                    Pair pair = new Pair(participants.get(i), participants.get(j), false);
                    boolean pairValidHardConstraints = pair.isValid();
                    boolean pairValidSoftConstraints = constraints.isValid(participants.get(i), participants.get(j));
                    if (pairValidHardConstraints && pairValidSoftConstraints) { // soft and hard Constraints fulfilled?
                        joinedPairs.add(pair);
                        used[i] = true;
                        used[j] = true;
                        break;
                    } else {
                        System.out.println("Pair invalid: " + participants.get(i).name + ", " + participants.get(j).name);
                    }
                }
            }

            for (int z = 0; z < participants.size(); z++) {
                // To check if all participants have been used
                if (!used[z]) {
                    successors.add(participants.get(z));
                }
            }

            if (!successors.isEmpty() && successors.size() > 1) { // maybe more than 1 left over -> check successorsRate and if bigger than allowed, relax constraints
                // Check successorRate - if acceptable rate, end, else try again with more relaxing constraints
                float successorsRate = (float) successors.size() / participants.size();
                if (successorsRate <= constraints.getSuccessorAllowedRate()) {
                    break; // acceptable rate, so we break here, if not acceptable: relax constraints and we try again to find pairs with acceptable rate
                } else {
                    constraints.relaxConstraints();
                }
                successors.clear();
            }
        }

        //Now all Constraints are fully relaxed, try last time to match pairs with hard-constraints only but only if joinedPairs completely empty
        //because normally we dont want to match only by hard-constraints but if not found any pairs, try as last chance
        if (joinedPairs.isEmpty()) {
            for (i = 0; i < participants.size(); i++) {
                if (used[i]) {
                    continue;
                }
                for (j = 0; j < participants.size(); j++) {  // Start from 0 to check all combinations
                    if (used[j]) {
                        continue;
                    }
                    if (i == j) {
                        continue;
                    }
                    System.out.println("Trying to pair: " + participants.get(i).name + " and " + participants.get(j).name);
                    Pair pair = new Pair(participants.get(i), participants.get(j), false);
                    boolean pairValidHardConstraints = pair.isValid();
                    if (pairValidHardConstraints) { //only hard constraints
                        joinedPairs.add(pair);
                        used[i] = true;
                        used[j] = true;
                        break;
                    } else {
                        System.out.println("Pair invalid: " + participants.get(i).name + ", " + participants.get(j).name);
                    }
                }
            }
            //collect succ.
            for (int z = 0; z < participants.size(); z++) {
                // To check if all participants have been used
                if (!used[z]) {
                    successors.add(participants.get(z));
                }
            }
        }



        System.out.println((successors.isEmpty() ? "No successors left over" : "Some successors left over") + ", are constraints fully relaxed: " + constraints.areConstraintsFullyRelaxed());
        return joinedPairs;
    }

    public static PairList getPairList(List<Criterion> criterion) {
        List<Pair> registeredTogetherPairs = getRegisteredTogetherPairs();
        PairPairingConstraints constraints = new PairPairingConstraints(criterion);
        List<Pair> generatedPairs = getGeneratedPairs(constraints);
        registeredTogetherPairs.addAll(generatedPairs);
        /*
        for (Pair pair : generatedPairs) {
            System.out.println(pair.shortString());
        }
         */
        return new PairList(registeredTogetherPairs, successors);
    }

    static List<Participant> getSuccessors() {
        return successors;
    }

    public static void main(String[] args) {
        // Pairs für Teilnehmer, die sich alleine angemeldet haben
        Main.initializeWithoutFileChooser(); // load test event

        List<Criterion> criteria = new ArrayList<>();

        criteria.add(Criterion.Criterion_10_Group_Amount);
        criteria.add(Criterion.Criterion_08_Sex_Diversity);
        criteria.add(Criterion.Criterion_06_Food_Preference);
        criteria.add(Criterion.Criterion_07_Age_Difference);
        criteria.add(Criterion.Criterion_09_Path_Length);

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> allPairs = getGeneratedPairs(constraints);

        System.out.println("Pairs: ");
        for (Pair pair : allPairs) {
            System.out.println(pair.shortString());
        }
        System.out.println("Successors: ");
        for (Participant successor : successors) {
            System.out.println(successor.name);
        }
        System.out.println(successors.size() + " successors size");
    }
}
