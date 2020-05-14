package example.booking.operations;

import java.util.Collection;

import example.booking.model.BookConfirmation;
import example.booking.model.BookingInfo;
import example.booking.model.HotelDescription;
import example.booking.model.RoomDescription;

public interface HotelService {

    Collection<RoomDescription> getAvailableRooms(BookingInfo info, HotelDescription description);

    BookConfirmation bookRoom(BookingInfo info, HotelDescription description, RoomDescription roomDesc);
}
