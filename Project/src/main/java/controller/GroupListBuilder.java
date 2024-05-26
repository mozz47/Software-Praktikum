package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of group builder algorithm. Takes pairs from SpinFoodEvent's pairList and sorts them into groups.
 */
public class GroupListBuilder {

    private List<Pair> toBeUsed;  // pairs from pair list that haven't been put into other lists.
    private List<List<Pair>> pairLists;
    private List<Pair> pairSuccessors;

    /**
     * Uses Chris' algorithm for building groups from the pairList in the SpinfoodEvent singleton.
     * The resulting group list can be found in said singleton under the name groupList.
     */
    public void buildGroupList() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        toBeUsed = new ArrayList<>(event.getPairList());  // clone pairList
        pairLists = new ArrayList<>();
        pairSuccessors = new ArrayList<>();

        // criterion 1: food preference (max 1 meaty in mixed group)
        boolean goodSplit = separateMeatiesAndRest();
        if (!goodSplit) {
            System.out.println("Keine brauchbare Aufteilung in Schritt 1");
            return;
        }

        // criterion 4: max 3 pairs per kitchen
        // todo: maybe it's better to do check this during the pair building algo

        // criteria 6 (strict food pref separation),8 (increase sex diversity),9 (reduce travel distance) via selected order:
        // todo

        // criterion 2: collect into groups of unique pairs
        splitIntoGroupsOf9Pairs();
        collectIntoGroups();

