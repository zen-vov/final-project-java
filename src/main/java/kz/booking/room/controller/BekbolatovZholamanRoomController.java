package kz.booking.room.controller;

import jakarta.validation.Valid;
import kz.booking.room.dto.BekbolatovZholamanCreateRoomRequest;
import kz.booking.room.dto.BekbolatovZholamanRoomResponse;
import kz.booking.room.dto.BekbolatovZholamanUpdateRoomRequest;
import kz.booking.room.mapper.BekbolatovZholamanRoomMapper;
import kz.booking.room.service.BekbolatovZholamanRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/rooms")
public class BekbolatovZholamanRoomController {
    private final BekbolatovZholamanRoomService roomService;

    public BekbolatovZholamanRoomController(BekbolatovZholamanRoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BekbolatovZholamanRoomResponse create(@Valid @RequestBody BekbolatovZholamanCreateRoomRequest req) {
        return BekbolatovZholamanRoomMapper.toResponse(roomService.create(req));
    }

    @GetMapping("/{id}")
    public BekbolatovZholamanRoomResponse get(@PathVariable Long id) {
        return BekbolatovZholamanRoomMapper.toResponse(roomService.getById(id));
    }

    @GetMapping
    public Page<BekbolatovZholamanRoomResponse> list(
            @RequestParam(required = false) Long hotelId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return roomService.list(hotelId, pageable).map(BekbolatovZholamanRoomMapper::toResponse);
    }

    @PutMapping("/{id}")
    public BekbolatovZholamanRoomResponse update(@PathVariable Long id, @Valid @RequestBody BekbolatovZholamanUpdateRoomRequest req) {
        return BekbolatovZholamanRoomMapper.toResponse(roomService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roomService.delete(id);
    }
}

