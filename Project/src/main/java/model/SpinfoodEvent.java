package model;

import java.util.List;

/**
 * Singleton class to store Spinfood Event
 */
public class SpinfoodEvent {
    private static SpinfoodEvent event;

    public List<Participant> participants;
    public List<Participant> successors;
    public Location partyLocation;
    public List<Pair> pairList;
    public List<Pair> pairListOld;
    public List<Group> groupsList;
    public List<Group> groupsListOld;

    /**
     * private constructor to prevent creating instances from outside the class
     */
    private SpinfoodEvent() {
        this.participants = null;
        this.successors = null;
        this.partyLocation = null;
        this.pairList = null;
        this.groupsList = null;
        this.pairListOld = null;
        this.groupsListOld = null;
    }

    /**
     * public method to get the instance of SpinfoodEvent singleton
     * @return SpinfoodEvent
     */
    public static SpinfoodEvent getInstance() {
        if (event == null) {
            event = new SpinfoodEvent();
        }
        return event;
    }


    /**
     * prints human-readable version of the participant and party location data
     */
    public void printInput() {
        System.out.println("Teilnehmer:");
        for (Participant p : participants) {
            System.out.println(p);
        }
        System.out.println("Party Koordinaten:");
        System.out.println(partyLocation);
    }
}
