package model;

public class Kitchen extends Location{
    public int story;

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
