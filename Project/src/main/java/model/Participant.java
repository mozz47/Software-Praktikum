package model;

/**
 * Represents a participant in a group, with various attributes such as name, age, food preference, etc.
 */
public class Participant {
    public int story;
    public String id;
    public String name;
    public FoodPreference foodPreference;
    public int age;
    public Sex sex;
    public boolean hasKitchen;
    public boolean mightHaveKitchen;
    public Kitchen kitchen;  // geographical location
    public Participant partner;  // pair-partner

    /**
     * main constructor of Participant
     *
     * @param id               presumably unique hash-code for every participant (maybe we should replace this with a simple int)
     * @param name             full name of the participant
     * @param foodPreference   enum
     * @param age              int age
     * @param sex              enum
     * @param hasKitchen       boolean
     * @param mightHaveKitchen boolean, false if hasKitchen is already true
     * @param kitchen          Kitchen instance with longitude, latitude and story
     * @param story            int (redundant?)
     * @param partner          Participant instance of partner
     */
    public Participant(String id, String name, FoodPreference foodPreference, int age, Sex sex, boolean hasKitchen, boolean mightHaveKitchen, Kitchen kitchen, int story, Participant partner) {
        this.id = id;
        this.name = name;
        this.foodPreference = foodPreference;
        this.age = age;
        this.sex = sex;
        this.hasKitchen = hasKitchen;
        this.mightHaveKitchen = mightHaveKitchen;
        this.kitchen = kitchen;
        this.partner = partner;
        this.story = story;
    }

    public int getAgeRange() {
        if (age <= 17) {
            return 0;
        } else if (age <= 23) {
            return 1;
        } else if (age <= 27) {
            return 2;
        } else if (age <= 30) {
            return 3;
        } else if (age <= 35) {
            return 4;
        } else if (age <= 41) {
            return 5;
        } else if (age <= 46) {
            return 6;
        } else if (age <= 56) {
            return 7;
        } else {
            return 8;
        }
    }

    /**
     * parameterless constructor mainly for testing
     */
    public Participant() {
    }

    /**
     * returns human-readable string version of the participant
     */
    @Override
    public String toString() {
        if (partner == null) {
            return "Name: '" + name + "'\n" +
                    "Food Preference: " + foodPreference + "\n" +
                    "Age: " + age + "\n" +
                    "Sex: " + sex + "\n" +
                    getHasKitchenString() +
                    "Partner: -\n" +
                    "Story: " + story + "\n";
        } else {
            return "Name: '" + name + "'\n" +
                    "Food Preference: " + foodPreference + "\n" +
                    "Age: " + age + "\n" +
                    "Sex: " + sex + "\n" +
                    getHasKitchenString() +
                    "Partner: " + partner.name + "\n" +
                    "Story: " + story + "\n";
        }
    }

    private String getHasKitchenString() {
        String kitchenString = "Kitchen:";
        if (!hasKitchen && !mightHaveKitchen) {
            kitchenString += " -\n";
        }
        else {
            kitchenString += " " + kitchen + "\n";
        }
        String hasKitchenString = hasKitchen ? "Yes" : (mightHaveKitchen ? "Maybe" : "No");

        return "Kitchen available: " + hasKitchenString + "\n" + kitchenString;
    }

    public String getShortRepresentation() {
        return name;
        /* old version
        if (participant.partner == null) {
            return participant.name + "(" +
                    "foodPreference=" + participant.foodPreference +
                    ", age=" + participant.age +
                    ", sex=" + participant.sex +
                    ", hasKitchen=" + participant.hasKitchen +
                    ", mightHaveKitchen=" + participant.mightHaveKitchen +
                    ", kitchen=" + participant.kitchen +
                    ", partner=null" +
                    ", story=" + participant.story +
                    ')';
        } else {
            return participant.name + "(" +
                    "foodPreference=" + participant.foodPreference +
                    ", age=" + participant.age +
                    ", sex=" + participant.sex +
                    ", hasKitchen=" + participant.hasKitchen +
                    ", mightHaveKitchen=" + participant.mightHaveKitchen +
                    ", kitchen=" + participant.kitchen +
                    ", partner=" + participant.name + //or else stackoverflow-error because of recursive call to each other
                    ", story=" + participant.story +
                    ')';
        }

         */
    }

    public String getId() {
        return this.id;
    }
}
