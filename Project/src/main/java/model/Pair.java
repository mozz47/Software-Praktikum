package model;

/**
 * Represents a pair of participants.
 * A pair consists of two participants, who may or may not have registered together.
 */
public class Pair {
    private static final int MAX_AGE_GAP = 10; // the maximum age difference for a valid pair in years -useless for now

    private static int idCounter = 0;

    public int id;
    public Participant participant1;
    public Participant participant2;
    public final boolean registeredAsPair;
    public boolean p2sKitchenIsUsed;

    public Cluster cluster;
    public NinePairFormation ninePairFormation;

    /**
     * Main constructor of Pair.
     *
     * @param participant1     partner of participant 2
     * @param participant2     partner of participant 1
     * @param registeredAsPair whether they registered together
     */
    public Pair(Participant participant1, Participant participant2, boolean registeredAsPair) {
        idCounter++;
        this.id = idCounter;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.registeredAsPair = registeredAsPair;
        if (registeredAsPair) {
            p2sKitchenIsUsed = false;
        } else if (getKitchenAmount() == 2) {
            Coordinate p1Kitchen = new Coordinate(participant1.kitchen.longitude, participant1.kitchen.latitude);
            Coordinate p2Kitchen = new Coordinate(participant2.kitchen.longitude, participant2.kitchen.latitude);
            Coordinate partylocation = new Coordinate(SpinfoodEvent.getInstance().partyLocation.longitude, SpinfoodEvent.getInstance().partyLocation.latitude);
            if (p1Kitchen.distanceTo(partylocation) > p2Kitchen.distanceTo(partylocation)) {
                p2sKitchenIsUsed = true;
            } else {
                p2sKitchenIsUsed = false;
            }
        } else { //kitchenamount == 1
            p2sKitchenIsUsed = participant2.hasKitchen || participant2.mightHaveKitchen;
        }

    }

    /**
     * Returns age difference of the two participant in terms of age ranges.
     */
    public int getAgeRangeDifference() {
        int ageRange1 = participant1.getAgeRange();
        int ageRange2 = participant2.getAgeRange();
        return Math.abs(ageRange1 - ageRange2);
    }

    /**
     * Calculates the absolute age difference between two participants in a pair.
     *
     * @return The absolute age difference between the two participants.
     */
    public int getAbsoluteAgeDifference() {
        int age1 = participant1.age;
        int age2 = participant2.age;
        return Math.abs(age1 - age2);
    }

    /**
     * Returns the main food preference of a pair according to criteria 6.1.
     *
     * @return FoodPreference main food preference of the pair
     */
    public FoodPreference getMainFoodPreference() {
        FoodPreference fp1 = participant1.foodPreference;
        FoodPreference fp2 = participant2.foodPreference;
        if (fp1 == fp2) {
            return fp1;
        }
        if ((fp1 == FoodPreference.MEAT && fp2 == FoodPreference.NONE) || (fp2 == FoodPreference.MEAT && fp1 == FoodPreference.NONE)) {
            return FoodPreference.MEAT; //Fleischi und Egali ergibt Fleischi
        }
        if (fp1 == FoodPreference.VEGAN  && fp2 == FoodPreference.VEGGIE || fp2 == FoodPreference.VEGAN  && fp1 == FoodPreference.VEGGIE) {
            return FoodPreference.VEGAN;  //Vegan und Veggie ergibt VEGAN
        }
        if (fp1 == FoodPreference.MEAT  && fp2 == FoodPreference.VEGGIE || fp2 == FoodPreference.MEAT  && fp1 == FoodPreference.VEGGIE) {
            return FoodPreference.VEGGIE; //Fleischi UND Veggie ergibt Veggie
        }
        if (fp1 == FoodPreference.MEAT  && fp2 == FoodPreference.VEGAN || fp2 == FoodPreference.MEAT  && fp1 == FoodPreference.VEGAN) {
            return FoodPreference.VEGAN; //Fleischi und Vegan ergibt Vegan
        }
        if (fp1 == FoodPreference.NONE  && fp2 == FoodPreference.VEGGIE || fp2 == FoodPreference.NONE  && fp1 == FoodPreference.VEGGIE) {
            return FoodPreference.VEGGIE; //Egali und Veggie ergibt Veggie
        }
        if (fp1 == FoodPreference.NONE  && fp2 == FoodPreference.VEGAN || fp2 == FoodPreference.NONE  && fp1 == FoodPreference.VEGAN) {
            return FoodPreference.VEGGIE; //Egali und Vegan ergibt Vegan
        }
        return FoodPreference.VEGAN;
    }

    /**
     * Checks if the pair is valid. If a pair has registered together, it is automatically considered valid.
     */
    public boolean isValid() {
        boolean haveAKitchen = getKitchenAmount() >= 1;

        return registeredAsPair || (haveAKitchen && !sameHouse()); //removed isValidFoodPreference because specification changed!!!
    }

