package kz.booking.room.service;

import kz.booking.common.exception.BekbolatovZholamanNotFoundException;
import kz.booking.hotel.entity.BekbolatovZholamanHotel;
import kz.booking.hotel.repo.BekbolatovZholamanHotelRepository;
import kz.booking.room.dto.BekbolatovZholamanCreateRoomRequest;
import kz.booking.room.dto.BekbolatovZholamanUpdateRoomRequest;
import kz.booking.room.entity.BekbolatovZholamanRoom;
import kz.booking.room.repo.BekbolatovZholamanRoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BekbolatovZholamanRoomService {
    private final BekbolatovZholamanRoomRepository roomRepository;
    private final BekbolatovZholamanHotelRepository hotelRepository;

    public BekbolatovZholamanRoomService(BekbolatovZholamanRoomRepository roomRepository, BekbolatovZholamanHotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public BekbolatovZholamanRoom create(BekbolatovZholamanCreateRoomRequest req) {
        BekbolatovZholamanHotel hotel = hotelRepository.findById(req.getHotelId())
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("Hotel not found: id=" + req.getHotelId()));
        BekbolatovZholamanRoom room = new BekbolatovZholamanRoom(hotel, req.getRoomNumber(), req.getCapacity(), req.getPricePerNight());
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public BekbolatovZholamanRoom getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("Room not found: id=" + id));
    }

    @Transactional(readOnly = true)
    public Page<BekbolatovZholamanRoom> list(Long hotelId, Pageable pageable) {
        if (hotelId != null) {
            return roomRepository.findByHotelId(hotelId, pageable);
        }
        return roomRepository.findAll(pageable);
    }

    @Transactional
    public BekbolatovZholamanRoom update(Long id, BekbolatovZholamanUpdateRoomRequest req) {
        BekbolatovZholamanRoom room = getById(id);
        room.setRoomNumber(req.getRoomNumber());
        room.setCapacity(req.getCapacity());
        room.setPricePerNight(req.getPricePerNight());
        room.setActive(req.isActive());
        return roomRepository.save(room);
    }

    @Transactional
    public void delete(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new BekbolatovZholamanNotFoundException("Room not found: id=" + id);
        }
        roomRepository.deleteById(id);
    }
}

