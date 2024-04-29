package model;

public class Kitchen extends Location{
    public int story;
    public Kitchen(double longitude, double latitude) {
        super(longitude, latitude);
    }


    @Override
    public String toString() {
        return "Kitchen{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
