package example.booking.model;

public class BookConfirmation implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private final String bookId;
    private final String hotelName;
    private final String roomType;
    private final BookingInfo bookingInfo;

    public BookConfirmation(String bookId, String hotelName, String roomType, BookingInfo bookingInfo) {
        this.bookId = bookId;
        this.hotelName = hotelName;
        this.roomType = roomType;
        this.bookingInfo = bookingInfo;
    }

    public String getBookId() {
        return bookId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public BookingInfo getBookingInfo() {
        return bookingInfo;
    }

    @Override
    public String toString() {
        return "BookConfirmation [bookId=" + bookId + ", hotel=" + hotelName + ", roomType=" + roomType + ", bookingInfo=" + bookingInfo + "]";
    }
}
