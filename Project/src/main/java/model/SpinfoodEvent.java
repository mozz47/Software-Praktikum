package model;

import java.util.List;

/**
 * Singleton class to store Spinfood Event
 */
public class SpinfoodEvent {
    private static SpinfoodEvent event;  // Singleton instance

    public List<Participant> participants;
    public List<Participant> successors;
    public Location partyLocation;
    private List<Pair> pairList;
    private List<Pair> pairListOld;
    private List<Group> groupList;
    private List<Group> groupListOld;

    /**
     * private constructor to prevent creating instances from outside the class
     */
    private SpinfoodEvent() {
        this.participants = null;
        this.successors = null;
        this.partyLocation = null;
        this.pairList = null;
        this.groupList = null;
        this.pairListOld = null;
        this.groupListOld = null;
    }

    /**
     * public method to get the instance of SpinfoodEvent singleton
     *
     * @return SpinfoodEvent
     */
    public static SpinfoodEvent getInstance() {
        if (event == null) {
            event = new SpinfoodEvent();
        }
        return event;
    }

    /**
     * Updates the pair list, saving the current one in pairListOld.
     * @param pairList new pair list
     */
    public void updatePairList(List<Pair> pairList) {
        this.pairListOld = this.pairList;
        this.pairList = pairList;
    }

    /**
     * Updates the group list, saving the current one in pairListOld.
     * @param groupList new group list
     */
    public void updateGroupList(List<Group> groupList) {
        this.groupListOld = this.groupList;
        this.groupList = groupList;
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
