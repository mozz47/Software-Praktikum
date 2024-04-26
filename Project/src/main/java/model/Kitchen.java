package model;

public class Kitchen extends Location{
    public int story;
    public Kitchen(double longitude, double latitude, int story) {
        super(longitude, latitude);
        this.story = story;
    }


    @Override
    public String toString() {
        return "Kitchen{" +
                "story=" + story +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
