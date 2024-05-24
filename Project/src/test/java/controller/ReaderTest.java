package controller;

import model.Location;
import model.Participant;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    @Test
    void testFirstParticipantAge() {
        List<Participant> participants = Reader.getTestParticipants();
        assertEquals(21, participants != null ? participants.get(0).age : 0);
    }

    @Test
    void testFirstParticipantName() {
        List<Participant> participants = Reader.getTestParticipants();
        assertEquals("Person1", participants != null ? participants.get(0).name : null);
    }

    @Test
    void testSecondPersonKitchen() {
        List<Participant> participants = Reader.getTestParticipants();
        Participant secondPerson = participants.get(1);
        assertTrue(secondPerson.hasKitchen);
        assertFalse(secondPerson.mightHaveKitchen);
        assertEquals(1, secondPerson.story);
        assertEquals(8.718914539788807, secondPerson.kitchen.longitude);
        assertEquals(50.590899839788804, secondPerson.kitchen.latitude);
    }

    @Test
    void testFirstPersonKitchen() {
        List<Participant> participants = Reader.getTestParticipants();
        Participant secondPerson = participants.get(0);
        assertFalse(secondPerson.hasKitchen);
        assertTrue(secondPerson.mightHaveKitchen);
        assertEquals(3, secondPerson.story);
        assertEquals(8.673368271555807, secondPerson.kitchen.longitude);
        assertEquals(50.5941282715558, secondPerson.kitchen.latitude);
    }

    @Test
    void testSixthParticipantHasPartner() {
        List<Participant> participants = Reader.getTestParticipants();
        assertNotNull(participants.get(5).partner);
    }

    @Test
    void testFirstParticipantHasNoPartner() {
        List<Participant> participants = Reader.getTestParticipants();
        assertNull(participants.get(0).partner);
    }

    @Test
    void testGetPartyLocation() {
        Location testLocation = Reader.getTestPartyLocation();
        assertEquals(8.6746166676233, testLocation.longitude);
        assertEquals(50.5909317660173, testLocation.latitude);
    }
}