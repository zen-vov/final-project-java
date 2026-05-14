package kz.booking.hotel.mapper;

import kz.booking.hotel.dto.BekbolatovZholamanHotelResponse;
import kz.booking.hotel.entity.BekbolatovZholamanHotel;

public class BekbolatovZholamanHotelMapper {
    private BekbolatovZholamanHotelMapper() {
    }

    public static BekbolatovZholamanHotelResponse toResponse(BekbolatovZholamanHotel hotel) {
        BekbolatovZholamanHotelResponse dto = new BekbolatovZholamanHotelResponse();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setCity(hotel.getCity());
        dto.setAddress(hotel.getAddress());
        dto.setDescription(hotel.getDescription());
        return dto;
    }
}

