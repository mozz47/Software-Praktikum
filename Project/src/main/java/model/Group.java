package model;

/**
 * Represents a group consisting of three pairs.
 */
public class Group {
    private static int idCounter = 0;
    public int id;
    public Pair pair1;
    public Pair pair2;
    public Pair pair3;

    /**
     * Simple constructor for Group.
     */
    public Group(Pair pair1, Pair pair2, Pair pair3) {
        idCounter++;
        this.id = idCounter;
        this.pair1 = pair1;
        this.pair2 = pair2;
        this.pair3 = pair3;
    }

    /**
     * Returns the main food preference of a group according to criteria 6.1.
     * @return FoodPreference
     */
    public FoodPreference getMainFoodPreference() {
        FoodPreference fp1 = pair1.getMainFoodPreference();
        FoodPreference fp2 = pair2.getMainFoodPreference();
        FoodPreference fp3 = pair3.getMainFoodPreference();

        FoodPreference out = FoodPreference.NONE;

        FoodPreference vegan = FoodPreference.VEGAN;
        FoodPreference veggie = FoodPreference.VEGGIE;

        // if all have the same food preference, the group also has it
        if (fp1 == fp2 && fp2 == fp3) {
            out = fp1;
        }
        // if a single person is vegan, the group should be vegan
        else if (fp1 == vegan || fp2 == vegan || fp3 == vegan) {
            out = vegan;
        }
        // if a single person is veggie, the group should be veggie
        else if (fp1 == veggie || fp2 == veggie || fp3 == veggie) {
            out = veggie;
        }
        // if the group has no food preference, it should be meat
        return out == FoodPreference.NONE ? FoodPreference.MEAT : out;
    }
}
