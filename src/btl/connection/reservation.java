package btl.connection;

import java.time.LocalDateTime;

public class reservation {
    private int reservationId;
    private customer customer;
    private table table;
    private int numberOfPeople;
    private LocalDateTime reservationTime;

    // Getters và Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public customer getCustomer() {
        return customer;
    }

    public void setCustomer(customer customer) {
        this.customer = customer;
    }

    public table getTable() {
        return table;
    }

    public void setTable(table table) {
        this.table = table;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}

