package model;

import java.util.List;

/**
 * The PairList class represents a list of pairs and successors and provides various statistics and calculations
 * related to the pairs of participants.
 */
public class PairList {
    private List<Pair> pairList;
    private List<Participant> successorList;
    private double averageAgeDifference; // ideally 0
    private double genderRatio;
    private int pairCount;
    private int successorCount;
    private double averageFoodpreferenceDifference;

    /**
     * The PairList class represents a list of Pair objects and their associated information.
     * It calculates and stores various statistics about the pairs, such as average age difference, gender ratio,
     * and average food preference difference.
     */
    public PairList(List<Pair> pairList, List<Participant> successorList) {
        this.pairList = pairList;
        this.successorList = successorList;
        this.pairCount = pairList.size();
        this.successorCount = successorList.size();
        this.averageAgeDifference = calculateAverageAgeDifference(pairList);
        this.genderRatio = calculateGenderRatio(pairList);
        this.averageFoodpreferenceDifference = calculateFoodPreferenceDifference(pairList);
    }

    /**
     * Calculates the average path length of pairs in a given list.
     *
     * @param pairList the list of Pair objects to calculate the average path length for
     * @return the average path length of pairs in the given list, or -1.0 if the list is empty
     */
    public static double averagePathLength(List<Pair> pairList) {
        if (pairList.isEmpty()) return -1.0;
        double totalPathLength = 0;
        int pairsUsedForCalculation = 0;
        for (Pair pair : pairList) {
            if (pair.cluster == null) {
                continue;
            }
            totalPathLength += pair.getPathLength();
            pairsUsedForCalculation++;
        }
        return totalPathLength / pairsUsedForCalculation;
    }

    /**
     * Calculates the average age difference between participants in a given list of Pair objects.
     *
     * @param pairList the list of Pair objects containing the participants
     * @return the average age difference between participants, or 0.0 if the list is empty
     */
    public static double calculateAverageAgeDifference(List<Pair> pairList) {
        if (pairList.isEmpty()) return 0.0;
        int totalAgeDifference = 0;
        for (Pair pair : pairList) {
            totalAgeDifference += pair.getAbsoluteAgeDifference();
        }
        return (double) totalAgeDifference / pairList.size();
    }

    /**
     * Calculates the gender ratio of participants in a list of Pair objects.
     *
     * @param pairList the list of Pair objects containing the participants
     * @return the gender ratio, which is the ratio of the number of males to females,
     *         or 0.0 if the list is empty
     */
    public static double calculateGenderRatio(List<Pair> pairList) {
        if (pairList.isEmpty()) return 0.0;
        int maleCount = 0;
        int femaleCount = 0;
        for (Pair pair : pairList) {
            if (pair.participant1.sex == Sex.MALE) maleCount++;
            if (pair.participant1.sex == Sex.FEMALE) femaleCount++;
            if (pair.participant2.sex == Sex.MALE) maleCount++;
            if (pair.participant2.sex == Sex.FEMALE) femaleCount++;
        }
        return femaleCount == 0 ? maleCount : (double) maleCount / femaleCount;
    }

    /**
     * Calculates the difference in food preferences between pairs of participants in a given list.
     *
     * @param pairList the list of Pair objects containing the participants
     * @return the average difference in food preferences between pairs of participants,
     *         or 0.0 if the list is empty
     */
    public static double calculateFoodPreferenceDifference(List<Pair> pairList) {
        if (pairList.isEmpty()) return 0;
        double totalFoodPreferenceDifference = 0;
        for (Pair pair : pairList) {
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(pair.participant1, pair.participant2);
        }
        return totalFoodPreferenceDifference / (double) pairList.size();
    }


    public List<Pair> getPairList() {
        return pairList;
    }

    public List<Participant> getSuccessorList() {
        return successorList;
    }

    public double getAverageAgeDifference() {
        return averageAgeDifference;
    }

    public double getGenderRatio() {
        return genderRatio;
    }

    public int getPairCount() {
        return pairCount;
    }

    public int getSuccessorCount() {
        return successorCount;
    }

    public double getAverageFoodpreferenceDifference() {
        return averageFoodpreferenceDifference;
    }
}
