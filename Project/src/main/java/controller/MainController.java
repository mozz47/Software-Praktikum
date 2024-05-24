package controller;

import model.*;
import view.SpinfoodFrame;

public class MainController {

    /**
     * MainController method for main execution order of the program.
     */
    public static void main(String[] args) {
        initialize();
        run();
    }

    /**
     * Create Spinfood Event Singleton and initialize it with test data.
     */
    public static void initializeWithoutFileChooser() {
        // Create Spinfood Event
        SpinfoodEvent event = SpinfoodEvent.getInstance();

        // Read Input
        event.participants = Reader.getTestParticipants();
        event.partyLocation = Reader.getTestPartyLocation();

        // print human-readable version
        event.printInput();
    }

    /**
     * Create Spinfood Event Singleton and initialize it with data chosen via FileChooser.
     */
    public static void initialize() {
        // Create Spinfood Event
        SpinfoodEvent event = SpinfoodEvent.getInstance();

        // Read Input
        event.participants = Reader.getParticipants();
        event.partyLocation = Reader.getPartyLocation();

        // print human-readable version
        event.printInput();
    }

    /**
     * Start GUI / main event loop.
     */
    private static void run() {
        //Start GUI
        //better with SwingUtilities.invokeLater(), because it will not block threads
        new SpinfoodFrame();
    }

}
