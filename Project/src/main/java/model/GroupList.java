package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Static helper class for calculating some key figures of the group list.
 */
public class GroupList {

    /**
     * Calculates the gender ratio of the group list by dividing the number of female participants by the total.
     *
     * @param groupList group list
     * @return the gender ratio
     */
    public static double calculateGenderRatio(List<Group> groupList) {
        if (groupList.isEmpty()) return 0.0;
        int femaleCount = 0;
        for (Group group : groupList) {
            femaleCount += group.pair1.participant1.sex == Sex.FEMALE ? 1 : 0;
            femaleCount += group.pair2.participant1.sex == Sex.FEMALE ? 1 : 0;
            femaleCount += group.pair3.participant1.sex == Sex.FEMALE ? 1 : 0;
            femaleCount += group.pair1.participant2.sex == Sex.FEMALE ? 1 : 0;
            femaleCount += group.pair2.participant2.sex == Sex.FEMALE ? 1 : 0;
            femaleCount += group.pair3.participant2.sex == Sex.FEMALE ? 1 : 0;
        }
        return ((double) femaleCount) / (6 * groupList.size()); // every group has 6 participants
    }

    /**
     * Calculates the average age difference among the participants in a group list.
     *
     * @param groupList the list of groups containing pairs
     * @return the average age difference
     */
    public static double calculateAverageAgeDifference(List<Group> groupList) {
        List<Participant> participantList = new ArrayList<>();

        List<Pair> pairList = new ArrayList<>();
        // Extract all participants from the groups
        for (Group group : groupList) {
            pairList.add(group.pair1);
            pairList.add(group.pair2);
            pairList.add(group.pair3);
        }
        double sumOfAgeRanges = 0;
        for (Pair pair : pairList) {
            sumOfAgeRanges+= pair.getAgeRangeDifference();
        }
        return sumOfAgeRanges / (double) groupList.size();
    }

    public static double calculateFoodPreferenceDeviation(List<Group> groupList) {
        if (groupList.isEmpty()) return 0;
        double totalFoodPreferenceDifference = 0;
        for (Group group : groupList) {
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(group.pair1.participant1, group.pair1.participant2);
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(group.pair2.participant1, group.pair2.participant2);
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(group.pair3.participant1, group.pair3.participant2);
        }
        return totalFoodPreferenceDifference / (double) (groupList.size()*3); // *3 because 3 pairs in each group
    }

}
