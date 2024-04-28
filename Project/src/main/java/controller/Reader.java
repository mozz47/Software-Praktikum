package controller;

import model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Reader Class for csv files
 */
public class Reader {
    private static final String teilnehmerCSV = "src/main/resources/teilnehmerliste.csv";
    private static final String partylocationCSV = "src/main/resources/partylocation.csv";

    Reader() {}


    /**
     * Reads all the Info of participants from the .csv file, creates participant and adds each one to list
     * @return List of all Participants
     */
    public static List<Participant> getParticipants() {
        //TODO: Parviz: 
        List<Participant> participantList = new ArrayList<>();

        try (java.io.Reader fileReader = new FileReader(teilnehmerCSV);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT)) {
            int index = 0;
            boolean firstIteration = true;
            for (CSVRecord csvRecord : csvParser) {
                index++;
                if (firstIteration) { //skip first iteration, because has no data, only header
                    firstIteration = false;
                    System.out.println("Current working directory: " + System.getProperty("user.dir"));
                    continue;
                }
                //READING OF DATA
                String id = csvRecord.get(1); //hashCode
                String name = csvRecord.get(2);
                FoodPreference foodPreference = FoodPreference.valueOf(csvRecord.get(3).toUpperCase());
                Integer age = Integer.parseInt(csvRecord.get(4));
                Sex sex = Sex.valueOf(csvRecord.get(5).toUpperCase());

                //if kitchen available add, else null
                Kitchen kitchen;
                boolean hasKitchen;
                boolean mightHaveKitchen;

                String get_has_kitchen = csvRecord.get(7);
                String get_kitchen_story = csvRecord.get(8);
                int kitchenstory = (csvRecord.get(7).isEmpty()) ? 0 : (int) Double.parseDouble(csvRecord.get(7)); //if empty, story = 0
                String get_kitchen_longitude = csvRecord.get(9);
                String get_kitchen_latitude = csvRecord.get(10);
                String get_id_2 = csvRecord.get(11);
                String get_name_2 = csvRecord.get(12);
                String get_age_2 = csvRecord.get(13);
                String get_sex_2 = csvRecord.get(14);

                if (get_has_kitchen.equals("no") && get_kitchen_story.isEmpty()) { //has no kitchen aka story empty and == "no"
                   mightHaveKitchen = false;
                   hasKitchen = false;

                } else if (get_has_kitchen.equals("maybe")) { //maybe has kitchen
                    mightHaveKitchen = true;
                    hasKitchen = false;

                } else if (get_has_kitchen.equals("yes")) { //has kitchen
                    mightHaveKitchen = false;
                    hasKitchen = true;

                } else if () { //has kitchenstory but has no kitchen, aka partner has a kitchen
                    hasKitchen = true;
                    mightHaveKitchen = false;

                }

                //TODO read rest of data

                //CREATING PARTICIPANT
                Participant participant = new Participant();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * //todo: Parviz
     */
    public static Location getPartyLocation() {
        //todo: Parviz

        return null;
    }


}
