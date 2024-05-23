package controller;

import model.Pair;
import model.Participant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Class to handle Pairs
 */
public class PairController {
    public static List<Pair> getRegisteredTogetherPairs() {
        List<Pair> pairList = new ArrayList<>();
        HashSet<String> processedParticipantsIds = new HashSet<>();

        for (Participant participant : Objects.requireNonNull(Reader.getParticipants())) {

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

    public static List<Pair> getPairs() {
        List<Pair> pairList = getRegisteredTogetherPairs(); //first get all Pairs which already registered together
        //TODO Algorithm
        return null;
    }
}
