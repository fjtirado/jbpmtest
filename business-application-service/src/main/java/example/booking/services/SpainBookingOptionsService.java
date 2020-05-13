package example.booking.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import example.booking.model.HotelDescription;
import example.booking.model.HotelSelector;
import example.booking.model.RoomDescription;
import example.booking.model.RoomFeature;
import example.booking.model.RoomSelector;
import example.booking.operations.BookingOptionsService;
import example.booking.operations.FindHotelService;
import example.booking.operations.HotelService;
import org.springframework.stereotype.Component;

@Component
public class SpainBookingOptionsService implements BookingOptionsService, FindHotelService {

    private final Map<String, Collection<HotelService>> cityHotels = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        RoomDescription suite = new RoomDescription("suite");
        suite.setDescription("The most expensive room in the hotel");
        suite.setSize(100);
        suite.setFeatures(EnumSet.allOf(RoomFeature.class));

        RoomDescription superior = new RoomDescription("superior");
        suite.setDescription("For finest travellers");
        suite.setSize(60);
        suite.setFeatures(EnumSet.of(RoomFeature.SOFA, RoomFeature.KING_SIZE, RoomFeature.DESKTOP, RoomFeature.BATHTUB));

        RoomDescription standard = new RoomDescription("standard");
        suite.setDescription("For finest travellers");
        suite.setSize(35);
        suite.setFeatures(EnumSet.of(RoomFeature.SOFA, RoomFeature.DESKTOP));

        cityHotels.put("Madrid", Arrays.asList(buildHotelService("Hilton Gran Via", superior, standard), buildHotelService("Ritz", suite, superior), buildHotelService("Palace", suite, superior, standard)));
        cityHotels.put("Valencia", Arrays.asList(buildHotelService("Jaime I", suite, standard), buildHotelService("Sidi Saler", superior, standard), buildHotelService("Astoria", standard)));
        cityHotels.put("Sevilla", Arrays.asList(buildHotelService("Alfonso XIII", suite, superior, standard), buildHotelService("M Deluxe", superior, standard), buildHotelService("Colon", standard)));
    }

    @Override
    public Collection<String> getCities() {
        return cityHotels.keySet();
    }

    @Override
    public Collection<HotelService> getHotels(String location, HotelSelector hotelSelector, RoomSelector roomSelector) {
        return cityHotels.get(location);
    }

    private HotelService buildHotelService(String name, RoomDescription... roomTypes) {
        return new InMemoryHotelService(new HotelDescription(name), Arrays.asList(roomTypes));
    }

}
