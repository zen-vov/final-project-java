package kz.booking.hotel.controller;

import jakarta.validation.Valid;
import kz.booking.hotel.dto.BekbolatovZholamanCreateHotelRequest;
import kz.booking.hotel.dto.BekbolatovZholamanHotelResponse;
import kz.booking.hotel.dto.BekbolatovZholamanUpdateHotelRequest;
import kz.booking.hotel.mapper.BekbolatovZholamanHotelMapper;
import kz.booking.hotel.service.BekbolatovZholamanHotelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@RestController
@RequestMapping("/api/hotels")
public class BekbolatovZholamanHotelController {
    private final BekbolatovZholamanHotelService hotelService;

    public BekbolatovZholamanHotelController(BekbolatovZholamanHotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BekbolatovZholamanHotelResponse create(@Valid @RequestBody BekbolatovZholamanCreateHotelRequest req) {
        return BekbolatovZholamanHotelMapper.toResponse(hotelService.create(req));
    }

    @GetMapping("/{id}")
    public BekbolatovZholamanHotelResponse get(@PathVariable Long id) {
        return BekbolatovZholamanHotelMapper.toResponse(hotelService.getById(id));
    }

    @GetMapping
    public Page<BekbolatovZholamanHotelResponse> list(
            @RequestParam(required = false) String city,
            @RequestParam(required = false, name = "q") String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return hotelService.list(city, q, pageable).map(BekbolatovZholamanHotelMapper::toResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BekbolatovZholamanHotelResponse update(@PathVariable Long id, @Valid @RequestBody BekbolatovZholamanUpdateHotelRequest req) {
        return BekbolatovZholamanHotelMapper.toResponse(hotelService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        hotelService.delete(id);
    }
}
