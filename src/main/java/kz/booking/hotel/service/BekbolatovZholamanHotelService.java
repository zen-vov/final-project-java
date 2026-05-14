package kz.booking.hotel.service;

import kz.booking.common.exception.BekbolatovZholamanNotFoundException;
import kz.booking.hotel.dto.BekbolatovZholamanCreateHotelRequest;
import kz.booking.hotel.dto.BekbolatovZholamanUpdateHotelRequest;
import kz.booking.hotel.entity.BekbolatovZholamanHotel;
import kz.booking.hotel.repo.BekbolatovZholamanHotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BekbolatovZholamanHotelService {
    private final BekbolatovZholamanHotelRepository hotelRepository;

    public BekbolatovZholamanHotelService(BekbolatovZholamanHotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public BekbolatovZholamanHotel create(BekbolatovZholamanCreateHotelRequest req) {
        BekbolatovZholamanHotel hotel = new BekbolatovZholamanHotel(req.getName(), req.getCity(), req.getAddress(), req.getDescription());
        return hotelRepository.save(hotel);
    }

    @Transactional(readOnly = true)
    public BekbolatovZholamanHotel getById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("Hotel not found: id=" + id));
    }

    @Transactional(readOnly = true)
    public Page<BekbolatovZholamanHotel> list(String city, String q, Pageable pageable) {
        if (city != null && !city.isBlank()) {
            return hotelRepository.findByCityContainingIgnoreCase(city.trim(), pageable);
        }
        if (q != null && !q.isBlank()) {
            return hotelRepository.findByNameContainingIgnoreCase(q.trim(), pageable);
        }
        return hotelRepository.findAll(pageable);
    }

    @Transactional
    public BekbolatovZholamanHotel update(Long id, BekbolatovZholamanUpdateHotelRequest req) {
        BekbolatovZholamanHotel hotel = getById(id);
        hotel.setName(req.getName());
        hotel.setCity(req.getCity());
        hotel.setAddress(req.getAddress());
        hotel.setDescription(req.getDescription());
        return hotelRepository.save(hotel);
    }

    @Transactional
    public void delete(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new BekbolatovZholamanNotFoundException("Hotel not found: id=" + id);
        }
        hotelRepository.deleteById(id);
    }
}

