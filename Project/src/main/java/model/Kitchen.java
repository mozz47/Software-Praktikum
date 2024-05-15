package model;

/**
 * Represents a kitchen, a type of location within a building.
 * Extends the Location class to inherit longitude and latitude attributes.
 */
public class Kitchen extends Location {
    public int story;

    /**
     * Constructs a kitchen with the specified story, longitude, and latitude.
     *
     * @param story     The story (floor) where the kitchen is located.
     * @param longitude The longitude coordinate of the kitchen's location.
     * @param latitude  The latitude coordinate of the kitchen's location.
     */
    public Kitchen(int story, double longitude, double latitude) {
        super(longitude, latitude);
        this.story = story;
    }


    /**
     * returns human-readable string version of the kitchen story and location
     */
    @Override
    public String toString() {
        return "Kitchen{" +
                "story=" + story +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
