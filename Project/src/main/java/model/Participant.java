package model;
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

    public Participant() {}

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
