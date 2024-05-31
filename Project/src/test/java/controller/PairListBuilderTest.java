package controller;

//TODO change according to:https://www.baeldung.com/java-mockito-singleton

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PairListBuilderTest {
    private SpinfoodEvent mockEvent;
    private List<Participant> mockParticipants;
    private List<Criterion> mockCriteria;

    @BeforeEach
    public void setUp() {
        // Create mock participants
        Kitchen kitchen1 = new Kitchen(1, 52.5200, 13.4050);
        Kitchen kitchen2 = new Kitchen(1, 48.8566, 2.3522);
        Kitchen kitchen3 = new Kitchen(1, 51.1657, 10.4515);

        Participant p1 = new Participant("1", "Alice", FoodPreference.VEGAN, 25, Sex.FEMALE, true, true, kitchen1, 1, null);
        Participant p2 = new Participant("2", "Bob", FoodPreference.MEAT, 30, Sex.MALE, true, false, kitchen2, 1, null);
        Participant p3 = new Participant("3", "Charlie", FoodPreference.NONE, 35, Sex.MALE, false, false, null, 1, null);
        Participant p4 = new Participant("4", "Diana", FoodPreference.VEGGIE, 28, Sex.FEMALE, false, true, kitchen3, 1, null);

        p1.partner = p2;  // Pair p1 and p2

        mockParticipants = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));

        // Create mock criteria
        mockCriteria = new ArrayList<>();
        mockCriteria.add(Criterion.Criterion_10_Group_Amount);
        mockCriteria.add(Criterion.Criterion_08_Sex_Diversity);
        mockCriteria.add(Criterion.Criterion_06_Food_Preference);
        mockCriteria.add(Criterion.Criterion_07_Age_Difference);
        mockCriteria.add(Criterion.Criterion_09_Path_Length);

        // Create mock event
        mockEvent = Mockito.mock(SpinfoodEvent.class);
        when(mockEvent.getParticipants()).thenReturn(mockParticipants);
        when(mockEvent.getCriteria()).thenReturn(mockCriteria);

        // Set the singleton instance to the mock
        SpinfoodEvent.setInstance(mockEvent);
    }

    @Test
    public void testGetRegisteredTogetherPairs() {
        List<Pair> pairs = PairListBuilder.getRegisteredTogetherPairs();
        assertEquals(1, pairs.size());
        assertEquals("Alice, Bob", pairs.get(0).shortString());
    }

    @Test
    public void testGetRegisteredAloneParticipants() {
        List<Participant> aloneParticipants = PairListBuilder.getRegisteredAloneParticipants();
        assertEquals(2, aloneParticipants.size());
        assertEquals("Charlie", aloneParticipants.get(0).name);
        assertEquals("Diana", aloneParticipants.get(1).name);
    }

    @Test
    public void testGetGeneratedPairs() {
        List<Pair> generatedPairs = PairListBuilder.getGeneratedPairs(mockCriteria);
        // Ensure some pairs are generated
        assertTrue(generatedPairs.size() > 0);
    }

    @Test
    public void testGetPairList() {
        PairList pairList = PairListBuilder.getPairList(mockCriteria);
        assertNotNull(pairList);
        assertEquals(1, pairList.getPairCount()); // 1 registered pair
        assertTrue(pairList.getPairList().size() > 1); // There should be generated pairs as well
    }

}
