package com.uplift.desktop.controller.rider;

import javafx.beans.property.*;

public class RiderScheduledRideRow {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty route = new SimpleStringProperty();
    private final StringProperty dateTime = new SimpleStringProperty();
    private final IntegerProperty seatsLeft = new SimpleIntegerProperty();
    private final StringProperty pricePerSeat = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public RiderScheduledRideRow() {}

    // getters / setters / properties
    public long getId() { return id.get(); }
    public void setId(long value) { id.set(value); }
    public LongProperty idProperty() { return id; }

    public String getRoute() { return route.get(); }
    public void setRoute(String value) { route.set(value); }
    public StringProperty routeProperty() { return route; }

    public String getDateTime() { return dateTime.get(); }
    public void setDateTime(String value) { dateTime.set(value); }
    public StringProperty dateTimeProperty() { return dateTime; }

    public int getSeatsLeft() { return seatsLeft.get(); }
    public void setSeatsLeft(int value) { seatsLeft.set(value); }
    public IntegerProperty seatsLeftProperty() { return seatsLeft; }

    public String getPricePerSeat() { return pricePerSeat.get(); }
    public void setPricePerSeat(String value) { pricePerSeat.set(value); }
    public StringProperty pricePerSeatProperty() { return pricePerSeat; }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }
}
