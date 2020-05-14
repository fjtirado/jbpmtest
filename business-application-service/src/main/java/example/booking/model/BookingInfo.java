package example.booking.model;

import java.util.Date;

public class BookingInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String userName;
    private Date startDate;
    private Date endDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "BookingInfo [userName=" + userName + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }

}
