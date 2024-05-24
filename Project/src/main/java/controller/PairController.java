package controller;

import com.sun.tools.javac.Main;
import model.*;

import java.util.*;

/**
 * Class to handle Pairs
 */
public class PairController {
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

    private static boolean[] used;
    private static List<Participant> successors;

    private static List<Pair> getGeneratedPairs(List<Criterion> criterions) {
        List<Participant> participants = getRegisteredAloneParticipants();
        List<Pair> joinedPairs = new ArrayList<>();
        used = new boolean[participants.size()];
        successors = new ArrayList<>();
        int[] successorsAmount = new int[15];
        PairPairingConstraints softConstraints = new PairPairingConstraints(criterions);
        int i;
        int j;
        int indexSuccessorsAmount = 0;
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
            successorsAmount[indexSuccessorsAmount++] = successors.size();
            if (!successors.isEmpty() && successors.size() > 1) { //maybe more than 1 left over -> relax constraints and try again
                softConstraints.relaxConstraints();
                //TODO if succsesors are same time 2 times in a row -> break or Look at position minimizeSuccessors
                // for example if minimizeSuccessors at 1, then only 5% succsessor rate will be allowed
                // if at 1
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
        MainController.initializeWithoutFileChooser(); // startEvent

        List<Pair> allPairs = getGeneratedPairs(List.of(
                Criterion.Criterion_06_Food_Preference,
                Criterion.Criterion_07_Age_Difference,
                Criterion.Criterion_08_Sex_Diversity,
                Criterion.Criterion_10_Group_Amount,
                Criterion.Criterion_09_Path_Length));

        System.out.println("Pairs (registered alone): ");
        for (Pair pair : allPairs) {
            System.out.println(pair.shortString());
        }
        System.out.println("Successors: ");
        for (Participant successor : successors) {
            System.out.println(successor.name);
        }

    }

}
