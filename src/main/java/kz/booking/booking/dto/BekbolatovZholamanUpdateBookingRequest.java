package kz.booking.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.booking.booking.entity.BekbolatovZholamanBookingStatus;

public class BekbolatovZholamanUpdateBookingRequest {
    @NotNull
    private BekbolatovZholamanBookingStatus status;

    @Size(max = 500)
    private String notes;

    public BekbolatovZholamanBookingStatus getStatus() {
        return status;
    }

    public void setStatus(BekbolatovZholamanBookingStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

