package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {
    private Participant participant1;
    private Participant participant2;
    private Participant participant3;
    private Participant participant4;
    private Participant participant5;
    private Participant participant6;
    private Kitchen kitchen1;
    private Kitchen kitchen2;
    private Kitchen kitchen3;
    private Kitchen kitchen4;

    @BeforeEach
    public void setUp() {
        kitchen1 = new Kitchen(1, 10.0, 10.0);
        kitchen2 = new Kitchen(1, 20.0, 20.0);
        kitchen3 = new Kitchen(1, 30.0, 30.0);
        kitchen4 = new Kitchen(1, 40.0, 40.0);

        participant1 = new Participant("1", "Alice", FoodPreference.VEGAN, 25, Sex.FEMALE, true, true, kitchen1, 1, null);
        participant2 = new Participant("2", "Bob", FoodPreference.VEGGIE, 26, Sex.MALE, true, false, kitchen2, 1, null);
        participant3 = new Participant("3", "Charlie", FoodPreference.MEAT, 30, Sex.MALE, false, true, kitchen3, 1, null);
        participant4 = new Participant("4", "Diana", FoodPreference.NONE, 22, Sex.FEMALE, true, false, kitchen4, 1, null);
        participant5 = new Participant("5", "Eve", FoodPreference.VEGAN, 27, Sex.FEMALE, false, true, kitchen1, 1, null);
        participant6 = new Participant("6", "Frank", FoodPreference.VEGGIE, 26, Sex.MALE, true, false, kitchen2, 1, null);

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(50.590932, 8.674617);
    }

    @Test
    public void testConstructorAndGetters() {
        Pair pair = new Pair(participant1, participant2, true);
        assertNotNull(pair);
        assertEquals(participant1, pair.participant1);
        assertEquals(participant2, pair.participant2);
        assertTrue(pair.registeredAsPair);
    }

    @Test
    public void testGetAgeRangeDifference() {
        Pair pair = new Pair(participant1, participant2, true);
        assertEquals(0, pair.getAgeRangeDifference());
    }

    @Test
    public void testGetAbsoluteAgeDifference() {
        Pair pair = new Pair(participant1, participant4, true);
        assertEquals(3, pair.getAbsoluteAgeDifference());
    }

    @Test
    public void testGetMainFoodPreference() {
        Pair pair = new Pair(participant1, participant2, true);
        assertEquals(FoodPreference.VEGAN, pair.getMainFoodPreference());

        Pair pair2 = new Pair(participant3, participant4, true);
        assertEquals(FoodPreference.MEAT, pair2.getMainFoodPreference());
    }

    @Test
    public void testIsValid() {
        Pair pair = new Pair(participant1, participant2, true);
        assertTrue(pair.isValid());

        Pair pair2 = new Pair(participant1, participant3, false);
        assertTrue(pair2.isValid());

        Participant noKitchenParticipant = new Participant("7", "Grace", FoodPreference.VEGAN, 30, Sex.FEMALE, false, false, null, 1, null);
        Participant noKitchenParticipant2 = new Participant("7", "Alex", FoodPreference.VEGAN, 30, Sex.MALE, false, false, null, 1, null);
        Pair invalidPair = new Pair(noKitchenParticipant2, noKitchenParticipant, false);
        assertFalse(invalidPair.isValid());
    }

    @Test
    public void testGetKitchen() {
        Pair pair = new Pair(participant1, participant2, true);
        assertEquals(kitchen1, pair.getKitchen());

        Pair pair2 = new Pair(participant3, participant4, false);
        assertEquals(kitchen3, pair2.getKitchen());
    }

    @Test
    public void testGetDistanceToLocation() {
        Pair pair = new Pair(participant1, participant2, true);
        pair.p2sKitchenIsUsed = false;
        Location loc = new Location(50.590932, 8.674617);
        double expectedDistance = new Coordinate(10.0, 10.0).distanceTo(new Coordinate(50.590932, 8.674617));
        assertEquals(expectedDistance, pair.getDistanceToLocation(loc));
    }

    @Test
    public void testSameHouseBothHaveKitchen() {
        Kitchen kitchen1 = new Kitchen(1, 10.0, 20.0);
        Kitchen kitchen2 = new Kitchen(1, 10.0, 20.0);
        Participant participant1 = new Participant("1", "Alice", FoodPreference.NONE, 25, Sex.FEMALE, true, false, kitchen1, 1, null);
        Participant participant2 = new Participant("2", "Bob", FoodPreference.NONE, 28, Sex.MALE, true, false, kitchen2, 1, null);

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(8.674617, 50.590932);

        Pair pair = new Pair(participant1, participant2, false);
        assertTrue(pair.sameHouse());
    }

    @Test
    public void testSameHouseOneHasKitchenOtherDoesNot() {
        Kitchen kitchen1 = new Kitchen(1, 10.0, 20.0);
        Participant participant1 = new Participant("1", "Alice", FoodPreference.NONE, 25, Sex.FEMALE, true, false, kitchen1, 1, null);
        Participant participant2 = new Participant("2", "Bob", FoodPreference.NONE, 28, Sex.MALE, false, false, null, 1, null);

        Pair pair = new Pair(participant1, participant2, false);
        assertFalse(pair.sameHouse());
    }

    @Test
    public void testSameHouseBothMaybeHaveKitchen() {
        Kitchen kitchen1 = new Kitchen(1, 10.0, 20.0);
        Kitchen kitchen2 = new Kitchen(1, 10.0, 20.0);

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(8.674617, 50.590932);

        Participant participant1 = new Participant("1", "Alice", FoodPreference.NONE, 25, Sex.FEMALE, false, true, kitchen1, 1, null);
        Participant participant2 = new Participant("2", "Bob", FoodPreference.NONE, 28, Sex.MALE, false, true, kitchen2, 1, null);
        Pair pair = new Pair(participant1, participant2, false);
        assertTrue(pair.sameHouse());
    }

    @Test
    public void testSameHouseDifferentKitchens() {
        Kitchen kitchen1 = new Kitchen(1, 10.0, 20.0);
        Kitchen kitchen2 = new Kitchen(1, 30.0, 40.0);
        Participant participant1 = new Participant("1", "Alice", FoodPreference.NONE, 25, Sex.FEMALE, true, false, kitchen1, 1, null);
        Participant participant2 = new Participant("2", "Bob", FoodPreference.NONE, 28, Sex.MALE, true, false, kitchen2, 1, null);

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(8.674617, 50.590932);

        Pair pair = new Pair(participant1, participant2, false);
        assertFalse(pair.sameHouse());
    }

    @Test
    public void testSameHouseOneMaybeKitchenOtherHasKitchen() {
        Kitchen kitchen1 = new Kitchen(1, 10.0, 20.0);
        Kitchen kitchen2 = new Kitchen(1, 10.0, 20.0);

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(8.674617, 50.590932);

        Participant participant1 = new Participant("1", "Alice", FoodPreference.NONE, 25, Sex.FEMALE, false, true, kitchen1, 1, null);
        Participant participant2 = new Participant("2", "Bob", FoodPreference.NONE, 28, Sex.MALE, true, false, kitchen2, 1, null);

        Pair pair = new Pair(participant1, participant2, false);
        assertTrue(pair.sameHouse());
    }
}
