package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of group builder algorithm. Takes pairs from SpinFoodEvent's pairList and sorts them into groups.
 */
public class GroupListBuilder {

    private List<List<Pair>> pairLists;
    private List<Pair> pairSuccessors;

    /**
     * Uses Chris' algorithm for building groups from the pairList in the SpinfoodEvent singleton.
     * The resulting group list can be found in said singleton under the name groupList.
     */
    public void buildGroupList(List<Criterion> criteria) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        // pairs from pair list that haven't been put into other lists.
        List<Pair> toBeUsed = new ArrayList<>(event.getPairList());  // clone pairList
        pairLists = new ArrayList<>();
        pairSuccessors = new ArrayList<>();

        // criterion 1: food preference (max 1 meaty in mixed group)
        //pairLists = separateMeatiesAndRest(toBeUsed);
        pairLists.add(toBeUsed);  // remove this line if applying criteria01

        // criterion 4: max 3 pairs per kitchen
        // todo: maybe it's better to check this during the pair building algo

        // criteria 6 (strict food pref separation),8 (increase sex diversity),9 (reduce travel distance) via selected order:
        executeOptionalCriteriaAlgosInCorrectOrder(criteria);
        criterion06StrictFoodSeparation();

        // criterion 2: collect into groups of unique pairs (THIS HAS TO BE THE LAST CRITERION TO USE)

        splitIntoGroupsOf9Pairs();
        collectIntoGroups();

        // put successors from pairSuccessors into event.successors
        // todo: Absprache mit Parviz notwendig, um successors richtig zu speichern
    }

    /**
     * Receives the criteria list with the 5 criteria sorted by the user. Executes algorithms corresponding to these
     * criteria in the correct order.
     * @param criteria list of 5 criteria (6-10)
     */
    private void executeOptionalCriteriaAlgosInCorrectOrder(List<Criterion> criteria) {
        for (Criterion criterion : criteria) {
            switch (criterion) {
                case Criterion_06_Food_Preference -> criterion06StrictFoodSeparation();
                case Criterion_08_Sex_Diversity -> criterion08SexDiversity();
                case Criterion_09_Path_Length -> criterion09PathLength();
                // 7 and 10 are irrelevant for the group algorithm
            }
        }
    }

    private void criterion09PathLength() {
        // todo
    }

    private void criterion08SexDiversity() {
        // todo
    }

    /**
     * Applies strictFoodSeparation() on every list in pairList and upgrades to new pairList.
     */
    private void criterion06StrictFoodSeparation() {
        List<List<Pair>> newPairLists = new ArrayList<>();
        for (List<Pair> list : pairLists) {
            newPairLists.addAll(strictFoodSeparation(list));
        }
        pairLists = newPairLists;
    }

    /**
     * Preferably, pairs and groups with identical food preferences should be formed.
     * If this is not possible, the preferences should at least be as similar as possible.
     * The preference deviation within the pairs and groups should thus be kept to a minimum.
     * The group of 'Egalis' is particularly helpful here.
     * They can be used as fillers and should preferably be paired with a 'Fleischi' when forming pairs.
     * When forming groups, Egali pairs should preferably be combined with Fleischi pairs.
     * Otherwise, there will be an increased number of withdrawals
     * shortly after the announcement of the pairs and groups.
     *
     * @return pairList (not automatically set to enable use in other methods)
     */
    private List<List<Pair>> strictFoodSeparation(List<Pair> toBeSeparated) {
        List<Pair> egalis = new ArrayList<>();
        List<Pair> meaties = new ArrayList<>();
        List<Pair> vegans = new ArrayList<>();
        List<Pair> veggies = new ArrayList<>();

        for (Pair p : toBeSeparated) {
            switch (p.getMainFoodPreference()) {
                case NONE:
                    egalis.add(p);
                case MEAT:
                    meaties.add(p);
                case VEGAN:
                    vegans.add(p);
                case VEGGIE:
                    veggies.add(p);
            }
        }

        // FILL VEGANS-GROUP up to 9

        int veganCnt = vegans.size();
        int veggieCnt = veggies.size();
        int egalisCnt = egalis.size();

        // alternative: veggies.size() % 9 != 0
        if (veganCnt < 9) {
            // fill vegan group with veggies
            if (veganCnt + veggieCnt >= 9) {
                int neededVeggies = 9 - veganCnt;
                for (int i = 0; i < neededVeggies; i++) {
                    Pair veggie = veggies.remove(0); // Remove the veggie from veggies list
                    vegans.add(veggie); // Add the veggie to vegans list
                }
            }
            else if (veganCnt + veggieCnt + egalisCnt >= 9) {
                // Add all veggies first
                vegans.addAll(veggies);
                veggies.clear();

                // Calculate remaining needed egalis
                int neededEgalis = 9 - vegans.size();
                for (int i = 0; i < neededEgalis; i++) {
                    Pair egal = egalis.remove(0); // Remove the egalis from egalis list
                    vegans.add(egal); // Add the egalis to vegans list
                }
            }
            // if both aren't enough, we just leave the vegans alone in the list.
        }

        // FILL VEGGIE-GROUP up to 9

        // Recalculate veggie count
        veggieCnt = veggies.size();

        // Fill veggies to 9 if possible
        if (veggieCnt < 9) {
            if (veggieCnt + egalisCnt >= 9) {
                int neededEgalis = 9 - veggieCnt;
                for (int i = 0; i < neededEgalis; i++) {
                    Pair egali = egalis.remove(0); // Remove the egalis from egalis list
                    veggies.add(egali); // Add the egalis to veggies list
                }
            }
        }

        // Add remaining egalis to meaties
        meaties.addAll(egalis);
        egalis.clear();

        List<List<Pair>> out = new ArrayList<>();
        out.add(vegans);
        out.add(veggies);
        out.add(meaties);

        return out;
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
    private List<List<Pair>> separateMeatiesAndRest(List<Pair> toBeSeparated) {
        List<Pair> meaties = new ArrayList<>();
        List<Pair> rest = new ArrayList<>();
        int meatyCount = 0;
        int restCount = 0;
        for (Pair p : toBeSeparated) {
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
                throw new IllegalArgumentException("Algorithm aborted, less than 9 meaties and rest.");
            }
            else {
                rest = new ArrayList<>(toBeSeparated);  // clone list
            }
        }
        else if (meatyCount < 9) {
            // throw all meaties into successors (we could keep one pair here)
            // but for now we discard all meaties
            for (Pair p : toBeSeparated) {
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
            for (Pair p : toBeSeparated) {
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
            for (Pair p : toBeSeparated) {
                if (p.getMainFoodPreference() == FoodPreference.MEAT
                        || p.getMainFoodPreference() == FoodPreference.NONE) {
                    meaties.add(p);
                } else {
                    rest.add(p);
                }
            }
        }
        List<List<Pair>> out = new ArrayList<>();
        if (!meaties.isEmpty()) {
            out.add(meaties);
        }
        if (!rest.isEmpty()) {
            out.add(rest);
        }
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
        glb.buildGroupList(criteria);
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
            Cluster cluster = event.getGroupList().get(0).pair1.cluster;
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
