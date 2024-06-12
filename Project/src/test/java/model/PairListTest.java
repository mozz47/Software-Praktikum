package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PairListTest {

    private SpinfoodEvent event = SpinfoodEvent.getInstance();

    private Kitchen kitchen1;
    private Kitchen kitchen2;
    private Kitchen kitchen3;
    private Kitchen kitchen4;

    private Participant p1;
    private Participant p2;
    private Participant p3;
    private Participant p4;

    private Pair pair1;
    private Pair pair2;

    @BeforeEach
    public void setUp() {
        event.partyLocation = new Location(0,0);
        kitchen1 = new Kitchen(1, 10.0, 10.0);
        kitchen2 = new Kitchen(1, 20.0, 20.0);
        kitchen3 = new Kitchen(1, 30.0, 30.0);
        kitchen4 = new Kitchen(1, 40.0, 40.0);

        p1 = new Participant("1", "Alice", FoodPreference.NONE, 25, Sex.FEMALE, true, false, kitchen1, 1, null);
        p2 = new Participant("2", "Bob", FoodPreference.MEAT, 28, Sex.MALE, true, false, kitchen2, 1, null);
        p3 = new Participant("3", "Charlie", FoodPreference.MEAT, 26, Sex.MALE, true, false, kitchen3, 1, null);
        p4 = new Participant("4", "Diana", FoodPreference.NONE, 22, Sex.FEMALE, true, false, kitchen4, 1, null);

        pair1 = new Pair(p1, p2, false);
        pair2 = new Pair(p3, p4, false);


        // Mock clusters for pairs to have path lengths
        Cluster cluster1 = new Cluster(new Group(pair1, pair1, pair1, pair1, Meal.STARTER),
                new Group(pair1, pair1, pair1, pair1, Meal.MAIN_COURSE),
                new Group(pair1, pair1, pair1, pair1, Meal.DESSERT));
        Cluster cluster2 = new Cluster(new Group(pair2, pair2, pair2, pair2, Meal.STARTER),
                new Group(pair2, pair2, pair2, pair2, Meal.MAIN_COURSE),
                new Group(pair2, pair2, pair2, pair2, Meal.DESSERT));

        pair1.cluster = cluster1;
        pair2.cluster = cluster2;
    }

    @Test
    public void testAveragePathLength() {
        PairList pairList = new PairList(Arrays.asList(pair1, pair2), Collections.emptyList());
        double expected = (pair1.getPathLength() + pair2.getPathLength()) / 2;
        assertEquals(expected, PairList.averagePathLength(Arrays.asList(pair1, pair2)));
    }

    @Test
    public void testCalculateAverageAgeDifference() {
        double expected = (pair1.getAbsoluteAgeDifference() + pair2.getAbsoluteAgeDifference()) / 2.0;
        assertEquals(expected, PairList.calculateAverageAgeDifference(Arrays.asList(pair1, pair2)));
    }

    @Test
    public void testCalculateGenderRatio() {
        double expected = 1.0; // equal number of males and females in the pairs
        assertEquals(expected, PairList.calculateGenderRatio(Arrays.asList(pair1, pair2)));
    }

    @Test
    public void testCalculateFoodPreferenceDifference() {
        double expected = (PairPairingConstraints.getAbsoluteFoodDistance(p1, p2) +
                PairPairingConstraints.getAbsoluteFoodDistance(p3, p4)) / 2.0;
        assertEquals(expected, PairList.calculateFoodPreferenceDifference(Arrays.asList(pair1, pair2)));
    }

    @Test
    public void testPairListInitialization() {
        PairList pairList = new PairList(Arrays.asList(pair1, pair2), Arrays.asList(p1, p3));
        assertEquals(2, pairList.getPairCount());
        assertEquals(2, pairList.getSuccessorCount());
        assertNotNull(pairList.getPairList());
        assertNotNull(pairList.getSuccessorList());
    }

    @Test
    public void testGetters() {
        PairList pairList = new PairList(Arrays.asList(pair1, pair2), Arrays.asList(p1, p3));
        assertEquals(2, pairList.getPairCount());
        assertEquals(2, pairList.getSuccessorCount());
        assertEquals((pair1.getAbsoluteAgeDifference() + pair2.getAbsoluteAgeDifference()) / 2.0, pairList.getAverageAgeDifference());
        assertEquals(1.0, pairList.getGenderRatio());
        assertEquals((PairPairingConstraints.getAbsoluteFoodDistance(p1, p2) +
                PairPairingConstraints.getAbsoluteFoodDistance(p3, p4)) / 2.0, pairList.getAverageFoodpreferenceDifference());
    }

    @Test
    public void testAveragePathLengthEmptyList() {
        assertEquals(-1.0, PairList.averagePathLength(Collections.emptyList()));
    }

    @Test
    public void testCalculateAverageAgeDifferenceEmptyList() {
        assertEquals(0.0, PairList.calculateAverageAgeDifference(Collections.emptyList()));
    }

    @Test
    public void testCalculateGenderRatioEmptyList() {
        assertEquals(0.0, PairList.calculateGenderRatio(Collections.emptyList()));
    }

    @Test
    public void testCalculateFoodPreferenceDifferenceEmptyList() {
        assertEquals(0.0, PairList.calculateFoodPreferenceDifference(Collections.emptyList()));
    }
}
