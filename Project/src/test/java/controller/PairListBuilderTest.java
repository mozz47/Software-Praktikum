package controller;

//TODO change according to: https://www.baeldung.com/java-mockito-singleton

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PairListBuilderTest {

    private List<Participant> participants;
    private List<Criterion> criteria;

    @BeforeEach
    public void setUp() {
        participants = loadTestParticipants();
        criteria = new ArrayList<>();

        criteria.add(Criterion.Criterion_06_Food_Preference);
        criteria.add(Criterion.Criterion_07_Age_Difference);
        criteria.add(Criterion.Criterion_08_Sex_Diversity);
        criteria.add(Criterion.Criterion_09_Path_Length);
        criteria.add(Criterion.Criterion_10_Group_Amount);

        // Set the party location for the event
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(8.674617, 50.590932);
    }

    @Test
    public void testPairingByFoodPreference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            if (pair.participant1.foodPreference != pair.participant2.foodPreference) {
                assertTrue(pair.getMainFoodPreference() == FoodPreference.VEGAN || pair.getMainFoodPreference() == FoodPreference.VEGGIE);
            }
        }
    }

    @Test
    public void testPairingByAgeDifference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            assertTrue(Math.abs(pair.participant1.age - pair.participant2.age) <= 5);
        }
    }

    @Test
    public void testPairingByKitchenAvailability() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            assertTrue(pair.participant1.hasKitchen || pair.participant2.hasKitchen);
        }
    }

    @Test
    public void testPairingByDistance() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            assertNotEquals(pair.participant1.kitchen.longitude, pair.participant2.kitchen.longitude);
            assertNotEquals(pair.participant1.kitchen.latitude, pair.participant2.kitchen.latitude);
        }
    }

    @Test
    public void testAllPairsAreValid() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            assertTrue(pair.isValid());
        }
    }

    @Test
    public void testInvalidPairingByFoodPreference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            if (pair.participant1.foodPreference == FoodPreference.MEAT && pair.participant2.foodPreference == FoodPreference.VEGAN) {
                assertFalse(pair.getMainFoodPreference() == FoodPreference.MEAT);
            }
        }
    }

    @Test
    public void testInvalidPairingByAgeDifference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            if (Math.abs(pair.participant1.age - pair.participant2.age) > 5) {
                assertFalse(pair.isValid());
            }
        }
    }

    @Test
    public void testInvalidPairingByKitchenAvailability() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            if (!pair.participant1.hasKitchen && !pair.participant2.hasKitchen) {
                assertFalse(pair.isValid());
            }
        }
    }

    @Test
    public void testInvalidPairingByDistance() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;

        PairPairingConstraints constraints = new PairPairingConstraints(criteria);
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints);

        for (Pair pair : pairs) {
            if (pair.participant1.kitchen.longitude == pair.participant2.kitchen.longitude &&
                    pair.participant1.kitchen.latitude == pair.participant2.kitchen.latitude) {
                assertFalse(pair.isValid());
            }
        }
    }

    private List<Participant> loadTestParticipants() {
        List<Participant> participants = new ArrayList<>();

        participants.add(new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", FoodPreference.VEGGIE, 21, Sex.MALE, false, true, new Kitchen(3, 8.673368, 50.594128), 3, null));
        participants.add(new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", FoodPreference.NONE, 26, Sex.MALE, true, false, new Kitchen(1, 8.718915, 50.590900), 1, null));
        participants.add(new Participant("01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", FoodPreference.VEGAN, 22, Sex.MALE, true, false, new Kitchen(0, 8.681372, 50.582079), 0, null));
        participants.add(new Participant("01c1372d-d120-4459-9b65-39d56d1ad430", "Person4", FoodPreference.VEGGIE, 23, Sex.MALE, true, false, new Kitchen(1, 8.683279, 50.581563), 1, null));
        participants.add(new Participant("033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", FoodPreference.MEAT, 28, Sex.MALE, true, false, new Kitchen(2, 8.681891, 50.576791), 2, null));
        participants.add(new Participant("06082fb2-4297-4cf0-8840-c246d99f9700", "Person10", FoodPreference.VEGAN, 30, Sex.MALE, true, false, new Kitchen(2, 8.719772, 50.5919253), 2, null));

        // Additional paired participants
        Participant partner1 = new Participant("117ee996-14d3-44e8-8bcb-eb2d29fddda5", "Personx1", FoodPreference.VEGAN, 25, Sex.MALE, false, false, new Kitchen(0, 8.681372, 50.582079), 0, null);
        Participant partner2 = new Participant("ab81bb52-28b6-47dc-8d54-8d7c42ceaea1", "Personx2", FoodPreference.VEGGIE, 24, Sex.FEMALE, false, false, new Kitchen(1, 8.683279, 50.581563), 1, null);

        participants.get(2).partner = partner1;
        participants.get(3).partner = partner2;

        return participants;
    }
}