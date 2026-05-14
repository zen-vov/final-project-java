package kz.booking.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import kz.booking.common.entity.BekbolatovZholamanBaseEntity;
import kz.booking.room.entity.BekbolatovZholamanRoom;
import kz.booking.user.entity.BekbolatovZholamanUser;

import java.time.LocalDate;

@Entity
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "idx_bookings_room_dates", columnList = "room_id,start_date,end_date"),
                @Index(name = "idx_bookings_user", columnList = "user_id")
        }
)
public class BekbolatovZholamanBooking extends BekbolatovZholamanBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private BekbolatovZholamanUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private BekbolatovZholamanRoom room;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BekbolatovZholamanBookingStatus status = BekbolatovZholamanBookingStatus.PENDING;

    @Column(length = 500)
    private String notes;

    protected BekbolatovZholamanBooking() {
    }

    public BekbolatovZholamanBooking(BekbolatovZholamanUser user, BekbolatovZholamanRoom room, LocalDate startDate, LocalDate endDate, String notes) {
        this.user = user;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public BekbolatovZholamanUser getUser() {
        return user;
    }

    public BekbolatovZholamanRoom getRoom() {
        return room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

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
