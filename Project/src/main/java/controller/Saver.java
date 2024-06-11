package controller;

import model.FoodPreference;
import model.Group;
import model.Pair;
import model.SpinfoodEvent;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;



/**
 * Class with static methods for outputting the data to csv files.
 */
public class Saver {

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());

    /**
     * Saves current groupList from the SpinFood Singleton to a specified CSV file.
     */
    public static boolean save() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());


        List<Group> groupList = event.getGroupList();
        if (groupList == null || groupList.isEmpty()) {
            JOptionPane.showMessageDialog(null, resourceBundle.getString("noGroupListAvailableToSave"), resourceBundle.getString("error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Open a file chooser dialog for the user to select the save location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(resourceBundle.getString("selectPlaceToSaveCsv"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, resourceBundle.getString("savingCancelled"), resourceBundle.getString("cancellation"), JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = ensureUniqueFileName(fileToSave.getAbsolutePath());

        // Ensure the file has a .csv extension
        if (!filePath.toLowerCase().endsWith(".csv")) {
            filePath += ".csv";
        }

        // write to .csv
        return writeGroupListToCSV(groupList, filePath);
    }

    /**
     * Ensures the provided file name is unique by appending a number if a file with the same name already exists.
     */
    private static String ensureUniqueFileName(String basePath) {
        File file = new File(basePath);
        String baseName = file.getAbsolutePath();
        String extension = "";

        int dotIndex = baseName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < baseName.length() - 1) {
            extension = baseName.substring(dotIndex);
            baseName = baseName.substring(0, dotIndex);
        }

        int count = 1;

        while (file.exists()) {
            String newFileName = baseName + "_" + count + extension;
            file = new File(newFileName);
            count++;
        }

        return file.getAbsolutePath();
    }

    private static boolean writeGroupListToCSV(List<Group> groups, String fileName) {

        try {
            // sort groups into vegan, veggie, meat
            List<Group> veganGroups = new ArrayList<>();
            List<Group> veggieGroups = new ArrayList<>();
            List<Group> meatGroups = new ArrayList<>();

            for (Group g : groups) {
                if (g.getMainFoodPreference() == FoodPreference.VEGAN) {
                    veganGroups.add(g);
                } else if (g.getMainFoodPreference() == FoodPreference.VEGGIE) {
                    veggieGroups.add(g);
                } else if (g.getMainFoodPreference() == FoodPreference.MEAT) {
                    meatGroups.add(g);
                }
            }

            File file = new File(fileName);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileWriter fw = new FileWriter(file);
            StringBuilder sb = new StringBuilder();
            Set<String> pairSet = new HashSet<>();

            for (Group group : veganGroups) {
                addPairToCSV(group.pair1, sb, pairSet);
                addPairToCSV(group.pair2, sb, pairSet);
                addPairToCSV(group.pair3, sb, pairSet);
            }
            for (Group group : veggieGroups) {
                addPairToCSV(group.pair1, sb, pairSet);
                addPairToCSV(group.pair2, sb, pairSet);
                addPairToCSV(group.pair3, sb, pairSet);
            }
            for (Group group : meatGroups) {
                addPairToCSV(group.pair1, sb, pairSet);
                addPairToCSV(group.pair2, sb, pairSet);
                addPairToCSV(group.pair3, sb, pairSet);
            }

            fw.write(sb.toString());
            fw.close();
            JOptionPane.showMessageDialog(null, resourceBundle.getString("theGroupListWasSuccsessfullyIn") + " " + fileName + " " + resourceBundle.getString("saved"), resourceBundle.getString("success"), JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, resourceBundle.getString("errorWhileSaving") + " " + e.getMessage(), resourceBundle.getString("error"), JOptionPane.ERROR_MESSAGE);

        }
        return false;
    }

    private static void addPairToCSV(Pair pair, StringBuilder sb, Set<String> pairSet) {
        String pairString = pairToString(pair);
        if (!pairSet.contains(pairString)) {
            pairSet.add(pairString);
            sb.append(pairString).append("\n");
        }
    }

    private static String pairToString(Pair pair) {
        // convert MainFoodPreference according to Output criteria (NONE should be ANY)
        String foodPreference;
        if (pair.getMainFoodPreference() == FoodPreference.NONE) {
            foodPreference = "ANY";
        } else {
            foodPreference = pair.getMainFoodPreference().toString();
        }

        // identify the meal in which this pair has to cook
        int cookingMeal = pair.cluster.getCookingMeal(pair);

        return pair.participant1.name + ";" +
                pair.participant2.name + ";" +
                pair.registeredAsPair + ";" +
                pair.getKitchen().longitude + ";" +
                pair.getKitchen().latitude + ";" +
                foodPreference + ";" +
                pair.id + ";" +
                pair.cluster.groupA.id + ";" +
                pair.cluster.groupB.id + ";" +
                pair.cluster.groupC.id + ";" +
                pair.p2sKitchenIsUsed + ";" +
                cookingMeal;
    }
}
