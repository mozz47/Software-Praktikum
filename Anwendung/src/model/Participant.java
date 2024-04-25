package model;

public class Participant {
    public int id;
    public String name;
    public FoodPreference foodPreference;
    public int age;
    public Sex sex;
    public boolean hasKitchen;
    public boolean mightHaveKitchen;
    public Kitchen kitchen;  // geographical location
    public Participant partner;  // pair-partner

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", foodPreference=" + foodPreference +
                ", age=" + age +
                ", sex=" + sex +
                ", hasKitchen=" + hasKitchen +
                ", mightHaveKitchen=" + mightHaveKitchen +
                ", kitchen=" + kitchen +
                ", partner=" + partner +
                '}';
    }
}
