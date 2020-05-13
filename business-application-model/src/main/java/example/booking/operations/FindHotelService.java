package example.booking.operations;

import java.util.Collection;

import example.booking.model.HotelSelector;
import example.booking.model.RoomSelector;

public interface FindHotelService {

    Collection<HotelService> getHotels(String location, HotelSelector hotelSelector, RoomSelector roomSelector);
}
