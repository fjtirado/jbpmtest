package example.booking.model;

public class HotelSelector implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private boolean parking;
    private boolean laundry;
    private boolean wifi;
    private boolean swimmingPool;
    private boolean restaurant;
    private boolean nursery;

    public boolean isNursery() {
        return nursery;
    }

    public void setNursery(boolean nursery) {
        this.nursery = nursery;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isLaundry() {
        return laundry;
    }

    public void setLaundry(boolean laundry) {
        this.laundry = laundry;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public boolean isRestaurant() {
        return restaurant;
    }

    public void setRestaurant(boolean restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "HotelSelector [parking=" + parking + ", laundry=" + laundry + ", wifi=" + wifi + ", swimmingPool=" + swimmingPool + ", restaurant=" + restaurant + ", nursery=" + nursery + "]";
    }

}
