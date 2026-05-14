package kz.booking.hotel.repo;

import kz.booking.hotel.entity.BekbolatovZholamanHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BekbolatovZholamanHotelRepository extends JpaRepository<BekbolatovZholamanHotel, Long> {
    Page<BekbolatovZholamanHotel> findByCityContainingIgnoreCase(String city, Pageable pageable);
}

