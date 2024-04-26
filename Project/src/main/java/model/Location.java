package model;

public class Location {
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double longitude;
    public double latitude;

    public Location getLocation() {
        return new Location(longitude, latitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
