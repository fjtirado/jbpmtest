package example.booking.model;

public class BookingHotelDescHolder implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private BookingInfo bookingInfo;
    private HotelDescription hotelDescription;

    public BookingHotelDescHolder(BookingInfo bookingInfo, HotelDescription hotelDescription) {
        this.bookingInfo = bookingInfo;
        this.hotelDescription = hotelDescription;
    }

    public BookingInfo getBookingInfo() {
        return bookingInfo;
    }

    public void setBookingInfo(BookingInfo bookingInfo) {
        this.bookingInfo = bookingInfo;
    }

    public HotelDescription getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(HotelDescription hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    @Override
    public String toString() {
        return "BookingHotelDescHolder [bookingInfo=" + bookingInfo + ", hotelDescription=" + hotelDescription + "]";
    }
}
