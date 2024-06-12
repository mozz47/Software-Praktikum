package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GroupListBuilder.
 */
public class GroupListBuilderTest {

    private List<Criterion> criteria;

    /**
     * Sets up the test data and environment before each test.
     * This method initializes the list of criteria and participants for the SpinFoodEvent.
     */
    @BeforeEach
    public void setUp() {
        List<Participant> participants = loadTestParticipants(); // Load test participants
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
        event.participants = participants; // Set participants for the event
        event.updateSuccessors(new ArrayList<>()); // Initialize successors list
    }

    /**
     * Tests the group formation process.
     * Ensures that valid groups are formed from the pairs and that the main food preference of each group matches.
     */
    @Test
    public void testGroupFormation() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();

        PairPairingConstraints constraints = new PairPairingConstraints(criteria); // Create constraints based on criteria

        // Relax constraints a few times initially for testing --DONT DO, breaks algorithm
        /*
        for (int i = 0; i < 20; i++) {
            constraints.relaxConstraints();
        }
         */

        List<Pair> pairs = PairListBuilder.getPairList(criteria).getPairList(); // Generate pairs
        event.updatePairList(pairs); // Update event's pair list
        System.out.println(pairs.size());

        assertFalse(pairs.isEmpty(), "Pairs should not be empty");

        GroupListBuilder groupListBuilder = new GroupListBuilder();
        groupListBuilder.buildGroupList(criteria); // Build groups from pairs

        List<Group> groups = event.getGroupList(); // Retrieve groups from event

        // Verify each group is valid and main food preference matches
        for (Group group : groups) {
            assertTrue(isGroupValid(group), "Group is not valid");
            assertEquals(group.getMainFoodPreference(), group.pair1.getMainFoodPreference(), "Main food preference should match");
        }
    }

    /**
     * Tests the formation of groups from a list of exactly 9 pairs.
     * Ensures that exactly 9 groups are formed and that each group has the correct structure.
     */
    @Test
    public void testListOf9PairsToGroup() {
        List<Pair> pairs = loadTestPairs(); // Load test pairs
        GroupListBuilder groupListBuilder = new GroupListBuilder();

        List<Group> groups = groupListBuilder.listOf9PairsToGroup(pairs); // Form groups from 9 pairs

        assertEquals(9, groups.size(), "There should be exactly 9 groups formed");

        // Verify the structure of the groups
        for (Group group : groups) {
            assertNotNull(group, "Group should not be null");
            assertNotNull(group.pair1, "Pair1 should not be null");
            assertNotNull(group.pair2, "Pair2 should not be null");
            assertNotNull(group.pair3, "Pair3 should not be null");
            assertNotNull(group.pairWithKitchen, "PairWithKitchen should not be null");
            assertNotNull(group.mealType, "MealType should not be null");
        }
    }

    /**
     * Checks if a group is valid.
     * A group is valid if all pairs are valid and no pairs are repeated.
     *
     * @param group the group to check
     * @return true if the group is valid, false otherwise
     */
    private boolean isGroupValid(Group group) {
        return group.pair1.isValid() && group.pair2.isValid() && group.pair3.isValid() &&
                !group.pair1.equals(group.pair2) && !group.pair1.equals(group.pair3) && !group.pair2.equals(group.pair3);
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
        participants.add(new Participant("07b7446a-9d8b-478b-b3e9-e95b992fcf50", "Person13", FoodPreference.NONE, 27, Sex.MALE, true, false, new Kitchen(2, 8.674109, 50.5764905482704), 2, null));

        // Additional paired participants
        Participant partner1 = new Participant("117ee996-14d3-44e8-8bcb-eb2d29fddda5", "Personx1", FoodPreference.VEGAN, 25, Sex.MALE, false, false, new Kitchen(0, 8.681372, 50.582079), 0, null);
        Participant partner2 = new Participant("ab81bb52-28b6-47dc-8d54-8d7c42ceaea1", "Personx2", FoodPreference.VEGGIE, 24, Sex.FEMALE, false, false, new Kitchen(1, 8.683279, 50.581563), 1, null);

        // Assign partners
        participants.get(2).partner = partner1;
        participants.get(3).partner = partner2;

        return participants;
    }

    /**
     * Loads test pairs for the tests.
     * Creates a list of exactly 9 pairs with predefined attributes for testing purposes.
     *
     * @return A list of test pairs.
     */
    private List<Pair> loadTestPairs() {
        List<Pair> pairs = new ArrayList<>();

        // Create 9 pairs
        for (int i = 1; i <= 9; i++) {
            Participant p1 = new Participant("id" + i + "a", "Person" + i + "a", FoodPreference.NONE, 20 + i, Sex.MALE, true, false, new Kitchen(i, 8.68 + i, 50.58 + i), i, null);
            Participant p2 = new Participant("id" + i + "b", "Person" + i + "b", FoodPreference.NONE, 21 + i, Sex.FEMALE, true, false, new Kitchen(i, 8.68 + i, 50.58 + i), i, null);
            pairs.add(new Pair(p1, p2, false)); // Create pair with participants
        }

        return pairs;
    }
}