        // put successors from pairSuccessors into event.successors
        // todo: Absprache mit Parviz notwendig, um successors richtig zu speichern
    }

    /**
     * Splitting into pair lists of 9 pairs each (exactly) and discarding the rest to successors.
     */
    private void splitIntoGroupsOf9Pairs() {
        List<List<Pair>> newPairLists = new ArrayList<>();
        for (List<Pair> list : pairLists) {
            // skip empty lists
            if (list.isEmpty()) {
                continue;
            }
            List<List<Pair>> splitList = splitList(list, 9);
            // we only want lists of 9 pairs, so if it's not, the rest are put into successors.
            if (list.size() % 9 != 0) {
                List<Pair> overflow = splitList.remove(splitList.size() - 1);
                pairSuccessors.addAll(overflow);
            }
            newPairLists.addAll(splitList);
        }
        pairLists = newPairLists;
    }

    /**
     * Helper method to split a list into lists of length n.
     * If the input list is not an exact multiple of n, the last list in the list will be partially filled.
     * Say list three times quickly in a row. ;)
     *
     * @param list the list to split
     * @param n length of the output lists
     * @return list of lists of length n
     * @param <T> generic type of list
     */
    private static <T> List<List<T>> splitList(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += n) {
            result.add(new ArrayList<>(list.subList(i, Math.min(i + n, list.size()))));
        }
        return result;
    }

    /**
     * The idea is to only have lists of 9 pairs, so we can use the exact same pattern for each of those lists.
     * The pattern goes like this:
     * Starter groups: A*,B,C; D,E,F*; G,H*,I
     * Main course groups: A,D*,G; B*,E,H; C,F,I*
     * Dessert groups: A,E*,I; D,H,C*; B,F,G*
     * The kitchen of the pairs denoted with '*' are used for the meal.
     * This ensures that every pair uses its kitchen exactly once.
     */
    private void collectIntoGroups() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        List<Group> groupList = new ArrayList<>();
        for (List<Pair> list : pairLists) {
            groupList.addAll(listOf9PairsToGroup(list));
        }
        event.updateGroupList(groupList);  // sets group list and backs up potential old group list
    }

    /**
     * Helper method for collectIntoGroups()
     *
     * @param list list of EXACTLY 9 pairs, otherwise this WILL NOT WORK.
     * @return 9 groups
     */
    private List<Group> listOf9PairsToGroup(List<Pair> list) {
        if (list.size() != 9) {
            throw new IllegalArgumentException("Input list must be of length 9, you fool!");
        }
        List<Group> groups = new ArrayList<>(9);

        // name pairs systematically
        Pair A = list.get(0);
        Pair B = list.get(1);
        Pair C = list.get(2);
        Pair D = list.get(3);
        Pair E = list.get(4);
        Pair F = list.get(5);
        Pair G = list.get(6);
        Pair H = list.get(7);
        Pair I = list.get(8);

        // apply pattern
        Group A1 = new Group(A, B, C, A);
        Group A2 = new Group(D, E, F, F);
        Group A3 = new Group(G, H, I, H);
        Group B1 = new Group(A ,D, G, D);
        Group B2 = new Group(B, E, H, B);
        Group B3 = new Group(C, F, I, I);
        Group C1 = new Group(A, E, I, E);
        Group C2 = new Group(D, H, C, C);
        Group C3 = new Group(B, F, G, G);

        // add clusters
        A.cluster = new Cluster(A1, B1, C1);
        B.cluster = new Cluster(A1, B2, C3);
        C.cluster = new Cluster(A1, B3, C2);
        D.cluster = new Cluster(A2, B1, C3);
        E.cluster = new Cluster(A2, B2, C1);
        F.cluster = new Cluster(A2, B3, C3);
        G.cluster = new Cluster(A3, B1, C3);
        H.cluster = new Cluster(A3, B2, C2);
        I.cluster = new Cluster(A3, B3, C1);

        // add groups
        groups.add(A1);
        groups.add(A2);
        groups.add(A3);
        groups.add(B1);
        groups.add(B2);
        groups.add(B3);
        groups.add(C1);
        groups.add(C2);
        groups.add(C3);

        return groups;
    }

    /**
     * We want to split meaties and the rest (veggies and vegans), but not that clear-cut.
     * It is important to have 9+ pairs per list, otherwise we can't split.
     * Also, it is not a problem to have a single meaty in a rest-group.
     * For now, we handle egalies like meaties
     *
     * @return false only if there was no easy match for a 9+ pair group list.
     */
    private boolean separateMeatiesAndRest() {
        List<Pair> meaties = new ArrayList<>();
        List<Pair> rest = new ArrayList<>();
        int meatyCount = 0;
        int restCount = 0;
        boolean out = true;
        for (Pair p : toBeUsed) {
            if (p.getMainFoodPreference() == FoodPreference.MEAT
                    || p.getMainFoodPreference() == FoodPreference.NONE) {
                meatyCount++;
            }
            else {
                restCount++;
            }
        }
        if (meatyCount < 9 && restCount < 9) {
            if (meatyCount > 1) {
                // we could throw meaties into successors to still make it work here
                // but for now we just abort
                out = false;
            }
            else {
                rest = new ArrayList<>(toBeUsed);  // clone list
                toBeUsed = new ArrayList<>();  // empty
            }
        }
        else if (meatyCount < 9) {
            // throw all meaties into successors (we could keep one pair here)
            // but for now we discard all meaties
            for (Pair p : toBeUsed) {
                if (p.getMainFoodPreference() == FoodPreference.MEAT
                        || p.getMainFoodPreference() == FoodPreference.NONE) {
                    pairSuccessors.add(p);
                }
                else {
                    rest.add(p);
                }
            }
        }
        else if (restCount < 9) {
            // throw all veggies and vegans into successors
            // it is a bit complicated to keep them in this case
            for (Pair p : toBeUsed) {
                if (p.getMainFoodPreference() != FoodPreference.MEAT
                        || p.getMainFoodPreference() == FoodPreference.NONE) {
                    pairSuccessors.add(p);
                }
                else {
                    meaties.add(p);
                }
            }
        }
        else {
            // here we have more than 9 of each group, so we split them
            // for each meaty that is over 9*n we could include him into a rest group
            // but for now we split them totally
            for (Pair p : toBeUsed) {
                if (p.getMainFoodPreference() == FoodPreference.MEAT
                        || p.getMainFoodPreference() == FoodPreference.NONE) {
                    meaties.add(p);
                } else {
                    rest.add(p);
                }
            }
        }
        pairLists.add(meaties);
        pairLists.add(rest);
        return out;
    }

    /**
     * Main method for testing the group list builder
     */
    public static void main(String[] args) {
        // setup
        Main.initializeWithoutFileChooser(); // load test event
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        List<Criterion> criteria = new ArrayList<>();
        criteria.add(Criterion.Criterion_06_Food_Preference);
        criteria.add(Criterion.Criterion_07_Age_Difference);
        criteria.add(Criterion.Criterion_08_Sex_Diversity);
        criteria.add(Criterion.Criterion_10_Group_Amount);
        criteria.add(Criterion.Criterion_09_Path_Length);
        PairList allPairs = PairListBuilder.getPairList(criteria);
        event.updatePairList(allPairs.getPairList());

        // test of group list builder
        GroupListBuilder glb = new GroupListBuilder();
        glb.buildGroupList();
        System.out.println("Amount of created groups: " + event.getGroupList().size());
        System.out.println("Amount of successors: " + glb.pairSuccessors.size() * 2);

        System.out.println("Example group:");
        if (event.getGroupList().isEmpty()) {
            System.out.println("nicht vorhanden");
        }
        else {
            Group group = event.getGroupList().get(0);
            System.out.println("Pair1:");
            System.out.println(group.pair1.participant1);
            System.out.println(group.pair1.participant2);
            System.out.println("Pair2:");
            System.out.println(group.pair2.participant1);
            System.out.println(group.pair2.participant2);
            System.out.println("Pair3:");
            System.out.println(group.pair3.participant1);
            System.out.println(group.pair3.participant2);
            System.out.println();
        }

        System.out.println("Example cluster:");
        if (event.getGroupList().isEmpty()) {
            System.out.println("nicht vorhanden");
        }
        else {
            Cluster cluster = event.getGroupList().get(90).pair1.cluster;
            List<Group> groups = cluster.getGroups();
            for (int i = 0; i < groups.size(); i++) {
                System.out.println("Group" + (i+1) + ":");
                Group group = groups.get(i);
                System.out.println("Pair1:");
                System.out.println(group.pair1.participant1);
                System.out.println(group.pair1.participant2);
                System.out.println("Pair2:");
                System.out.println(group.pair2.participant1);
                System.out.println(group.pair2.participant2);
                System.out.println("Pair3:");
                System.out.println(group.pair3.participant1);
                System.out.println(group.pair3.participant2);
                System.out.println();
            }
        }
    }
}
