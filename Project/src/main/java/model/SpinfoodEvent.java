package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Singleton class to store Spinfood Event
 */
public class SpinfoodEvent { //TODO I18NIZE HARD CODED STRINGS
    private static SpinfoodEvent event;  // Singleton instance

    public List<Participant> participants;
    public Location partyLocation;
    public List<Criterion> criteria;
    public int maxPairs;

    private List<Pair> pairList;  // private to manage the old pair list
    private List<Pair> pairListOld;
    private List<Group> groupList;  // private to manage the old group list
    private List<Group> groupListOld;
    private List<Participant> successors;  // private to manage the old successors
    private List<Participant> successorsOld;

    private Participant swapCandidate1;
    private Participant swapCandidate2;

    private Stack<PairSwap> pairSwapStack;

    public List<NinePairFormation> ninePairFormations;

    /**
     * private constructor to prevent creating instances from outside the class
     */
    private SpinfoodEvent() {
        this.maxPairs = 0;
        this.participants = null;
        this.successors = null;
        this.partyLocation = null;
        this.pairList = null;
        this.groupList = null;
        this.pairListOld = null;
        this.groupListOld = null;
        this.ninePairFormations = new ArrayList<>();
    }

    /**
     * public method to get the instance of SpinfoodEvent singleton
     *
     * @return SpinfoodEvent
     */
    public static SpinfoodEvent getInstance() {
        if (event == null) {
            event = new SpinfoodEvent();
        }
        return event;
    }

    //For test purposes
    public static void setInstance(SpinfoodEvent event) {
        SpinfoodEvent.event = event;
    }

    /**
     * Updates the pair list, saving the current one in pairListOld.
     *
     * @param pairList new pair list
     */
    public void updatePairList(List<Pair> pairList) {
        this.pairListOld = this.pairList;
        this.pairList = pairList;
    }

    /**
     * Updates the group list, saving the current one in pairListOld.
     *
     * @param groupList new group list
     */
    public void updateGroupList(List<Group> groupList) {
        this.groupListOld = this.groupList;
        this.groupList = groupList;
    }

    /**
     * Updates the successors list, saving the current one in successorsOld.
     *
     * @param successors new successors list
     */
    public void updateSuccessors(List<Participant> successors) {
        this.successorsOld = this.successors;
        this.successors = successors;
    }

    /**
     * Loads the old pair list.
     */
    private void loadOldPairList() {
        List<Pair> temp = this.pairList;
        this.pairList = this.pairListOld;
        this.pairListOld = temp;
    }

    /**
     * Loads the old group list.
     */
    private void loadOldGroupList() {
        List<Group> temp = this.groupList;
        this.groupList = this.groupListOld;
        this.groupListOld = temp;
    }

    /**
     * Loads the old successors list.
     */
    private void loadOldSuccessors() {
        List<Participant> temp = this.successors;
        this.successors = this.successorsOld;
        this.successorsOld = temp;
    }

    /**
     * Restores the old pair list, group list and successors list.
     */
    public void restoreOldEvent() throws IllegalStateException {
        if (this.pairListOld == null || this.groupListOld == null || this.successorsOld == null) {
            throw new IllegalStateException("Old pair list, group list and successors can't be null");
        }
        loadOldPairList();
        loadOldGroupList();
        loadOldSuccessors();
    }

    /**
     * Getter for the pair list.
     *
     * @return pair list
     */
    public List<Pair> getPairList() {
        return pairList;
    }

    /**
     * Getter for the group list.
     *
     * @return group list
     */
    public List<Group> getGroupList() {
        return groupList;
    }

    /**
     * Getter for the successors list.
     *
     * @return successors list
     */
    public List<Participant> getSuccessors() {
        return successors;
    }

    /**
     * prints human-readable version of the participant and party location data
     */

    public String getParticipantKeyFigures() {
        return "Amount: " + participants.size();
    }

    /**
     * Retrieves the old pair list.
     *
     * @return the old pair list
     */
    public List<Pair> getOldPairList() {
        return pairListOld;
    }

    public String getPairKeyFigures() {
        return "Amount: " + pairList.size() + "\n" +
                "Gender Diversity: " + String.format("%.2f", PairList.calculateGenderRatio(pairList)) + "\n" +
                "Average Age Difference: " + String.format("%.2f", PairList.calculateAverageAgeDifference(pairList)) + "\n" +
                "Preference Deviation: " + String.format("%.2f", PairList.calculateFoodPreferenceDifference(pairList)) + "\n" +
                "Number of unmatched Participants (to Pairs): " + unmatchedParticipants() + "\n" +
                "Average Path Length: " + String.format("%.2f", PairList.averagePathLength(pairList)) + "km\n";
    }

    public String getOldPairKeyFigures() {
        return "Amount: " + pairListOld.size() + "\n" +
                "Gender Diversity: " + String.format("%.2f", PairList.calculateGenderRatio(pairListOld)) + "\n" +
                "Average Age Difference: " + String.format("%.2f", PairList.calculateAverageAgeDifference(pairListOld)) + "\n" +
                "Preference Deviation: " + String.format("%.2f", PairList.calculateFoodPreferenceDifference(pairListOld)) + "\n" +
                "Number of unmatched Participants (to Pairs): " + unmatchedParticipants() + "\n" +
                "Average Path Length: " + String.format("%.2f", PairList.averagePathLength(pairListOld)) + "km\n";
    }

