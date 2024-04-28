package controller;

import model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Read Input
        List<Participant> participants = Reader.getParticipants();
        Location partyLocation = Reader.getPartyLocation();

        // print human-readable version
        //printInput(participants, partyLocation);

    }

    static void printInput(List<Participant> participants, Location partyLocation) {
        System.out.println("Teilnehmer:");
        for (Participant p : participants) {
            System.out.println(p);
        }
        System.out.println("Party Koordinaten:");
        System.out.println(partyLocation);
    }

}
