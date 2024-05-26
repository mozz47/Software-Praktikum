package controller;

import model.*;
import view.SpinfoodFrame;

public class Main {

    /**
     * MainController method for main execution order of the program.
     */
    public static void main(String[] args) {
        initializeWithoutFileChooser();
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
    }

    /**
     * Start GUI / main event loop.
     */
    private static void run() {
        // print human-readable version of input
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        System.out.println("Human-readable version of input data:");
        event.printInput();

        //Start GUI
        //might be better with SwingUtilities.invokeLater(), because it will not block threads
        new SpinfoodFrame();
    }

}
