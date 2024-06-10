package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairPairingConstraints {

    private List<Criterion> criterions;
    private int currentCriterionIndex;
    private static final int MAXAGEGAP = 9;
    private int ageGap;
    private boolean relaxedMinimizeSuccessors;
    private boolean relaxedFoodPreferences;
    private boolean relaxedGenderDiversity;
    private boolean relaxedKitchenAmountOne;
    private float successorsAllowedRate;

    public PairPairingConstraints(List<Criterion> criterions) {
        ageGap = 0;
        relaxedFoodPreferences = false;
        relaxedGenderDiversity = false;
        relaxedKitchenAmountOne = false;
        currentCriterionIndex = 0;
        relaxedMinimizeSuccessors = false;
        this.criterions = new ArrayList<>(criterions);
        Collections.reverse(this.criterions);  // Reverse the list to prioritize relaxing the least important criterion first
        successorsAllowedRate = getAllowedSuccessorRate();
    }

    private float getAllowedSuccessorRate() {
        // If GroupAmount is top priority, only allow 5% succ. rate, for each next position add 5%
        return 0.05f * (criterions.size() - criterions.indexOf(Criterion.Criterion_10_Group_Amount));
    }

    public void relaxConstraints() {
        System.out.println("Relaxing constraints at index: " + currentCriterionIndex);
        if (currentCriterionIndex == criterions.size()) {
            relaxedKitchenAmountOne = true;
            System.out.println("All constraints fully relaxed.");
        }
        if (currentCriterionIndex < criterions.size()) {
            Criterion criterion = criterions.get(currentCriterionIndex);
            switch (criterion) {
                case Criterion_06_Food_Preference:
                    relaxedFoodPreferences = true;
                    System.out.println("Relaxed Food Preferences.");
                    break;
                case Criterion_07_Age_Difference:
                    if (ageGap == MAXAGEGAP) {
                        currentCriterionIndex++;
                        return;
                    } else {
                        ageGap++;
                        System.out.println("Relaxed Age Gap to: " + ageGap);
                    }
                    break;
                case Criterion_08_Sex_Diversity:
                    relaxedGenderDiversity = true;
                    System.out.println("Relaxed Gender Diversity.");
                    break;
                case Criterion_09_Path_Length:
                    break;
                case Criterion_10_Group_Amount:
                    successorsAllowedRate += 0.15f;
                    relaxedMinimizeSuccessors = true;
                    System.out.println("Relaxed Group Amount.");
                    break;
            }
            if (!(criterion == Criterion.Criterion_07_Age_Difference)) {
                currentCriterionIndex++;
            }
        }
    }

    public boolean areConstraintsFullyRelaxed() {
        return ageGap > 8 && relaxedFoodPreferences && relaxedGenderDiversity && relaxedMinimizeSuccessors && relaxedKitchenAmountOne;
    }

    public boolean isValid(Participant p1, Participant p2) {
        Pair testPair = new Pair(p1, p2, false);

        if (!relaxedFoodPreferences) {
            int absoluteFoodDistance = getAbsoluteFoodDistance(p1, p2);
            if (absoluteFoodDistance >= 2) {
                System.out.println("Invalid Pair due to Food Preferences: " + p1.name + ", " + p2.name);
                return false;
            }
        }

        if (!relaxedGenderDiversity && p1.sex == p2.sex) {
            System.out.println("Invalid Pair due to Gender Diversity: " + p1.name + ", " + p2.name);
            return false;
        }

        if (!relaxedKitchenAmountOne && testPair.getKitchenAmount() > 1) {
            System.out.println("Invalid Pair due to Kitchen Amount: " + p1.name + ", " + p2.name);
            return false;
        }

        if ((p1.getAgeRange() - p2.getAgeRange()) > ageGap) {
            System.out.println("Invalid Pair due to Age Gap: " + p1.name + ", " + p2.name);
            return false;
        }

        if (!kitchenAvailable(p1, p2)) {
            System.out.println("Invalid Pair due to No Available Kitchen: " + p1.name + ", " + p2.name);
            return false;
        }

        return true;
    }

    private boolean kitchenAvailable(Participant p1, Participant p2) {
        return p1.hasKitchen || p1.mightHaveKitchen || p2.hasKitchen || p2.mightHaveKitchen;
    }

    public static int getAbsoluteFoodDistance(Participant p1, Participant p2) {
        int p1Distance = switch (p1.foodPreference) {
            case VEGAN -> 0;
            case VEGGIE -> 1;
            case MEAT, NONE -> 2;
        };
        int p2Distance = switch (p2.foodPreference) {
            case VEGAN -> 0;
            case VEGGIE -> 1;
            case MEAT, NONE -> 2;
        };
        return Math.abs(p1Distance - p2Distance);
    }

    public static void main(String[] args) {
        PairPairingConstraints softConstraints = new PairPairingConstraints(List.of(
                Criterion.Criterion_06_Food_Preference,
                Criterion.Criterion_10_Group_Amount,
                Criterion.Criterion_08_Sex_Diversity,
                Criterion.Criterion_09_Path_Length,
                Criterion.Criterion_07_Age_Difference));
        System.out.println(softConstraints.criterions);
        for (int i = 0; i < 15; i++) {
            softConstraints.relaxConstraints();
        }
        System.out.println(softConstraints.areConstraintsFullyRelaxed() + ": are fully relaxed");
    }

    public float getSuccessorAllowedRate() {
        return successorsAllowedRate;
    }
}
