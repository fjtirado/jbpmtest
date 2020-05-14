package example.booking.model;

public class BookConfirmation {

    private final String bookId;
    private final HotelDescription hotelDesc;
    private final RoomDescription roomDesc;
    private final BookingInfo bookingInfo;

    public BookConfirmation(String bookId, HotelDescription hotelDesc, RoomDescription roomDesc, BookingInfo bookingInfo) {
        this.bookId = bookId;
        this.hotelDesc = hotelDesc;
        this.roomDesc = roomDesc;
        this.bookingInfo = bookingInfo;
    }

    public String getBookId() {
        return bookId;
    }

    public HotelDescription getHotelDesc() {
        return hotelDesc;
    }

    public RoomDescription getRoomDesc() {
        return roomDesc;
    }

    public BookingInfo getBookingInfo() {
        return bookingInfo;
    }

    @Override
    public String toString() {
        return "BookConfirmation [bookId=" + bookId + ", hotelDesc=" + hotelDesc + ", roomDesc=" + roomDesc + ", bookingInfo=" + bookingInfo + "]";
    }
}
