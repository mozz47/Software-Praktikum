package controller;

import model.*;
import view.spinfoodFrame;

import javax.swing.*;
import java.util.List;

public class MainController {

    /**
     * MainController method for main execution order of the program.
     */
    public static void main(String[] args) {

        // Read Input
        //List<Participant> participants = Reader.getParticipants();
        //Location partyLocation = Reader.getPartyLocation();


        // print human-readable version
        //printInput(participants, partyLocation);

        //collect Participants who already have a partner in pairList


        //Start GUI
        new spinfoodFrame();

    }

    /**
     * prints human-readable version of the participant and party location data
     * @param participants list of participants
     * @param partyLocation after dinner party location
     */
    static void printInput(List<Participant> participants, Location partyLocation) {
        System.out.println("Teilnehmer:");
        for (Participant p : participants) {
            System.out.println(p);
        }
        System.out.println("Party Koordinaten:");
        System.out.println(partyLocation);
    }

}
