package example.booking.model;

import java.util.Collection;
import java.util.Set;

public class HotelDescription implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private final long id;
    private final String name;
    private String address;
    private String webUrl;
    private byte starRating;
    private int distance; // from city centre
    private String description;
    private Set<HotelFeature> features;
    private Collection<RoomDescription> roomTypes;

    public HotelDescription(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HotelDescription other = (HotelDescription) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public Collection<RoomDescription> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Collection<RoomDescription> roomTypes) {
        this.roomTypes = roomTypes;
    }

    @Override
    public String toString() {
        return "HotelDescription [id=" + id + ", name=" + name + ", address=" + address + ", webUrl=" + webUrl + ", starRating=" + starRating + ", distance=" + distance + ", description=" + description + ", features=" +
               features + ", roomTypes=" + roomTypes + "]";
    }

}
