package kz.booking.booking.service;

import kz.booking.booking.dto.BekbolatovZholamanCreateBookingRequest;
import kz.booking.booking.dto.BekbolatovZholamanUpdateBookingRequest;
import kz.booking.booking.entity.BekbolatovZholamanBooking;
import kz.booking.booking.entity.BekbolatovZholamanBookingStatus;
import kz.booking.booking.repo.BekbolatovZholamanBookingRepository;
import kz.booking.common.exception.BekbolatovZholamanBadRequestException;
import kz.booking.common.exception.BekbolatovZholamanForbiddenException;
import kz.booking.common.exception.BekbolatovZholamanNotFoundException;
import kz.booking.common.security.BekbolatovZholamanSecurityUtils;
import kz.booking.notification.BekbolatovZholamanNotificationService;
import kz.booking.room.entity.BekbolatovZholamanRoom;
import kz.booking.room.repo.BekbolatovZholamanRoomRepository;
import kz.booking.user.entity.BekbolatovZholamanUser;
import kz.booking.user.repo.BekbolatovZholamanUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BekbolatovZholamanBookingService {
    private final BekbolatovZholamanBookingRepository bookingRepository;
    private final BekbolatovZholamanRoomRepository roomRepository;
    private final BekbolatovZholamanUserRepository userRepository;
    private final BekbolatovZholamanNotificationService notificationService;

    public BekbolatovZholamanBookingService(
            BekbolatovZholamanBookingRepository bookingRepository,
            BekbolatovZholamanRoomRepository roomRepository,
            BekbolatovZholamanUserRepository userRepository,
            BekbolatovZholamanNotificationService notificationService
    ) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public BekbolatovZholamanBooking createForCurrentUser(BekbolatovZholamanCreateBookingRequest req) {
        String email = BekbolatovZholamanSecurityUtils.currentEmailOrNull();
        if (email == null) {
            throw new BekbolatovZholamanForbiddenException("Unauthorized");
        }

        BekbolatovZholamanUser user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("User not found: email=" + email));

        BekbolatovZholamanRoom room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("Room not found: id=" + req.getRoomId()));

        validateDates(req.getStartDate(), req.getEndDate());

        boolean overlap = bookingRepository.existsOverlapping(
                room.getId(),
                List.of(BekbolatovZholamanBookingStatus.PENDING, BekbolatovZholamanBookingStatus.CONFIRMED),
                req.getStartDate(),
                req.getEndDate()
        );
        if (overlap) {
            throw new BekbolatovZholamanBadRequestException("Room is already booked for these dates");
        }

        BekbolatovZholamanBooking booking = new BekbolatovZholamanBooking(user, room, req.getStartDate(), req.getEndDate(), req.getNotes());
        BekbolatovZholamanBooking saved = bookingRepository.save(booking);
        notificationService.sendBookingCreatedEmail(user.getEmail(), saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public BekbolatovZholamanBooking getByIdForCurrentUserOrAdmin(Long id) {
        BekbolatovZholamanBooking b = bookingRepository.findById(id)
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("Booking not found: id=" + id));

        if (BekbolatovZholamanSecurityUtils.hasRole("ROLE_ADMIN")) {
            return b;
        }

        String email = BekbolatovZholamanSecurityUtils.currentEmailOrNull();
        if (email == null || !b.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new BekbolatovZholamanForbiddenException("Access denied");
        }
        return b;
    }

    @Transactional(readOnly = true)
    public Page<BekbolatovZholamanBooking> searchForCurrentUserOrAdmin(
            Long userId,
            Long hotelId,
            Long roomId,
            BekbolatovZholamanBookingStatus status,
            LocalDate from,
            LocalDate to,
            String q,
            Pageable pageable
    ) {
        if (!BekbolatovZholamanSecurityUtils.hasRole("ROLE_ADMIN")) {
            String email = BekbolatovZholamanSecurityUtils.currentEmailOrNull();
            BekbolatovZholamanUser me = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new BekbolatovZholamanNotFoundException("User not found: email=" + email));
            userId = me.getId();
        }

        return bookingRepository.search(userId, hotelId, roomId, status, from, to, (q == null || q.isBlank()) ? null : q.trim(), pageable);
    }

    @Transactional
    public BekbolatovZholamanBooking update(Long id, BekbolatovZholamanUpdateBookingRequest req) {
        BekbolatovZholamanBooking b = getByIdForCurrentUserOrAdmin(id);

        // Only ADMIN can confirm; user can cancel own booking.
        if (req.getStatus() == BekbolatovZholamanBookingStatus.CONFIRMED && !BekbolatovZholamanSecurityUtils.hasRole("ROLE_ADMIN")) {
            throw new BekbolatovZholamanForbiddenException("Only admin can confirm bookings");
        }

        if (req.getStatus() == BekbolatovZholamanBookingStatus.CANCELED) {
            // user or admin can cancel
            b.setStatus(BekbolatovZholamanBookingStatus.CANCELED);
        } else {
            b.setStatus(req.getStatus());
        }

        b.setNotes(req.getNotes());
        BekbolatovZholamanBooking saved = bookingRepository.save(b);

        if (saved.getStatus() == BekbolatovZholamanBookingStatus.CONFIRMED) {
            notificationService.sendBookingConfirmedEmail(saved.getUser().getEmail(), saved.getId());
        }

        return saved;
    }

    @Transactional
    public void delete(Long id) {
        // Admin only in controller via @PreAuthorize
        if (!bookingRepository.existsById(id)) {
            throw new BekbolatovZholamanNotFoundException("Booking not found: id=" + id);
        }
        bookingRepository.deleteById(id);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new BekbolatovZholamanBadRequestException("startDate and endDate are required");
        }
        if (end.isBefore(start)) {
            throw new BekbolatovZholamanBadRequestException("endDate must be >= startDate");
        }
        if (start.isBefore(LocalDate.now())) {
            throw new BekbolatovZholamanBadRequestException("startDate must be today or in the future");
        }
    }
}

