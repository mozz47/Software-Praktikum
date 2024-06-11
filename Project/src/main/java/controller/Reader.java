package controller;

import model.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class for reading data from CSV files.
 */
public class Reader {

    /**
     * Reads all the Info of participants from the test teilnemerliste.csv file, creates participant and adds each one to list
     *
     * @return List of all Participants
     */
    public static List<Participant> getTestParticipants() {
        String relativePath = "/teilnehmerliste.csv";

        // conversion from relative to absolute path
        String absolutePath = getAbsolutePath(relativePath);

        return getParticipants(absolutePath);
    }

    /**
     * Reads all the Info of participants from a selected .csv file, creates participant and adds each one to list
     *
     * @return List of all Participants
     */
    public static List<Participant> getParticipants() {
        return getParticipants(getFilePath("Select Teilnehmer.csv"));
    }

    /**
     * Reads all the Info of participants from the .csv file, creates participant and adds each one to list
     *
     * @return List of all Participants
     */
    private static List<Participant> getParticipants(String TEILNEHMER_CSV) {
        List<Participant> participantList = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(TEILNEHMER_CSV);
             Scanner scanner = new Scanner(inputStream)) {

            if (scanner.hasNextLine()) {
                scanner.nextLine();  // skip header line
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);  // negative limit to keep empty strings

                // Reading data
                String id = parts[1];
                String name = parts[2];
                FoodPreference foodPreference = FoodPreference.valueOf(parts[3].toUpperCase());
                int age = (int) Double.parseDouble(parts[4]);
                Sex sex = Sex.valueOf(parts[5].toUpperCase());
                boolean hasKitchen = parts[6].equalsIgnoreCase("yes");
                boolean mightHaveKitchen = parts[6].equalsIgnoreCase("maybe");
                int kitchenStory = parts[7].isEmpty() ? 0 : (int) Double.parseDouble(parts[7]);
                double kitchenLongitude = 0;
                double kitchenLatitude = 0;

                if (hasKitchen || mightHaveKitchen) {
                    kitchenLongitude = Double.parseDouble(parts[8]);
                    kitchenLatitude = Double.parseDouble(parts[9]);
                }

                Kitchen kitchen = new Kitchen(kitchenStory, kitchenLongitude, kitchenLatitude);
                Participant p1 = new Participant(id, name, foodPreference, age, sex, hasKitchen, mightHaveKitchen, kitchen, kitchenStory, null);
                participantList.add(p1);  // Add to list

                if (parts.length > 10 && !parts[10].isEmpty()) {
                    // Manage 2nd Participant
                    String id2 = parts[10];
                    String name2 = parts[11];
                    int age2 = (int) Double.parseDouble(parts[12]);
                    Sex sex2 = Sex.valueOf(parts[13].toUpperCase());

                    // Create instance
                    Participant p2 = new Participant(id2, name2, foodPreference, age2, sex2, false, false, kitchen, kitchenStory, p1);
                    p1.partner = p2;  // Add p2 as partner to p1
                    participantList.add(p2);  // Add p2 to list
                }
            }
            return participantList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads all the Info of participants from the test partylocation.csv file, creates participant and adds each one to list
     *
     * @return List of all Participants
     */
    public static Location getTestPartyLocation() {
        String relativePath = "/partylocation.csv";
        String absolutePath = getAbsolutePath(relativePath);
        return getPartyLocation(absolutePath);
    }

    /**
     * Reads all the Info of participants from a selected .csv file, creates participant and adds each one to list
     *
     * @return List of all Participants
     */
    public static Location getPartyLocation() {
        return getPartyLocation(getFilePath("Select PartyLocation.csv"));
    }

    /**
     * Reads party location from partylocation.csv
     */
    private static Location getPartyLocation(String PARTY_LOCATION_CSV) {
        try {
            assert PARTY_LOCATION_CSV != null;
            try (InputStream inputStream = new FileInputStream(PARTY_LOCATION_CSV)) {
                try (Scanner scanner = new Scanner(inputStream)) {

                    // skip header line
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }

                    if (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",");
                        String longitude = parts[0];
                        String latitude = parts[1];
                        return new Location(Double.parseDouble(longitude), Double.parseDouble(latitude));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves the absolute path of a selected file using a file chooser dialog.
     *
     * @param fileChooserTitle the title to be displayed in the file chooser dialog
     * @return the absolute path of the selected file, or null if the file selection was canceled
     */
    private static String getFilePath(String fileChooserTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(fileChooserTitle);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("File selection was canceled.");
            return null;
        }
    }

    /**
     * Returns the absolute path of the given relative path
     *
     * @param relativePath relative path
     * @return absolute path
     */
    private static String getAbsolutePath(String relativePath) {
        URL resourceUrl = Reader.class.getResource(relativePath);
        if (resourceUrl == null) {
            try {
                throw new FileNotFoundException("Resource not found: " + relativePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        File file = null;
        try {
            file = new File(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String absolutePath = file.getAbsolutePath();
        return absolutePath;
    }
}
