package example.booking.services;

import java.util.Collection;
import java.util.UUID;

import example.booking.model.BookConfirmation;
import example.booking.model.BookingInfo;
import example.booking.model.HotelDescription;
import example.booking.model.RoomDescription;
import example.booking.operations.HotelService;
import org.springframework.stereotype.Component;

@Component
public class AlwaysAvailableHotelService implements HotelService {

    @Override
    public Collection<RoomDescription> getAvailableRooms(BookingInfo info, HotelDescription description) {
        return description.getRoomTypes();
    }

    @Override
    public BookConfirmation bookRoom(BookingInfo info, long hotelId, String roomType) {
        return new BookConfirmation(UUID.randomUUID().toString(), HotelUtils.instance.getHotelName(hotelId), roomType, info);
    }
}