    public String getPairDifferenceKeyFigures() {
        return "Amount: " + Math.abs(pairList.size() - pairListOld.size()) + "\n" +
                "Gender Diversity: " + String.format("%.2f", Math.abs(PairList.calculateGenderRatio(pairListOld) - PairList.calculateGenderRatio(pairList))) + "\n" +
                "Average Age Difference: " + String.format("%.2f", Math.abs(PairList.calculateAverageAgeDifference(pairListOld) - PairList.calculateAverageAgeDifference(pairList))) + "\n" +
                "Preference Deviation: " + String.format("%.2f", Math.abs(PairList.calculateFoodPreferenceDifference(pairListOld)- PairList.calculateFoodPreferenceDifference(pairList))) + "\n" +
                "Number of unmatched Participants (to Pairs): " + Math.abs(unmatchedParticipants() - unmatchedParticipantsOldData()) + "\n" +
                "Average Path Length: " + String.format("%.2f", Math.abs(PairList.averagePathLength(pairListOld)-PairList.averagePathLength(pairList))) + "km\n";
    }

    public String getGroupKeyFigures() {
        return "Amount: " + groupList.size() + "\n" +
                "Gender Diversity: " + String.format("%.2f", GroupList.calculateGenderRatio(groupList)) + "\n" +
                "Average Age Difference: " + String.format("%.2f", GroupList.calculateAverageAgeDifference(groupList)) + "\n" +
                "Preference Deviation: " + String.format("%.3f", GroupList.calculateFoodPreferenceDeviation(groupList)) + "\n" +
                "Number of unmatched Pairs (to Groups): " + unmatchedPairs();
    }

    public String getOldGroupKeyFigures() {
        return "Amount: " + groupListOld.size() + "\n" +
                "Gender Diversity: " + String.format("%.2f", GroupList.calculateGenderRatio(groupListOld)) + "\n" +
                "Average Age Difference: " + String.format("%.2f", GroupList.calculateAverageAgeDifference(groupListOld)) + "\n" +
                "Preference Deviation: " + String.format("%.3f", GroupList.calculateFoodPreferenceDeviation(groupListOld)) + "\n" +
                "Number of unmatched Pairs (to Groups): " + unmatchedPairsOldData();
    }

    public String getGroupDifferenceKeyFigures() {
        return "Amount: " + Math.abs(groupListOld.size() - groupList.size()) + "\n" +
                "Gender Diversity: " + String.format("%.2f", Math.abs(GroupList.calculateGenderRatio(groupListOld) - GroupList.calculateGenderRatio(groupList))) + "\n" +
                "Average Age Difference: " + String.format("%.2f", Math.abs(GroupList.calculateAverageAgeDifference(groupListOld) - GroupList.calculateAverageAgeDifference(groupList))) + "\n" +
                "Preference Deviation: " + String.format("%.3f", Math.abs(GroupList.calculateFoodPreferenceDeviation(groupListOld) - GroupList.calculateFoodPreferenceDeviation(groupList))) + "\n" +
                "Number of unmatched Pairs (to Groups): " + Math.abs(unmatchedPairsOldData() - unmatchedPairs());
    }

    /**
     * For integrity checks.
     * Returns true if the old pair, group and successors lists are not null, so they can be restored.
     *
     * @return true if the old pair, group and successors lists are not null
     */
    public boolean hasOldData() {
        return this.pairListOld != null && this.groupListOld != null && this.successorsOld != null;
    }

    /**
     * Checks if the pair list from the previous event is not null.
     *
     * @return true if the pair list from the previous event is not null, false otherwise.
     */
    public boolean hasOldPairData() {
        return this.pairListOld != null;
    }

    private int unmatchedParticipants() {
        return participants.size() - 2 * pairList.size();
    }

    private int unmatchedParticipantsOldData() {
        return participants.size() - 2 * pairListOld.size();
    }

    private int unmatchedPairs() {
        return (successors.size() - unmatchedParticipants()) / 2;
    }

    private int unmatchedPairsOldData() {
        return (successorsOld.size() - unmatchedParticipantsOldData()) / 2;
    }

    public String getSuccessorsKeyFigures() {
        return "Amount: " + successors.size();
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Criterion> getCriteria() {
        return criteria;
    }

    public Participant getSwapCandidate1() {
        return swapCandidate1;
    }

    public void setSwapCandidate1(Participant swapCandidate1) {
        this.swapCandidate1 = swapCandidate1;
    }

    public Participant getSwapCandidate2() {
        return swapCandidate2;
    }

    public void setSwapCandidate2(Participant swapCandidate2) {
        this.swapCandidate2 = swapCandidate2;
    }
    public List<Group> getGroupListOld() {
        return groupListOld;
    }


    public void printInput() {
        System.out.println("Teilnehmer:");
        for (Participant p : participants) {
            System.out.println(p);
        }
        System.out.println("Party Koordinaten:");
        System.out.println(partyLocation);
    }

    public Stack<PairSwap> getPairSwapStack() {
        return pairSwapStack;
    }

    public void setPairSwapStack(Stack<PairSwap> pairSwapStack) {
        this.pairSwapStack = pairSwapStack;
    }
}