    /**
     * Checks if both Participant of the Pair live in the same house. If only one of them has/maybeHas kitchen, they don't
     * live in the same house. If both have/maybeHave kitchen, it is checked if they live in the same house (same Location of kitchen).
     *
     * @return true if both participants have the same house
     */
    public boolean sameHouse() {
        if ((this.participant1.hasKitchen || this.participant1.mightHaveKitchen) && !(this.participant2.hasKitchen || this.participant2.mightHaveKitchen)) { //if 1 has/maybeHaveKitchen and 2 not
            return false;
        } else if (!(this.participant1.hasKitchen || this.participant1.mightHaveKitchen) && (this.participant2.hasKitchen || this.participant2.mightHaveKitchen)) { // if 1 has no kitchen and 2 has/maybe has
            return false;
        } else { // both have/maybe have kitchen
            return (participant1.kitchen.longitude == participant2.kitchen.longitude)
                    && (participant1.kitchen.latitude == participant2.kitchen.latitude);
        }
    }

    /**
     * counts number of kitchens in pair
     */
    int getKitchenAmount() {
        return (participant1.hasKitchen || participant1.mightHaveKitchen ? 1 : 0) + (participant2.hasKitchen || participant2.mightHaveKitchen ? 1 : 0);
    }

    /**
     * Returns the Kitchen that is used in the pair.
     *
     * @return Kitchen
     */
    public Kitchen getKitchen() {
        return p2sKitchenIsUsed ? participant2.kitchen : participant1.kitchen;
    }


    /**
     * Calculates the Euclidean distance from kitchen 1 to kitchen 2 to kitchen 3 to party location.
     *
     * @return double sum of Euclidean distance
     */
    public double getPathLength() {
        Pair pairWithKitchen1 = cluster.groupA.pairWithKitchen;
        Coordinate c1 = new Coordinate(pairWithKitchen1.getKitchen().longitude, pairWithKitchen1.getKitchen().latitude);
        Pair pairWithKitchen2 = cluster.groupB.pairWithKitchen;
        Coordinate c2 = new Coordinate(pairWithKitchen2.getKitchen().longitude, pairWithKitchen2.getKitchen().latitude);
        Pair pairWithKitchen3 = cluster.groupC.pairWithKitchen;
        Coordinate c3 = new Coordinate(pairWithKitchen3.getKitchen().longitude, pairWithKitchen3.getKitchen().latitude);
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        Location partyLocation = event.partyLocation;
        Coordinate c4 = new Coordinate(partyLocation.longitude, partyLocation.latitude);

        return c1.distanceTo(c2) + c2.distanceTo(c3) + c3.distanceTo(c4) + c4.distanceTo(c1);
    }

    /**
     * Returns the distance between this pair and the given pair.
     *
     * @param pair other pair
     * @return double Euclidean distance
     */
    public double getDistanceToPair(Pair pair) {
        Coordinate c1 = new Coordinate(this.getKitchen().longitude, this.getKitchen().latitude);
        Coordinate c2 = new Coordinate(pair.getKitchen().longitude, pair.getKitchen().latitude);
        return c1.distanceTo(c2);
    }

    public Coordinate getCoordinates() {
        return new Coordinate(this.getKitchen().longitude, this.getKitchen().latitude);
    }

    /**
     * Calculates the distance between this pair and the given location.
     *
     * @param location the location
     * @return double Euclidean distance
     */
    public double getDistanceToLocation(Location location) {
        Coordinate c1 = new Coordinate(this.getKitchen().longitude, this.getKitchen().latitude);
        Coordinate c2 = new Coordinate(location.longitude, location.latitude);
        return c1.distanceTo(c2);
    }

    /**
     * Returns human-readable string version of the pair.
     */
    @Override
    public String toString() {
        String participantWithKitchen = p2sKitchenIsUsed ? participant2.name : participant1.name;
        String clusterString = cluster == null ? "None" : cluster.toString();
        String pathLengthString = cluster == null ? "No path set" : String.format("%.2f", (getPathLength())) + "km";
        return "Pair id: " + id + "\n" +
                "Participant 1: " + participant1.name + "\n" +
                "Participant 2: " + participant2.name + "\n" +
                "Path Length: " + pathLengthString + "\n" +
                "Kitchen used: Kitchen of " + participantWithKitchen + ":\n" + getKitchen() + "\n" +
                "Age difference: " + getAgeRangeDifference() + "\n" +
                "Different gender: " + (participant1.sex != participant2.sex) + "\n" +
                "Food Preference: " + getMainFoodPreference() + "\n" +
                "Distance to party: " + String.format("%.2f", (getDistanceToLocation(SpinfoodEvent.getInstance().partyLocation))) + "km\n" +
                "Cluster: " + clusterString;
    }

    /**
     * Returns a short String representation of the pair.
     */
    public String shortString() {
        return participant1.name + ", " + participant2.name;
    }
}
