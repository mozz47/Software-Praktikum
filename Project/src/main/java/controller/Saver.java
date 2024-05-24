package controller;

import model.FoodPreference;
import model.Group;
import model.Pair;
import model.SpinfoodEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with static methods for outputting the data to csv files.
 */
public class Saver {

    /**
     * Saves current groupList from the SpinFood Singleton to vegan.csv, veggie.csv and meat.csv.
     */
    public static void save() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();

        List<Group> groupList = event.getGroupList();

        // sort groups into vegan, veggie, meat
        List<Group> veganGroups = new ArrayList<>();
        List<Group> veggieGroups = new ArrayList<>();
        List<Group> meatGroups = new ArrayList<>();

        for (Group g : groupList) {
            if (g.getMainFoodPreference() == FoodPreference.VEGAN) {
                veganGroups.add(g);
            } else if (g.getMainFoodPreference() == FoodPreference.VEGGIE) {
                veggieGroups.add(g);
            } else if (g.getMainFoodPreference() == FoodPreference.MEAT) {
                meatGroups.add(g);
            }
        }

        // write to .csv
        writeGroupListToCSV(veganGroups, "vegan.csv");
        writeGroupListToCSV(veggieGroups, "veggie.csv");
        writeGroupListToCSV(meatGroups, "meat.csv");
    }

    private static void writeGroupListToCSV(List<Group>  groups, String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            StringBuilder sb = new StringBuilder();
            for (Group group : groups) {
                sb.append(pairToString(group.pair1));
                sb.append(pairToString(group.pair2));
                sb.append(pairToString(group.pair3));
            }
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String pairToString(Pair pair) {
        return pair.participant1.name + ";" +
                pair.participant2.name + ";" +
                pair.registeredAsPair + ";" +
                pair.getKitchen().longitude + ";" +
                pair.getKitchen().latitude + ";" +
                pair.getMainFoodPreference().toString() + ";" +
                pair.id + ";" +
                pair.cluster.groupA.id + ";" +
                pair.cluster.groupB.id + ";" +
                pair.cluster.groupC.id + ";" +
                pair.p2sKitchenIsUsed + ";" +
                "\n";
    }
}
