package example.booking.model;

import java.util.Set;

public class HotelDescription implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private final String name;
    private String address;
    private String webUrl;
    private byte starRating;
    private int distance; // from city centre
    private String description;
    private Set<HotelFeature> features;

    public HotelDescription(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public byte getStarRating() {
        return starRating;
    }

    public void setStarRating(byte starRating) {
        this.starRating = starRating;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<HotelFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<HotelFeature> features) {
        this.features = features;
    }

    public String getName() {
        return name;
    }

}
