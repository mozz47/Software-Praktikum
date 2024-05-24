package model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairPairingConstraints {

    public PairPairingConstraints(List<Criterion> criterions) {

        ageGap = 0;
        relaxedFoodPreferences = false;
        relaxedGenderDiversity = false;
        relaxedMinimizeSuccessors = false;
        relaxedKitchenAmountOne = false; // extra constraint, always try to fulfill, should be relaxed last
        currentCriterionIndex = 0;
        this.criterions = new ArrayList<>(criterions);
        Collections.reverse(this.criterions);
        //We have to reverse the list because the least important criterion stands on the bottom so we change it first.
    }

    private List<Criterion> criterions; // <>
    private int currentCriterionIndex;
    private static final int MAXAGEGAP = 9;
    private int ageGap;
    private boolean relaxedFoodPreferences;
    private boolean relaxedGenderDiversity;
    private boolean relaxedMinimizeSuccessors;
    private boolean relaxedKitchenAmountOne; //extra constraint, always try to find pairs so that they have max 1 kitchen

    public void relaxConstraints() {
        if (currentCriterionIndex == criterions.size()) {
            relaxedKitchenAmountOne = true; //gets relaxed last in each case
        }
        if (currentCriterionIndex < criterions.size()) {
            Criterion criterion = criterions.get(currentCriterionIndex);
            switch (criterion) {
                case Criterion_06_Food_Preference:
                    relaxedFoodPreferences = true;
                    break;
                case Criterion_07_Age_Difference:
                    if (ageGap == MAXAGEGAP) {
                        currentCriterionIndex++;
                        return;
                    } else {
                        ageGap++;
                    }
                    break;
                case Criterion_08_Sex_Diversity:
                    relaxedGenderDiversity = true;
                    break;
                case Criterion_09_Path_Length: //not used with Pairs
                    break;
                case Criterion_10_Group_Amount:
                    relaxedMinimizeSuccessors = true;
                    //TODO set everything relaxed because we try to find pairs no matter what or
                    //TODO maybe ? set for example 10% 15% 20% succsessor rate until exit loop
                    break;
            }
            if (!(criterion == Criterion.Criterion_07_Age_Difference)) { //because we increase ageGap several times
                currentCriterionIndex++;
            }
        }
    }

    public boolean areConstraintsFullyRelaxed() {
        return ageGap > 8 && relaxedFoodPreferences && relaxedGenderDiversity && relaxedMinimizeSuccessors && relaxedKitchenAmountOne;
    }

    public boolean isValid(Participant p1, Participant p2) {
        Pair testPair = new Pair(p1, p2, false);

        if (!relaxedFoodPreferences) { //if relaxedFoodPreferences true -> dont check food preferences
            // isValid in PairController checks for hard constraints, so we only check for soft constraints
            int absoluteFoodDistance = getAbsoluteFoodDistance(p1, p2);
            if (absoluteFoodDistance >= 1) {
                return false;
            }
        }

        if (!relaxedGenderDiversity && p1.sex != p2.sex) { // relaxedGenderDiversity true -> dont check sex because irrelevant
            return false;
        }

        if (!relaxedKitchenAmountOne && testPair.getKitchenAmount() > 1) { // relaxedKitchenAmountOne true -> dont check kitchen amount because irrelevant
            return false;
        }

        if ((p1.getAgeRange() - p2.getAgeRange()) > ageGap) {
            return false;
        }

        return true; //all soft constraints are fulfilled
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
                Criterion.Criterion_08_Sex_Diversity,
                Criterion.Criterion_09_Path_Length,
                Criterion.Criterion_10_Group_Amount,
                Criterion.Criterion_07_Age_Difference));
        System.out.println(softConstraints.criterions);
        for (int i = 0; i < 15; i++) {
            softConstraints.relaxConstraints();
        }
        System.out.println(softConstraints.areConstraintsFullyRelaxed());
    }

}
