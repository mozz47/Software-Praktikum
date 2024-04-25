package controller;

import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private final PrintStream standardOut = System.out;  // to test console output
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();  // to test console output

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testPrintOutputWithSingleParticipant() {
        List<Participant> participants = new ArrayList<>();
        Kitchen k1 = new Kitchen();
        k1.story = 0;
        k1.latitude = 12.123;
        k1.longitude = 43.123;
        Participant p1 = new Participant();
        p1.id = 0;
        p1.name = "Chris";
        p1.foodPreference = FoodPreference.NONE;
        p1.age = 27;
        p1.sex = Sex.MALE;
        p1.hasKitchen = true;
        p1.mightHaveKitchen = false;
        p1.kitchen = k1;
        p1.partner = null;
        participants.add(p1);
        Location partyLocation = k1;

        Main.printInput(participants, partyLocation);

        assertEquals("""
                Teilnehmer:\r
                Participant{id=0, name='Chris', foodPreference=NONE, age=27, sex=MALE, hasKitchen=true, mightHaveKitchen=false, kitchen=Kitchen{story=0, longitude=43.123, latitude=12.123}, partner=null}\r
                Party Koordinaten:\r
                Kitchen{story=0, longitude=43.123, latitude=12.123}""", outputStreamCaptor.toString().trim());
    }
}