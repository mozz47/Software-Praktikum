package controller;

import model.*;
import view.SpinfoodFrame;

import java.util.List;

public class MainController {

    /**
     * MainController method for main execution order of the program.
     */
    public static void main(String[] args) {
        // Create Spinfood Event
        SpinfoodEvent event = SpinfoodEvent.getInstance();

        // Read Input
        event.participants = Reader.getParticipants();
        event.partyLocation = Reader.getPartyLocation();

        // print human-readable version
        event.printInput();

        //Start GUI
        //better with SwingUtilities.invokeLater(), because it will not block threads
        new SpinfoodFrame();
    }

}
