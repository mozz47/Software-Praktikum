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
     * @param id presumably unique hash-code for every participant (maybe we should replace this with a simple int)
     * @param name full name of the participant
     * @param foodPreference enum
     * @param age int age
     * @param sex enum
     * @param hasKitchen boolean
     * @param mightHaveKitchen boolean, false if hasKitchen is already true
     * @param kitchen Kitchen instance with longitude, latitude and story
     * @param story int (redundant?)
     * @param partner Participant instance of partner
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

    /**
     * parameterless constructor mainly for testing
     */
    public Participant() {}

    /**
     * returns human-readable string version of the participant
     */
    @Override
    public String toString() {
        if (partner == null) {
            return "Participant{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", foodPreference=" + foodPreference +
                    ", age=" + age +
                    ", sex=" + sex +
                    ", hasKitchen=" + hasKitchen +
                    ", mightHaveKitchen=" + mightHaveKitchen +
                    ", kitchen=" + kitchen +
                    ", partner=null" +
                    ", story=" + story +
                    '}';
        } else {
            return "Participant{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", foodPreference=" + foodPreference +
                    ", age=" + age +
                    ", sex=" + sex +
                    ", hasKitchen=" + hasKitchen +
                    ", mightHaveKitchen=" + mightHaveKitchen +
                    ", kitchen=" + kitchen +
                    ", partner=" + partner.name + //or else stackoverflow-error because of recursive call to each other
                    ", story=" + story +
                    '}';
        }
    }
}
