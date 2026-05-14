package kz.booking.file.repo;

import kz.booking.file.entity.BekbolatovZholamanStoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BekbolatovZholamanStoredFileRepository extends JpaRepository<BekbolatovZholamanStoredFile, Long> {
    List<BekbolatovZholamanStoredFile> findByBookingId(Long bookingId);
}

