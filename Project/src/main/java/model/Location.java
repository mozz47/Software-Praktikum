package model;

/**
 * Represents a geographic location by its longitude and latitude coordinates.
 */
public class Location {

    public double longitude;
    public double latitude;

    /**
     * Location constructor
     * (if longitude and latitude are both 0, the Location is probably invalid)
     * @param longitude double value of longitude
     * @param latitude double value of longitude
     */
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * returns human-readable string version of the location
     */
    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
