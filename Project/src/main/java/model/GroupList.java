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
     * Calculates the average age difference of the group list. (The original algorithm had O(n²) operations,
     * using dynamic programming, we have O(n) operations now)
     *
     * @param groupList group list
     * @return the average age difference
     */
    public static double calculateAverageAgeDifference(List<Group> groupList) {
        List<Participant> participantList = new ArrayList<>();

        // Extract all participants from the groups
        for (Group group : groupList) {
            participantList.add(group.pair1.participant1);
            participantList.add(group.pair1.participant2);
            participantList.add(group.pair2.participant1);
            participantList.add(group.pair2.participant2);
            participantList.add(group.pair3.participant1);
            participantList.add(group.pair3.participant2);
        }

        int n = participantList.size();
        if (n < 2) {
            // If there are fewer than 2 participants, the average age difference is not defined
            return 0;
        }

        long sumAges = 0;
        long sumSquaredAges = 0;

        // Calculate the sum of ages and the sum of squared ages
        for (Participant p : participantList) {
            sumAges += p.age;
            sumSquaredAges += (long) p.age * p.age;
        }

        // Calculate the total age difference using the derived formula
        long totalDifference = (n * sumSquaredAges - sumAges * sumAges);

        // Since the totalDifference accounts for each pair twice (once as (i, j) and once as (j, i)),
        // we need to divide it by the number of pairs to get the average difference.
        return (double) totalDifference / (n * (n - 1));
    }

    public static double calculateFoodPreferenceDeviation(List<Group> groupList) {
        if (groupList.isEmpty()) return 0;
        double totalFoodPreferenceDifference = 0;
        for (Group group : groupList) {
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(group.pair1.participant1, group.pair1.participant2);
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(group.pair2.participant1, group.pair2.participant2);
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(group.pair3.participant1, group.pair3.participant2);
        }
        return totalFoodPreferenceDifference / (double) groupList.size();
    }

}
