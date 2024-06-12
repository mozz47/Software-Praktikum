package controller;

import model.Pair;
import model.Participant;
import model.SpinfoodEvent;

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
        // todo
        // build all possible pairs of successors and check if the whole constellation is still valid
        // if yes -> replace cancelledPair with new pair
        // if no constellation is valid -> abort, no good replacement can be found, you need to start whole algo again
    }

    private static void handleSingleParticipantCancellation(Participant p) {
        // todo
        // find participant from successors that can build valid pair with p (cancelledPair)
        // handlePairCancellation(p)
    }

}