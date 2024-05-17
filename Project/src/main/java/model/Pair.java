package model;

/**
 * Represents a pair of participants.
 * A pair consists of two participants, who may or may not have registered together.
 */
public class Pair {
    public Participant participant1;
    public Participant participant2;
    private final boolean registeredAsPair;
    private static final int MAX_AGE_GAP = 10; // the maximum age difference for a valid pair in years

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
     * Returns age difference of the two participant in absolute terms.
     */
    public int getAgeDifference() {
        return Math.abs(participant1.age - participant2.age);
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
        boolean haveSimilarAge = MAX_AGE_GAP <= getAgeDifference();
        boolean haveAKitchen = getKitchenAmount() >= 1;
        // todo check if live in same house
        return registeredAsPair || (haveSimilarAge && haveAKitchen);
    }

    /**
     * counts number of kitchens in pair
     */
    private int getKitchenAmount() {
        return (participant1.hasKitchen ? 1 : 0) + (participant2.hasKitchen ? 1 : 0);
    }

    /**
     * Returns human-readable string version of the pair.
     */
    @Override
    public String toString() {
        return "Pair{" +
                "participant1=" + participant1 +
                ", participant2=" + participant2 +
                '}';
    }

    /**
     * Returns a short String representation of the pair.
     */
    public String shortName() {
        return "(" + participant1.name + ", " + participant2.name + ")";
    }
}
