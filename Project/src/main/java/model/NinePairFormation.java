package model;

import java.util.List;

/**
 * Helper class to manipulate the groups of 9 used internally in the group list builder.
 */
public class NinePairFormation {

    public List<Group> groups;
    public List<Pair> pairs;

    /**
     * Constructor.
     *
     * @param groups groups
     * @param pairs pairs
     */
    public NinePairFormation(List<Group> groups, List<Pair> pairs) {
        this.groups = groups;
        this.pairs = pairs;
    }

    /**
     * Delete all groups and pairs from this formation
     */
    public void deleteAll() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        for (Group group : groups) {
            // delete group from group list
            event.getGroupList().remove(group);
        }
        for (Pair pair : pairs) {
            // add to successors
            event.getSuccessors().remove(pair.participant1);
            event.getSuccessors().remove(pair.participant2);
            // delete pair from pair list
            event.getPairList().remove(pair);
        }
    }
}
