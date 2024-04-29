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
        //TODO: Parviz: Not finding teilnehmerCSV
        List<Participant> participantList = new ArrayList<>();

        try (java.io.Reader fileReader = new FileReader(teilnehmerCSV);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT)) {
            boolean firstIteration = true;
            for (CSVRecord csvRecord : csvParser) {
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

                String get_has_kitchen = csvRecord.get(6);
                int kitchenstory = (csvRecord.get(7).isEmpty()) ? 0 : (int) Double.parseDouble(csvRecord.get(7)); //if empty, story = 0
                String get_kitchen_longitude = csvRecord.get(8);
                String get_kitchen_latitude = csvRecord.get(9);
                String get_id_2 = csvRecord.get(10);
                String get_name_2 = csvRecord.get(11);
                String get_age_2 = csvRecord.get(12);
                String get_sex_2 = csvRecord.get(13);

                if (get_has_kitchen.equals("no")) { //has no kitchen aka story empty and == "no"
                   mightHaveKitchen = false;
                   hasKitchen = false;
                   kitchen = null;
                } else {
                    if (get_has_kitchen.equals("maybe")) { //maybe has kitchen
                        mightHaveKitchen = true;
                        hasKitchen = false;
                    } else { //has kitchen
                        mightHaveKitchen = false;
                        hasKitchen = true;
                    }
                    kitchen = new Kitchen(Double.parseDouble(get_kitchen_longitude), Double.parseDouble(get_kitchen_latitude));
                }

                //CREATING PARTICIPANT (and partner)
                Participant p1 = new Participant(id, name, foodPreference, age, sex, hasKitchen, mightHaveKitchen, kitchen, kitchenstory, null);
                Participant p2;
                //add Participant(s) to List
                participantList.add(p1);
                if (!get_id_2.isEmpty()) { //if partner avaialable
                    p2 = new Participant(get_id_2, get_name_2, foodPreference, (int) Double.parseDouble(get_age_2), Sex.valueOf(get_sex_2.toUpperCase()), false, false, null, kitchenstory, p1);
                    p1.partner = p2;
                    participantList.add(p2);
                }

            }
            return participantList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * //Read partylocation from partylocation.csv
     */
    public static Location getPartyLocation() {
        try (java.io.Reader fileReader = new FileReader(partylocationCSV);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT)) {
            boolean firstIteration = true;
            for (CSVRecord csvRecord : csvParser) {
                if (firstIteration) { //skip first iteration, because has no data, only header
                    firstIteration = false;
                    System.out.println("Current working directory: " + System.getProperty("user.dir"));
                    continue;
                }
                String get_longitude = csvRecord.get(0);
                String get_latitude = csvRecord.get(1);

                return new Location(Double.parseDouble(get_longitude), Double.parseDouble(get_latitude));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
