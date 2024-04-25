package model;

public class Participant {
    public int id;
    public String name;
    public FoodPreference foodPreference;
    public int age;
    public Sex sex;
    public boolean hasKitchen;
    public Kitchen kitchen;  // geographical location
    public Participant partner;  // pair-partner
}
