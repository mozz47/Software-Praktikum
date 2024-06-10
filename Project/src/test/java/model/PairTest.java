package model;

import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

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
