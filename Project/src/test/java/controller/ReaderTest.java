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
        List<Participant> participants = Reader.getParticipants();
        assertEquals(21, participants != null ? participants.get(0).age : 0);
    }

    @Test
    void testFirstParticipantName() {
        List<Participant> participants = Reader.getParticipants();
        assertEquals("Person1", participants != null ? participants.get(0).name : null);
    }

    @Test
    void testSixthParticipantHasPartner() {
        List<Participant> participants = Reader.getParticipants();
        assertNotNull(participants.get(5).partner);
    }

    @Test
    void testFirstParticipantHasNoPartner() {
        List<Participant> participants = Reader.getParticipants();
        assertNull(participants.get(0).partner);
    }

    @Test
    void testGetPartyLocation() {
        Location testLocation = Reader.getPartyLocation();
        assertEquals(testLocation.longitude, 8.6746166676233);
        assertEquals(testLocation.latitude, 50.5909317660173);

    }
}