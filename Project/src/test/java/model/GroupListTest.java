package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GroupListTest {
    private Participant participant1;
    private Participant participant2;
    private Participant participant3;
    private Participant participant4;
    private Participant participant5;
    private Participant participant6;
    private Participant participant7;
    private Participant participant8;
    private Participant participant9;
    private Participant participant10;
    private Participant participant11;
    private Participant participant12;
    private Group group1;
    private Group group2;
    private SpinfoodEvent event = SpinfoodEvent.getInstance();

    @BeforeEach
    public void setUp() {
        event.partyLocation = new Location(0,0);

        Kitchen kitchen1 = new Kitchen(1, 10.0, 10.0);
        Kitchen kitchen2 = new Kitchen(1, 20.0, 20.0);
        Kitchen kitchen3 = new Kitchen(1, 30.0, 30.0);
        Kitchen kitchen4 = new Kitchen(1, 40.0, 40.0);
        Kitchen kitchen5 = new Kitchen(1, 50.0, 50.0);
        Kitchen kitchen6 = new Kitchen(1, 60.0, 60.0);

        participant1 = new Participant("1", "Alice", FoodPreference.VEGAN, 20, Sex.FEMALE, true, false, kitchen1, 1, null);
        participant2 = new Participant("2", "Bob", FoodPreference.VEGGIE, 30, Sex.MALE, true, false, kitchen2, 1, null);
        participant3 = new Participant("3", "Charlie", FoodPreference.VEGAN, 35, Sex.MALE, true, false, kitchen3, 1, null);
        participant4 = new Participant("4", "Diana", FoodPreference.VEGGIE, 28, Sex.FEMALE, true, false, kitchen4, 1, null);
        participant5 = new Participant("5", "Eve", FoodPreference.VEGAN, 22, Sex.FEMALE, true, false, kitchen5, 1, null);
        participant6 = new Participant("6", "Frank", FoodPreference.VEGGIE, 27, Sex.MALE, true, false, kitchen6, 1, null);
        participant7 = new Participant("7", "Grace", FoodPreference.VEGAN, 31, Sex.FEMALE, true, false, kitchen1, 1, null);
        participant8 = new Participant("8", "Hank", FoodPreference.VEGGIE, 36, Sex.MALE, true, false, kitchen2, 1, null);
        participant9 = new Participant("9", "Ivy", FoodPreference.VEGAN, 23, Sex.FEMALE, true, false, kitchen3, 1, null);
        participant10 = new Participant("10", "Jack", FoodPreference.VEGGIE, 32, Sex.MALE, true, false, kitchen4, 1, null);
        participant11 = new Participant("11", "Kathy", FoodPreference.VEGAN, 29, Sex.FEMALE, true, false, kitchen5, 1, null);
        participant12 = new Participant("12", "Leo", FoodPreference.VEGGIE, 26, Sex.MALE, true, false, kitchen6, 1, null);

        Pair pair1 = new Pair(participant1, participant2, false);
        Pair pair2 = new Pair(participant3, participant4, false);
        Pair pair3 = new Pair(participant5, participant6, false);
        Pair pair4 = new Pair(participant7, participant8, false);
        Pair pair5 = new Pair(participant9, participant10, false);
        Pair pair6 = new Pair(participant11, participant12, false);

        group1 = new Group(pair1, pair2, pair3, pair1, Meal.STARTER);
        group2 = new Group(pair4, pair5, pair6, pair4, Meal.MAIN_COURSE);

    }

    @Test
    public void testCalculateGenderRatio() {
        List<Group> groupList = Arrays.asList(group1, group2);
        double expectedRatio = 0.5; // 6 females out of 12 participants
        assertEquals(expectedRatio, GroupList.calculateGenderRatio(groupList));
    }

    @Test
    public void testCalculateAverageAgeDifference() {
        List<Group> groupList = Arrays.asList(group1, group2);
        double expectedAverageAgeDifference = 4.5; // calculated average age difference for the provided participants
        assertEquals(expectedAverageAgeDifference, GroupList.calculateAverageAgeDifference(groupList));
    }

    @Test
    public void testCalculateFoodPreferenceDeviation() {
        List<Group> groupList = Arrays.asList(group1, group2);
        double expectedDeviation = 1.0; // distance for each pair is 1
        assertEquals(expectedDeviation, GroupList.calculateFoodPreferenceDeviation(groupList));
    }
}
