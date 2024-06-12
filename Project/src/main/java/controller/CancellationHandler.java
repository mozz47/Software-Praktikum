package controller;
/**
 * This class handles the cancellation of participants in the SpinfoodEvent.
 * It maintains lists of participants, pairs, groups, and waiting lists.
 * It also provides methods to cancel participants, process cancellations, and update groups.
 */
import model.Participant;
import model.SpinfoodEvent;

import java.util.ArrayList;
import java.util.List;

public class CancellationHandler {
    private int maxGroups;
    private List<Integer> cancellations;
    private List<Participant> pairList;
    private List<List<Participant>> groupList;
    private List<Participant> waitingList;
    private List<Participant> waitingListPairs;

    public CancellationHandler(int maxGroups) {
        this.maxGroups = maxGroups;
        this.cancellations = new ArrayList<>();
        this.pairList = new ArrayList<>();
        this.groupList = new ArrayList<>();
        this.waitingList = new ArrayList<>();
        this.waitingListPairs = new ArrayList<>();
    }

    /**
     * Adds the specified participant ID to the list of cancellations.
     *
     * @param participantId the ID of the participant to cancel
     */
    public void cancelParticipant(String participantId) {
        cancellations.add(Integer.valueOf(String.valueOf(participantId)));
    }

    /**
     * Processes all cancellations in the list of cancellations.
     */
    public void processCancellations() {
        for (int participantId : cancellations) {
            handleCancellation(String.valueOf(participantId));
        }
        cancellations.clear();
    }

    private void handleCancellation(String participantId) {
        Participant participant = findParticipant(participantId);
        if (participant == null) {
            return;
        }

        if (participant.id != null) {
            handlePairCancellation(participant);
        } else {
            handleSingleCancellation(participant);
        }
    }

    private void handleSingleCancellation(Participant participant) {
        Participant replacement = getNextFromWaitingList();
        if (replacement != null) {
            replaceParticipant(participant, replacement);
        }
    }

    private void handlePairCancellation(Participant participant) {
        Participant pair = findParticipant(participant.id);
        if (pair == null) {
            return;
        }

        if (cancellations.contains(pair.id)) {
            Participant[] replacementPair = getNextPairFromWaitingList();
            if (replacementPair != null) {
                replacePair(participant, pair, replacementPair);
            }
        } else {
            moveToWaitingList(pair);
            Participant newPairMember = getNextFromWaitingList();
            if (newPairMember != null) {
                formNewPair(pair, newPairMember);
            }
        }
    }

    /**
     * Finds a participant with the specified ID.
     *
     * @param participantId the ID of the participant to find
     * @return the participant with the specified ID, or null if no such participant is found
     */
    private Participant findParticipant(String participantId) {
        // Get the list of participants from the SpinfoodEvent singleton
        List<Participant> participants = SpinfoodEvent.getInstance().getParticipants();

        // Iterate over the list of participants
        for (Participant participant : participants) {
            // If the participant's ID matches the provided ID, return the participant
            if (participant.getId().equals(participantId)) {
                return participant;
            }
        }

        // If no participant with the provided ID is found, return null
        return null;
    }

    private Participant getNextFromWaitingList() {
        if (!waitingList.isEmpty()) {
            return waitingList.remove(0);
        }
        return null;
    }

    private Participant[] getNextPairFromWaitingList() {
        if (waitingListPairs.size() >= 2) {
            return new Participant[]{waitingListPairs.remove(0), waitingListPairs.remove(0)};
        }
        return null;
    }

    private void moveToWaitingList(Participant participant) {
        waitingList.add(participant);
        pairList.remove(participant);
    }

    private void formNewPair(Participant participant1, Participant participant2) {
        participant1.id = participant2.id;
        participant2.id = participant1.id;
        waitingList.remove(participant2);
        waitingListPairs.add(participant1);
        waitingListPairs.add(participant2);
    }

    private void replaceParticipant(Participant oldParticipant, Participant newParticipant) {
        int index = pairList.indexOf(oldParticipant);
        pairList.set(index, newParticipant);
    }

    private void replacePair(Participant oldParticipant1, Participant oldParticipant2, Participant[] newPair) {
        pairList.remove(oldParticipant1);
        pairList.remove(oldParticipant2);
        pairList.add(newPair[0]);
        pairList.add(newPair[1]);
    }

    /**
     * Updates the groups based on the current state of the waiting list and the maximum number of groups.
     */
    public void updateGroups() {
        if (waitingListPairs.size() >= 6 && groupList.size() < maxGroups) {
            formNewGroup();
        }
    }

    private void formNewGroup() {
        List<Participant> newGroup = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            newGroup.add(waitingListPairs.remove(0));
        }
        groupList.add(newGroup);
    }
    // Getters for testing

    /**
     * Returns the list of pairs for testing purposes.
     *
     * @return the list of pairs
     */
    public List<Participant> getPairList() {
        return pairList;
    }

    /**
     * Returns the waiting list for testing purposes.
     *
     * @return the waiting list
     */
    public List<Participant> getWaitingList() {
        return waitingList;
    }

    /**
     * Returns the list of groups for testing purposes.
     *
     * @return the list of groups
     */
    public List<List<Participant>> getGroupList() {
        return groupList;
    }

}