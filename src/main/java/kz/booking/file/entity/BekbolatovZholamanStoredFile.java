package kz.booking.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kz.booking.booking.entity.BekbolatovZholamanBooking;
import kz.booking.common.entity.BekbolatovZholamanBaseEntity;

@Entity
@Table(name = "stored_files")
public class BekbolatovZholamanStoredFile extends BekbolatovZholamanBaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private BekbolatovZholamanBooking booking;

    @Column(nullable = false, length = 200)
    private String originalFilename;

    @Column(nullable = false, length = 200)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false, unique = true, length = 200)
    private String storageKey;

    protected BekbolatovZholamanStoredFile() {
    }

    public BekbolatovZholamanStoredFile(BekbolatovZholamanBooking booking, String originalFilename, String contentType, long sizeBytes, String storageKey) {
        this.booking = booking;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.storageKey = storageKey;
    }

    public BekbolatovZholamanBooking getBooking() {
        return booking;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getStorageKey() {
        return storageKey;
    }
}

