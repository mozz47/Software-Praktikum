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
 * Singleton Reader Class for inputting csv files
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
        List<Participant> participantList = new ArrayList<>();
        try (java.io.Reader fileReader = new FileReader(teilnehmerCSV);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT)) {
            boolean firstIteration = true;
            for (CSVRecord csvRecord : csvParser) {
                if (firstIteration) { //skip first iteration, because has no data, only header
                    firstIteration = false;
                    continue;
                }
                //reading of data for participant
                String id = csvRecord.get(1); //hashCode
                FoodPreference foodPreference = FoodPreference.valueOf(csvRecord.get(3).toUpperCase());
                int age = Integer.parseInt(csvRecord.get(4));
                Sex sex = Sex.valueOf(csvRecord.get(5).toUpperCase());
                //if kitchen available add, else null
                Kitchen kitchen;
                if((csvRecord.get(6).isEmpty())) {
                    kitchen = null;
                } else {
                    //TODO read kitchen data
                    //kitchen = new Kitchen();
                }
                //TODO read rest of data

                //creating participant with data
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
