package model;

/**
 * Private class to calculate the distance between two coordinates.
 */
public class Coordinate {
    double longitude;
    double latitude;

    Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Calculates the distance between two coordinates.
     *
     * @param other coordinate
     * @return double Euclidean distance
     */
    double distanceTo(Coordinate other) {
        return Math.sqrt(Math.pow(this.longitude - other.longitude, 2) + Math.pow(this.latitude - other.latitude, 2));
    }
}
