package example.booking.model;

import java.util.Collection;

public class HotelRoomsDescription {

    private Collection<RoomDescription> roomTypes;
    private HotelDescription description;

    public HotelRoomsDescription(HotelDescription description, Collection<RoomDescription> roomTypes) {
        this.roomTypes = roomTypes;
        this.description = description;
    }

    public Collection<RoomDescription> getRoomTypes() {
        return roomTypes;
    }

    public HotelDescription getDescription() {
        return description;
    }
}
