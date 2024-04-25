package model;

public class Kitchen extends Location{
    public int story;

    @Override
    public String toString() {
        return "Kitchen{" +
                "story=" + story +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
