package controller;

import model.Location;
import model.Participant;

import java.util.List;

/**
 * Singleton Reader Class for inputting csv files
 */
public class Reader {
    private static Reader reader;

    private Reader(){}

    public static Reader getInstance() {
        return reader;
    }

    /**
     * //todo: Parviz
     */
    public static List<Participant> getParticipants() {
        //todo: Parviz: Input von assets/teilnehmerliste.csv
        return null;
    }

    /**
     * //todo: Parviz
     */
    public static Location getPartyLocation() {
        //todo: Parviz: Input von assets/partylocation.csv
        return null;
    }
}
