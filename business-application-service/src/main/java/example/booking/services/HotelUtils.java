package example.booking.services;

import java.util.ArrayList;
import java.util.List;

class HotelUtils {

    private List<String> hotelNames = new ArrayList<>();

    private HotelUtils() {

    }

    static HotelUtils instance = new HotelUtils();

    long generateId(String hotelName) {
        long id = hotelNames.size();
        hotelNames.add(hotelName);
        return id;

    }

    String getHotelName(long id) {
        return hotelNames.get((int) id);
    }

}
