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
     * Calculates the average age difference of the group list. (These are O(n²) operations.)
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

        double totalDifference = 0;
        int count = 0;

        // Calculate the total age difference between all pairs of participants
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int ageDifference = Math.abs(participantList.get(i).age - participantList.get(j).age);
                totalDifference += ageDifference;
                count++;
            }
        }

        // Calculate the average age difference
        return totalDifference / count;
    }

}
