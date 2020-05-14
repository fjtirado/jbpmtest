package example.booking.operations;

import example.booking.model.BookConfirmation;
import example.booking.model.BookingInfo;
import example.booking.model.HotelRoomsDescription;
import example.booking.model.RoomDescription;
import example.booking.model.RoomSelector;

public interface HotelService extends java.io.Serializable {

    HotelRoomsDescription getAvailableRooms(BookingInfo info, RoomSelector roomSelector);

    BookConfirmation bookRoom(RoomDescription roomDesc, BookingInfo info);
}
