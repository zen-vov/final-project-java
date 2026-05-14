package kz.booking.booking.repo;

import kz.booking.booking.entity.BekbolatovZholamanBooking;
import kz.booking.booking.entity.BekbolatovZholamanBookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;

public interface BekbolatovZholamanBookingRepository extends JpaRepository<BekbolatovZholamanBooking, Long> {

    @Query("""
            select b from BekbolatovZholamanBooking b
            where (:userId is null or b.user.id = :userId)
              and (:hotelId is null or b.room.hotel.id = :hotelId)
              and (:roomId is null or b.room.id = :roomId)
              and (:status is null or b.status = :status)
              and (:from is null or b.startDate >= :from)
              and (:to is null or b.endDate <= :to)
              and (:q is null or lower(b.notes) like lower(concat('%', :q, '%')))
            """)
    Page<BekbolatovZholamanBooking> search(
            @Param("userId") Long userId,
            @Param("hotelId") Long hotelId,
            @Param("roomId") Long roomId,
            @Param("status") BekbolatovZholamanBookingStatus status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("q") String q,
            Pageable pageable
    );

    @Query("""
            select (count(b) > 0) from BekbolatovZholamanBooking b
            where b.room.id = :roomId
              and b.status in :statuses
              and b.startDate <= :endDate
              and b.endDate >= :startDate
            """)
    boolean existsOverlapping(
            @Param("roomId") Long roomId,
            @Param("statuses") Collection<BekbolatovZholamanBookingStatus> statuses,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
