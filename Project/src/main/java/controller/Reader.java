package controller;

import model.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reader Class for csv files
 */
public class Reader {
    private static final String TEILNEHMER_CSV = "/teilnehmerliste.csv";
    private static final String PARTY_LOCATION_CSV = "/partylocation.csv";

    /**
     * Reads all the Info of participants from the .csv file, creates participant and adds each one to list
     * @return List of all Participants
     */
    public static List<Participant> getParticipants() {
        List<Participant> participantList = new ArrayList<>();

        try (InputStream inputStream = Reader.class.getResourceAsStream(TEILNEHMER_CSV)) {
            assert inputStream != null;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads party location from partylocation.csv
     */
    public static Location getPartyLocation() {
        try (InputStream inputStream = Reader.class.getResourceAsStream(PARTY_LOCATION_CSV)) {
            assert inputStream != null;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
