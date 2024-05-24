package model;

/**
 * Represents a pair of participants.
 * A pair consists of two participants, who may or may not have registered together.
 */
public class Pair {
    public Participant participant1;
    public Participant participant2;
    private final boolean registeredAsPair;
    private static final int MAX_AGE_GAP = 10; // the maximum age difference for a valid pair in years -useless for now

    /**
     * Main constructor of Pair.
     * @param participant1 partner of participant 2
     * @param participant2 partner of participant 1
     * @param registeredAsPair whether they registered together
     */
    public Pair(Participant participant1, Participant participant2, boolean registeredAsPair) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.registeredAsPair = registeredAsPair;
    }

    /**
     * Returns age difference of the two participant in terms of age ranges.
     */
    public int getAgeDifference() {
        int ageRange1 = participant1.getAgeRange();
        int ageRange2 = participant2.getAgeRange();
        return Math.abs(ageRange1 - ageRange2);
    }

    /**
     * Returns the main food preference of a pair according to criteria 6.1.
     * @return FoodPreference main food preference of the pair
     */
    public FoodPreference getMainFoodPreference() {
        FoodPreference fp1 = participant1.foodPreference;
        FoodPreference fp2 = participant2.foodPreference;
        if (fp1 == fp2) {
            return fp1;
        }
        if ((fp1 == FoodPreference.MEAT && fp2 == FoodPreference.NONE) ||
                (fp2 == FoodPreference.MEAT && fp1 == FoodPreference.NONE)) {
            return FoodPreference.MEAT;
        }
        if (fp1 == FoodPreference.VEGAN || fp2 == FoodPreference.VEGAN) {
            return FoodPreference.VEGAN;
        }
        if (fp1 == FoodPreference.VEGGIE || fp2 == FoodPreference.VEGGIE) {
            return FoodPreference.VEGGIE;
        }
        return FoodPreference.NONE;
    }

    /**
     * Checks if the pair is valid. If a pair has registered together, it is automatically considered valid.
     */
    public boolean isValid() {
        boolean haveAKitchen = getKitchenAmount() >= 1;

        return registeredAsPair || (haveAKitchen && !sameHouse() && isValidFoodPreference());
    }

    private boolean isValidFoodPreference() {
        FoodPreference fp1 = participant1.foodPreference;
        FoodPreference fp2 = participant2.foodPreference;
        // Fleischliebhaber darf nicht mit Veganer oder Vegetarier gepaart werden
        if (fp1 == FoodPreference.MEAT && (fp2 == FoodPreference.VEGAN || fp2 == FoodPreference.VEGGIE)) {
            return false;
        } else if (fp2 == FoodPreference.MEAT && (fp1 == FoodPreference.VEGAN || fp1 == FoodPreference.VEGGIE)) {
            return false;
        }
        return true;
    }

    /**
     * !!!!ONLY USE IF AT LEAST ONE HAS KITCHEN!!!!
     * @return true if both participants have the same house
     */
    public boolean sameHouse() {
        if ((this.participant1.hasKitchen || this.participant1.mightHaveKitchen)
                && !(this.participant2.hasKitchen || this.participant2.mightHaveKitchen)) { //if 1 has/maybe has kitchen and 2 not
            return false;
        } else if (!(this.participant1.hasKitchen || this.participant1.mightHaveKitchen)
                && (this.participant2.hasKitchen || this.participant2.mightHaveKitchen)) { // if 1 has no kitchen and 2 has/maybe has
            return false;
        } else { // both have/maybe have kitchen
            return participant1.kitchen.longitude == participant2.kitchen.longitude && participant1.kitchen.latitude == participant2.kitchen.latitude;
        }
    }

    /**
     * counts number of kitchens in pair
     */
    int getKitchenAmount() {
        return (participant1.hasKitchen || participant1.mightHaveKitchen ? 1 : 0) + (participant2.hasKitchen || participant2.mightHaveKitchen ? 1 : 0);
    }

    /**
     * Returns human-readable string version of the pair.
     */
    @Override
    public String toString() {
        return "Pair{" +
                "participant1=" + participant1 +
                ", participant2=" + participant2 +
                ", registeredAsPair=" + registeredAsPair +
                '}';
    }

    /**
     * Returns a short String representation of the pair.
     */
    public String shortString() {
        return "(" + participant1.name + ", " + participant2.name + ")";
    }

}
