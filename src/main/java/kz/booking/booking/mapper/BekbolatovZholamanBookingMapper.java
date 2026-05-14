package kz.booking.booking.mapper;

import kz.booking.booking.dto.BekbolatovZholamanBookingResponse;
import kz.booking.booking.entity.BekbolatovZholamanBooking;

public class BekbolatovZholamanBookingMapper {
    private BekbolatovZholamanBookingMapper() {
    }

    public static BekbolatovZholamanBookingResponse toResponse(BekbolatovZholamanBooking b) {
        BekbolatovZholamanBookingResponse dto = new BekbolatovZholamanBookingResponse();
        dto.setId(b.getId());
        dto.setUserId(b.getUser().getId());
        dto.setUserEmail(b.getUser().getEmail());
        dto.setHotelId(b.getRoom().getHotel().getId());
        dto.setHotelName(b.getRoom().getHotel().getName());
        dto.setRoomId(b.getRoom().getId());
        dto.setRoomNumber(b.getRoom().getRoomNumber());
        dto.setStartDate(b.getStartDate());
        dto.setEndDate(b.getEndDate());
        dto.setStatus(b.getStatus());
        dto.setNotes(b.getNotes());
        return dto;
    }
}

