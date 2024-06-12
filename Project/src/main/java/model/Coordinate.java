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
     * Calculates the distance between this coordinate and another coordinate using the Haversine formula.
     *
     * @param other the other coordinate
     * @return the distance in kilometers
     */
    double distanceTo(Coordinate other) {
        //Using Haversine Formula to calculate distance in kilometers with longitude and latitude
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(other.latitude - this.latitude);
        double lonDistance = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // convert to kilometers
    }
}
