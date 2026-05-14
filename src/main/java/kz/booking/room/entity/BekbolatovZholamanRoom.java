package kz.booking.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.booking.common.entity.BekbolatovZholamanBaseEntity;
import kz.booking.hotel.entity.BekbolatovZholamanHotel;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class BekbolatovZholamanRoom extends BekbolatovZholamanBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private BekbolatovZholamanHotel hotel;

    @Column(nullable = false, length = 20)
    private String roomNumber;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private boolean active = true;

    protected BekbolatovZholamanRoom() {
    }

    public BekbolatovZholamanRoom(BekbolatovZholamanHotel hotel, String roomNumber, int capacity, BigDecimal pricePerNight) {
        this.hotel = hotel;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    public BekbolatovZholamanHotel getHotel() {
        return hotel;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

