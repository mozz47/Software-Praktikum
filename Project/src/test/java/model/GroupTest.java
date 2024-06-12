package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupTest {
    private Pair pair1;
    private Pair pair2;
    private Pair pair3;
    private Pair pairWithKitchen;
    private Meal mealType;
    SpinfoodEvent event = SpinfoodEvent.getInstance();

    @BeforeEach
    public void setUp() {
        event.partyLocation = new Location(0,0);

        Kitchen kitchen1 = new Kitchen(1, 10.0, 10.0);
        Kitchen kitchen2 = new Kitchen(1, 20.0, 20.0);
        Kitchen kitchen3 = new Kitchen(1, 30.0, 30.0);

        Participant participant1 = new Participant("1", "Alice", FoodPreference.VEGAN, 20, Sex.FEMALE, true, false, kitchen1, 1, null);
        Participant participant2 = new Participant("2", "Bob", FoodPreference.VEGGIE, 30, Sex.MALE, true, false, kitchen2, 1, null);
        Participant participant3 = new Participant("3", "Charlie", FoodPreference.VEGAN, 35, Sex.MALE, true, false, kitchen3, 1, null);
        Participant participant4 = new Participant("4", "Diana", FoodPreference.VEGGIE, 28, Sex.FEMALE, true, false, kitchen1, 1, null);
        Participant participant5 = new Participant("5", "Eve", FoodPreference.VEGAN, 22, Sex.FEMALE, true, false, kitchen2, 1, null);
        Participant participant6 = new Participant("6", "Frank", FoodPreference.VEGGIE, 27, Sex.MALE, true, false, kitchen3, 1, null);

        pair1 = new Pair(participant1, participant2, false);
        pair2 = new Pair(participant3, participant4, false);
        pair3 = new Pair(participant5, participant6, false);
        pairWithKitchen = pair1;
        mealType = Meal.STARTER;

        Group.resetIdCounter(); // Reset ID counter before each test
    }

    @Test
    public void testConstructorAndId() {
        Group group = new Group(pair1, pair2, pair3, pairWithKitchen, mealType);
        assertEquals(1, group.id);
        assertEquals(pair1, group.pair1);
        assertEquals(pair2, group.pair2);
        assertEquals(pair3, group.pair3);
        assertEquals(pairWithKitchen, group.pairWithKitchen);
        assertEquals(mealType, group.mealType);
    }

    @Test
    public void testResetIdCounter() {
        new Group(pair1, pair2, pair3, pairWithKitchen, mealType);
        new Group(pair1, pair2, pair3, pairWithKitchen, mealType);
        Group.resetIdCounter();
        Group group = new Group(pair1, pair2, pair3, pairWithKitchen, mealType);
        assertEquals(1, group.id);
    }

    @Test
    public void testGetMainFoodPreference_AllVegan() {
        Group group = new Group(pair1, pair1, pair1, pairWithKitchen, mealType);
        assertEquals(FoodPreference.VEGAN, group.getMainFoodPreference());
    }

    @Test
    public void testGetMainFoodPreference_VeganAndVeggie() {
        Group group = new Group(pair1, pair2, pair1, pairWithKitchen, mealType);
        assertEquals(FoodPreference.VEGAN, group.getMainFoodPreference());
    }

    @Test
    public void testGetMainFoodPreference_AllVeggie() {
        Pair pairVeggie1 = new Pair(new Participant("7", "Grace", FoodPreference.VEGGIE, 31, Sex.FEMALE, true, false, new Kitchen(1, 10.0, 10.0), 1, null),
                new Participant("8", "Hank", FoodPreference.VEGGIE, 36, Sex.MALE, true, false, new Kitchen(1, 10.0, 10.0), 1, null), false);
        Pair pairVeggie2 = new Pair(new Participant("9", "Ivy", FoodPreference.VEGGIE, 23, Sex.FEMALE, true, false, new Kitchen(1, 10.0, 10.0), 1, null),
                new Participant("10", "Jack", FoodPreference.VEGGIE, 32, Sex.MALE, true, false, new Kitchen(1, 10.0, 10.0), 1, null), false);
        Pair pairVeggie3 = new Pair(new Participant("12", "Ice", FoodPreference.VEGGIE, 23, Sex.FEMALE, true, false, new Kitchen(1, 13.0, 14.0), 1, null),
                new Participant("10", "Jack", FoodPreference.VEGGIE, 32, Sex.MALE, true, false, new Kitchen(1, 10.0, 10.0), 1, null), false);

        Group group = new Group(pairVeggie1, pairVeggie2, pairVeggie3, pairVeggie1, mealType);
        assertEquals(FoodPreference.VEGGIE, group.getMainFoodPreference());
    }

    @Test
    public void testToString() {
        Group group = new Group(pair1, pair2, pair3, pairWithKitchen, mealType);
        String expectedString = "Type of Meal: STARTER\n" +
                "Pair 1: " + pair1.shortString() + "\n" +
                "Pair 2: " + pair2.shortString() + "\n" +
                "Pair 3: " + pair3.shortString() + "\n" +
                "Pair with Kitchen: " + pairWithKitchen.shortString() + "\n" +
                "Food Preference: VEGAN\n";
        assertEquals(expectedString, group.toString());
    }

    @Test
    public void testShortString() {
        Group group = new Group(pair1, pair2, pair3, pairWithKitchen, mealType);
        assertEquals("Group 1", group.shortString());
    }
}
