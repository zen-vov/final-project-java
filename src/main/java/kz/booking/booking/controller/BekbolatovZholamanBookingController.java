package kz.booking.booking.controller;

import jakarta.validation.Valid;
import kz.booking.booking.dto.BekbolatovZholamanBookingResponse;
import kz.booking.booking.dto.BekbolatovZholamanCreateBookingRequest;
import kz.booking.booking.dto.BekbolatovZholamanUpdateBookingRequest;
import kz.booking.booking.entity.BekbolatovZholamanBookingStatus;
import kz.booking.booking.mapper.BekbolatovZholamanBookingMapper;
import kz.booking.booking.service.BekbolatovZholamanBookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/bookings")
public class BekbolatovZholamanBookingController {
    private final BekbolatovZholamanBookingService bookingService;

    public BekbolatovZholamanBookingController(BekbolatovZholamanBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BekbolatovZholamanBookingResponse create(@Valid @RequestBody BekbolatovZholamanCreateBookingRequest req) {
        return BekbolatovZholamanBookingMapper.toResponse(bookingService.createForCurrentUser(req));
    }

    @GetMapping("/{id}")
    public BekbolatovZholamanBookingResponse get(@PathVariable Long id) {
        return BekbolatovZholamanBookingMapper.toResponse(bookingService.getByIdForCurrentUserOrAdmin(id));
    }

    // Pagination + sorting are supported by Pageable: ?page=0&size=10&sort=startDate,desc
    @GetMapping
    public Page<BekbolatovZholamanBookingResponse> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) BekbolatovZholamanBookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false, name = "q") String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return bookingService.searchForCurrentUserOrAdmin(userId, hotelId, roomId, status, from, to, q, pageable)
                .map(BekbolatovZholamanBookingMapper::toResponse);
    }

    @PutMapping("/{id}")
    public BekbolatovZholamanBookingResponse update(@PathVariable Long id, @Valid @RequestBody BekbolatovZholamanUpdateBookingRequest req) {
        return BekbolatovZholamanBookingMapper.toResponse(bookingService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookingService.delete(id);
    }
}

