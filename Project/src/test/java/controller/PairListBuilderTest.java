package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the PairListBuilder.
 */
public class PairListBuilderTest {

    private List<Participant> participants;
    private List<Criterion> criteria;

    /**
     * Sets up the test data and environment before each test.
     * This method initializes the list of participants and the criteria to be used for pairing.
     * It also sets the party location for the event.
     */
    @BeforeEach
    public void setUp() {
        participants = loadTestParticipants(); // Load test participants
        criteria = new ArrayList<>(); // Initialize criteria list

        // Add criteria in the order of importance
        criteria.add(Criterion.Criterion_06_Food_Preference);
        criteria.add(Criterion.Criterion_07_Age_Difference);
        criteria.add(Criterion.Criterion_08_Sex_Diversity);
        criteria.add(Criterion.Criterion_09_Path_Length);
        criteria.add(Criterion.Criterion_10_Group_Amount);

        // Set the party location for the event
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.partyLocation = new Location(8.674617, 50.590932); // Specific coordinates for the party location
    }

    /**
     * Tests pairing participants based on their food preferences.
     * Ensures that pairs with differing food preferences comply with specific rules.
     */
    @Test
    public void testPairingByFoodPreference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if pairs with differing food preferences comply with specific rules
        for (Pair pair : pairs) {
            if (pair.participant1.foodPreference != pair.participant2.foodPreference) {
                assertTrue(pair.getMainFoodPreference() == FoodPreference.VEGAN || pair.getMainFoodPreference() == FoodPreference.VEGGIE);
            }
        }
    }

    /**
     * Tests pairing participants based on their age differences.
     * Ensures that the age difference between paired participants does not exceed a specific limit.
     */
    @Test
    public void testPairingByAgeDifference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if age difference between paired participants is within the limit
        for (Pair pair : pairs) {
            assertTrue(Math.abs(pair.participant1.age - pair.participant2.age) <= 5);
        }
    }

    /**
     * Tests pairing participants based on kitchen availability.
     * Ensures that at least one participant in each pair has a kitchen available.
     */
    @Test
    public void testPairingByKitchenAvailability() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if at least one participant in each pair has a kitchen
        for (Pair pair : pairs) {
            assertTrue(pair.participant1.hasKitchen || pair.participant2.hasKitchen);
        }
    }

    /**
     * Tests pairing participants based on their geographic locations.
     * Ensures that paired participants have different kitchen locations.
     */
    @Test
    public void testPairingByDistance() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if paired participants have different kitchen locations
        for (Pair pair : pairs) {
            assertNotEquals(pair.participant1.kitchen.longitude, pair.participant2.kitchen.longitude);
            assertNotEquals(pair.participant1.kitchen.latitude, pair.participant2.kitchen.latitude);
        }
    }

    /**
     * Tests if all generated pairs are valid according to the constraints.
     * Ensures that each pair meets the validity criteria defined in the Pair class.
     */
    @Test
    public void testAllPairsAreValid() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if all pairs are valid
        for (Pair pair : pairs) {
            assertTrue(pair.isValid());
        }
    }

    /**
     * Tests invalid pairings based on food preferences.
     * Ensures that invalid pairs based on specific food preferences are not considered valid.
     */
    @Test
    public void testInvalidPairingByFoodPreference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if pairs with specific invalid food preferences are not considered valid
        for (Pair pair : pairs) {
            if (pair.participant1.foodPreference == FoodPreference.MEAT && pair.participant2.foodPreference == FoodPreference.VEGAN) {
                assertFalse(pair.getMainFoodPreference() == FoodPreference.MEAT);
            }
        }
    }

    /**
     * Tests invalid pairings based on age differences.
     * Ensures that pairs with an age difference exceeding the limit are not considered valid.
     */
    @Test
    public void testInvalidPairingByAgeDifference() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if pairs with age differences exceeding the limit are not considered valid
        for (Pair pair : pairs) {
            if (Math.abs(pair.participant1.age - pair.participant2.age) > 5) {
                assertFalse(pair.isValid());
            }
        }
    }

    /**
     * Tests invalid pairings based on kitchen availability.
     * Ensures that pairs without a kitchen are not considered valid.
     */
    @Test
    public void testInvalidPairingByKitchenAvailability() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if pairs without a kitchen are not considered valid
        for (Pair pair : pairs) {
            if (!pair.participant1.hasKitchen && !pair.participant2.hasKitchen) {
                assertFalse(pair.isValid());
            }
        }
    }

    /**
     * Tests invalid pairings based on distance.
     * Ensures that pairs with identical kitchen locations are not considered valid.
     */
    @Test
    public void testInvalidPairingByDistance() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        List<Pair> pairs = PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        // Check if pairs with identical kitchen locations are not considered valid
        for (Pair pair : pairs) {
            if (pair.participant1.kitchen.longitude == pair.participant2.kitchen.longitude &&
                    pair.participant1.kitchen.latitude == pair.participant2.kitchen.latitude) {
                assertFalse(pair.isValid());
            }
        }
    }

    /**
     * Tests retrieval of pairs that registered together.
     * Ensures that participants who registered together are correctly identified and paired.
     */
    @Test
    public void testGetRegisteredTogetherPairs() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        List<Pair> pairs = PairListBuilder.getRegisteredTogetherPairs(); // Retrieve pairs that registered together

        // Check if the correct number of pairs that registered together is retrieved
        assertEquals(2, pairs.size()); // Assuming there are 2 pairs registered together in the test data

        // Ensure each pair has a partner
        for (Pair pair : pairs) {
            assertNotNull(pair.participant1.partner);
        }
    }

    /**
     * Tests the retrieval of the PairList, which includes both registered together pairs and generated pairs.
     * Ensures that the PairList contains the expected pairs and successors.
     */
    @Test
    public void testGetPairList() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairList pairList = PairListBuilder.getPairList(criteria); // Retrieve the PairList

        // Ensure the PairList is not null and contains pairs
        assertNotNull(pairList);
        assertTrue(pairList.getPairList().size() > 0);

        // Check the successor list
        assertNotNull(pairList.getSuccessorList());
        assertEquals(pairList.getPairList().size(), pairList.getPairCount());
        assertEquals(pairList.getSuccessorList().size(), pairList.getSuccessorCount());

        // Validate metrics
        assertTrue(pairList.getAverageAgeDifference() >= 0);
        assertTrue(pairList.getGenderRatio() >= 0);
        assertTrue(pairList.getAverageFoodpreferenceDifference() >= 0);
    }

    /**
     * Tests retrieval of the list of successors.
     * Ensures that the list of successors is correctly retrieved.
     */
    @Test
    public void testGetSuccessors() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants; // Set the participants for the event

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria
        PairListBuilder.getGeneratedPairs(constraints); // Generate pairs

        List<Participant> successors = PairListBuilder.getSuccessors(); // Retrieve the list of successors

        // Ensure the list of successors is not null
        assertNotNull(successors);
    }

    /**
     * Loads test participants for the tests.
     * Creates a list of participants with predefined attributes for testing purposes.
     *
     * @return A list of test participants.
     */
    private List<Participant> loadTestParticipants() {
        List<Participant> participants = new ArrayList<>();

        // Add test participants
        participants.add(new Participant("004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", FoodPreference.VEGGIE, 21, Sex.MALE, false, true, new Kitchen(3, 8.673368, 50.594128), 3, null));
        participants.add(new Participant("01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", FoodPreference.NONE, 26, Sex.MALE, true, false, new Kitchen(1, 8.718915, 50.590900), 1, null));
        participants.add(new Participant("01be5c1f-4aa1-458d-a530-b1c109ffbb55", "Person3", FoodPreference.VEGAN, 22, Sex.MALE, true, false, new Kitchen(0, 8.681372, 50.582079), 0, null));
        participants.add(new Participant("01c1372d-d120-4459-9b65-39d56d1ad430", "Person4", FoodPreference.VEGGIE, 23, Sex.MALE, true, false, new Kitchen(1, 8.683279, 50.581563), 1, null));
        participants.add(new Participant("033d5f60-5853-4931-8b38-1d3da9910e6d", "Person5", FoodPreference.MEAT, 28, Sex.MALE, true, false, new Kitchen(2, 8.681891, 50.576791), 2, null));
        participants.add(new Participant("06082fb2-4297-4cf0-8840-c246d99f9700", "Person10", FoodPreference.VEGAN, 30, Sex.MALE, true, false, new Kitchen(2, 8.719772, 50.5919253), 2, null));

        // Additional paired participants
        Participant partner1 = new Participant("117ee996-14d3-44e8-8bcb-eb2d29fddda5", "Personx1", FoodPreference.VEGAN, 25, Sex.MALE, false, false, new Kitchen(0, 8.681372, 50.582079), 0, null);
        Participant partner2 = new Participant("ab81bb52-28b6-47dc-8d54-8d7c42ceaea1", "Personx2", FoodPreference.VEGGIE, 24, Sex.FEMALE, false, false, new Kitchen(1, 8.683279, 50.581563), 1, null);

        // Assign partners
        participants.get(2).partner = partner1;
        participants.get(3).partner = partner2;

        return participants;
    }
}
