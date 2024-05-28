package controller;

import model.*;

import java.util.*;

/**
 * Class to handle Pairs
 */
public class PairListBuilder {

    private static boolean[] used;
    private static List<Participant> successors;

    public static List<Pair> getRegisteredTogetherPairs() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        List<Pair> pairList = new ArrayList<>();
        HashSet<String> processedParticipantsIds = new HashSet<>();

        for (Participant participant : Objects.requireNonNull(event.participants)) {

            if (processedParticipantsIds.contains(participant.id)) { //already processed with previous Participant
                continue;
            }

            //create Pair if registered together, skip if registered alone
            if (participant.partner != null) { //has Partner
                Pair pair = new Pair(participant, participant.partner, true);
                pairList.add(pair);
                processedParticipantsIds.add(participant.id);
                processedParticipantsIds.add(participant.partner.id);
                //System.out.println(pair.shortString());
            }
        }
        return pairList;
    }

    public static List<Participant> getRegisteredAloneParticipants() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        List<Participant> pairList = new ArrayList<>();

        for (Participant participant : Objects.requireNonNull(event.participants)) {

            if (participant.partner == null) {
                pairList.add(participant);
            }
        }
        return pairList;
    }

    private static List<Pair> getGeneratedPairs(List<Criterion> criterions) {
        List<Participant> participants = getRegisteredAloneParticipants();
        List<Pair> joinedPairs = new ArrayList<>();
        used = new boolean[participants.size()];
        successors = new ArrayList<>();
        PairPairingConstraints softConstraints = new PairPairingConstraints(criterions);
        int i;
        int j;
        while (!softConstraints.areConstraintsFullyRelaxed()) {
            for (i = 0; i < participants.size(); i++) {
                if (used[i]) {
                    continue;
                }
                for (j = 1; j < participants.size(); j++) {
                    if (used[j]) {
                        continue;
                    }
                    if (i == j) {
                        continue;
                    }
                    Pair pair = new Pair(participants.get(i), participants.get(j), false);
                    if (pair.isValid() && softConstraints.isValid(participants.get(i), participants.get(j))) { //soft and hard Constraints fulfilled?
                        joinedPairs.add(pair);
                        used[i] = true;
                        used[j] = true;
                        break;
                    }
                }
            }

            for (int z = 0; z < participants.size(); z++) {
                //To check if all participants have been used
                if (!used[z]) {
                    successors.add(participants.get(z));
                }
            }
            if (!successors.isEmpty() && successors.size() > 1) { //maybe more than 1 left over -> check successorsRate and if bigger than allowed, relax constraints
                // Check successorRate - if acceptable rate, end, else try again with more relaxing constraints
                float successorsRate = (float) successors.size() / participants.size();
                if (successorsRate <= softConstraints.getSuccessorAllowedRate()) {
                    break; //acceptable rate, so we break here, if not acceptable: relax constraints and we try again to find pairs with acceptable rate
                } else {
                    softConstraints.relaxConstraints();
                }
                successors.clear();
            }
        }

        boolean successorsLeftOver = collectSuccessors(participants);
        //System.out.println((successorsLeftOver ? "successors left over" : "no successors left over") + ", are constraints fully relaxed: " + softConstraints.areConstraintsFullyRelaxed());
        return joinedPairs;
    }

    public static PairList getPairList(List<Criterion> criterion) {
        List<Pair> registeredTogetherPairs = getRegisteredTogetherPairs();
        List<Pair> generatedPairs = getGeneratedPairs(criterion);
        registeredTogetherPairs.addAll(generatedPairs);
        /*
        for (Pair pair : generatedPairs) {
            System.out.println(pair.shortString());
        }

         */
        return new PairList(registeredTogetherPairs, successors);
    }

    /**
     * Collects the successors from the given list of participants and saves them to a CSV file.
     *
     * @param participants the list of participants to collect successors from
     * @return true if there are successors in the list, false otherwise
     */
    private static boolean collectSuccessors(List<Participant> participants) {
        //TODO Save to CSV
        for (int i = 0; i < participants.size(); i++) {
            if (!used[i]) {
                successors.add(participants.get(i));
            }
        }
        return !successors.isEmpty();
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


        List<Pair> allPairs = getGeneratedPairs(criteria);

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
