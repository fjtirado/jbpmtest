package example.booking.model;

public class BookingSelector implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private BookingInfo bookingInfo;
    private String city;
    private HotelSelector hotelSelector;
    private RoomSelector roomSelector;

    public BookingInfo getBookingInfo() {
        return bookingInfo;
    }

    public void setBookingInfo(BookingInfo bookingInfo) {
        this.bookingInfo = bookingInfo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public HotelSelector getHotelSelector() {
        return hotelSelector;
    }

    public void setHotelSelector(HotelSelector hotelSelector) {
        this.hotelSelector = hotelSelector;
    }

    public RoomSelector getRoomSelector() {
        return roomSelector;
    }

    public void setRoomSelector(RoomSelector roomSelector) {
        this.roomSelector = roomSelector;
    }

    @Override
    public String toString() {
        return "BookingSelector [bookingInfo=" + bookingInfo + ", city=" + city + ", hotelSelector=" + hotelSelector + ", roomSelector=" + roomSelector + "]";
    }
}
