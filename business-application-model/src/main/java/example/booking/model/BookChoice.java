package example.booking.model;

public class BookChoice {

    private long hotelId;
    private String roomType;

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "BookChoice [hotelId=" + hotelId + ", roomType=" + roomType + "]";
    }

}
