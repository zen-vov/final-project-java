package kz.booking.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.booking.common.entity.BekbolatovZholamanBaseEntity;

@Entity
@Table(name = "hotels")
public class BekbolatovZholamanHotel extends BekbolatovZholamanBaseEntity {
    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(length = 500)
    private String description;

    protected BekbolatovZholamanHotel() {
    }

    public BekbolatovZholamanHotel(String name, String city, String address, String description) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

