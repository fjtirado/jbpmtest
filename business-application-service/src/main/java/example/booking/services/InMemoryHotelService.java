package example.booking.services;

import java.util.Collection;

import example.booking.model.BookConfirmation;
import example.booking.model.BookingInfo;
import example.booking.model.HotelDescription;
import example.booking.model.HotelRoomsDescription;
import example.booking.model.RoomDescription;
import example.booking.model.RoomSelector;
import example.booking.operations.HotelService;

public class InMemoryHotelService implements HotelService {

    private HotelDescription hotelDescription;
    private Collection<RoomDescription> roomTypes;

    private static long counter = 0L;

    public InMemoryHotelService(HotelDescription hotelDescription, Collection<RoomDescription> roomTypes) {
        this.hotelDescription = hotelDescription;
        this.roomTypes = roomTypes;
    }

    @Override
    public HotelRoomsDescription getAvailableRooms(BookingInfo info, RoomSelector roomSelector) {
        return new HotelRoomsDescription(hotelDescription, filterByAvailabilityAndType(info, roomSelector));
    }

    private Collection<RoomDescription> filterByAvailabilityAndType(BookingInfo info, RoomSelector roomSelector) {
        //TODO filter
        return roomTypes;
    }

    @Override
    public BookConfirmation bookRoom(RoomDescription roomDesc, BookingInfo info) {
        return new BookConfirmation(Long.toString(counter++), hotelDescription, roomDesc, info);

    }
}
