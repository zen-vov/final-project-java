package kz.booking.file.mapper;

import kz.booking.file.dto.BekbolatovZholamanStoredFileResponse;
import kz.booking.file.entity.BekbolatovZholamanStoredFile;

public class BekbolatovZholamanStoredFileMapper {
    private BekbolatovZholamanStoredFileMapper() {
    }

    public static BekbolatovZholamanStoredFileResponse toResponse(BekbolatovZholamanStoredFile f) {
        BekbolatovZholamanStoredFileResponse dto = new BekbolatovZholamanStoredFileResponse();
        dto.setId(f.getId());
        dto.setBookingId(f.getBooking().getId());
        dto.setOriginalFilename(f.getOriginalFilename());
        dto.setContentType(f.getContentType());
        dto.setSizeBytes(f.getSizeBytes());
        return dto;
    }
}

