package kz.booking.room.repo;

import kz.booking.room.entity.BekbolatovZholamanRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BekbolatovZholamanRoomRepository extends JpaRepository<BekbolatovZholamanRoom, Long> {
    Page<BekbolatovZholamanRoom> findByHotelId(Long hotelId, Pageable pageable);
}

