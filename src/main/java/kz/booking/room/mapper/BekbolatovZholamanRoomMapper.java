package kz.booking.room.mapper;

import kz.booking.room.dto.BekbolatovZholamanRoomResponse;
import kz.booking.room.entity.BekbolatovZholamanRoom;

public class BekbolatovZholamanRoomMapper {
    private BekbolatovZholamanRoomMapper() {
    }

    public static BekbolatovZholamanRoomResponse toResponse(BekbolatovZholamanRoom room) {
        BekbolatovZholamanRoomResponse dto = new BekbolatovZholamanRoomResponse();
        dto.setId(room.getId());
        dto.setHotelId(room.getHotel().getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setActive(room.isActive());
        return dto;
    }
}

