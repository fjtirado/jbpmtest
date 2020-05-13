package example.booking.model;

import java.util.Set;

public class RoomDescription {

    private final String id;
    private int size;
    private String description;
    private Set<RoomFeature> features;

    public RoomDescription(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Set<RoomFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<RoomFeature> features) {
        this.features = features;
    }
}
