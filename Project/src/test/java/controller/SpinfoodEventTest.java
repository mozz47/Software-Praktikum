package controller;

import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpinfoodEventTest {
    private final PrintStream standardOut = System.out;  // to test console output
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();  // to test console output

    @BeforeEach
    public void setUp() {
        // reset event
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event = null;

        // reset console output
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testPrintOutputWithSingleParticipant() {
        List<Participant> participants = new ArrayList<>();
        Kitchen k1 = new Kitchen(0, 12.123, 43.123);
        Participant p1 = new Participant();
        p1.story = 0;
        p1.id = "0";
        p1.name = "Chris";
        p1.foodPreference = FoodPreference.NONE;
        p1.age = 27;
        p1.sex = Sex.MALE;
        p1.hasKitchen = true;
        p1.mightHaveKitchen = false;
        p1.kitchen = k1;
        p1.partner = null;
        participants.add(p1);
        Location partyLocation = new Location(11.111, 12.222);

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.participants = participants;
        event.partyLocation = partyLocation;
        event.printInput();

        assertEquals("""
                Teilnehmer:\r
                Name: 'Chris'
                Food Preference: NONE
                Age: 27
                Sex: MALE
                Kitchen available: Yes
                Kitchen: Story 0, Longitude 12,12, Latitude 43,12
                Partner: -
                Story: 0
                \r
                Party Koordinaten:\r
                Location{longitude=11.111, latitude=12.222}""", outputStreamCaptor.toString().trim());
    }
}