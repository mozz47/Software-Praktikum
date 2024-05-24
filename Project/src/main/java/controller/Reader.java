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
        try {
            assert TEILNEHMER_CSV != null;
            try (InputStream inputStream = new FileInputStream(TEILNEHMER_CSV)) {
                try (Scanner scanner = new Scanner(inputStream)) {

                    // skip header line
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",", -1);  // negative limit to keep empty strings

                        //READING OF DATA
                        String id = parts[1];
                        String name = parts[2];
                        FoodPreference foodPreference = FoodPreference.valueOf(parts[3].toUpperCase());
                        int age = (int) Double.parseDouble(parts[4]);
                        Sex sex = Sex.valueOf(parts[5].toUpperCase());
                        boolean hasKitchen = parts[6].equals("yes");
                        boolean mightHaveKitchen = parts[6].equals("maybe");
                        int kitchen_story = parts[7].isEmpty() ? 0 : (int) Double.parseDouble(parts[7]); //if empty, story = 0
                        double kitchen_longitude = 0;
                        double kitchen_latitude = 0;
                        if (hasKitchen || mightHaveKitchen) {
                            kitchen_longitude = Double.parseDouble(parts[8]);
                            kitchen_latitude = Double.parseDouble(parts[9]);
                        }
                        Kitchen kitchen = new Kitchen(kitchen_story, kitchen_longitude, kitchen_latitude);

                        // create instance of 1st Participant
                        Participant p1 = new Participant(id, name, foodPreference, age, sex, hasKitchen, mightHaveKitchen, kitchen, kitchen_story, null);

                        participantList.add(p1); // add him to list

                        if (parts.length > 10 && !parts[10].isEmpty()) {
                            // manage 2nd Participant
                            String id_2 = parts[10];
                            String name_2 = parts[11];
                            int age_2 = (int) Double.parseDouble(parts[12]);
                            Sex sex_2 = Sex.valueOf(parts[13].toUpperCase());

                            // create instance
                            Participant p2 = new Participant(id_2, name_2, foodPreference, age_2, sex_2, false, false, kitchen, kitchen_story, p1);

                            p1.partner = p2; // adding p2 as partner to p1

                            participantList.add(p2); // add p2 to list
                        }
                    }
                    return participantList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
