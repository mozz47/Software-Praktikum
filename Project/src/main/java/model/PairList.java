package model;

import java.util.List;

public class PairList {
    private List<Pair> pairList;
    private List<Participant> successorList;
    private double averageAgeDifference; // ideally 0
    private double genderRatio;
    private int pairCount;
    private int successorCount;
    private int averageFoodpreferenceDifference;

    public PairList(List<Pair> pairList, List<Participant> successorList) {
        this.pairList = pairList;
        this.successorList = successorList;
        this.pairCount = pairList.size();
        this.successorCount = successorList.size();
        this.averageAgeDifference = calculateAverageAgeDifference();
        this.genderRatio = calculateGenderRatio();
        this.averageFoodpreferenceDifference = calculateFoodPreferenceDifference();
    }

    private double calculateAverageAgeDifference() {
        if (pairList.isEmpty()) return 0.0;
        int totalAgeDifference = 0;
        for (Pair pair : pairList) {
            totalAgeDifference += pair.getAgeDifference();
        }
        return (double) totalAgeDifference / pairList.size();
    }

    private double calculateGenderRatio() {
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

    private int calculateFoodPreferenceDifference() {
        if (pairList.isEmpty()) return 0;
        int totalFoodPreferenceDifference = 0;
        for (Pair pair : pairList) {
            totalFoodPreferenceDifference += PairPairingConstraints.getAbsoluteFoodDistance(pair.participant1, pair.participant2);
        }
        return totalFoodPreferenceDifference;
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

    public int getAverageFoodpreferenceDifference() {
        return averageFoodpreferenceDifference;
    }
}
