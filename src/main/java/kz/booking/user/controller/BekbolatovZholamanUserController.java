package kz.booking.user.controller;

import kz.booking.user.dto.BekbolatovZholamanUserResponse;
import kz.booking.user.mapper.BekbolatovZholamanUserMapper;
import kz.booking.user.service.BekbolatovZholamanUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class BekbolatovZholamanUserController {
    private final BekbolatovZholamanUserService userService;

    public BekbolatovZholamanUserController(BekbolatovZholamanUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public BekbolatovZholamanUserResponse me() {
        return BekbolatovZholamanUserMapper.toResponse(userService.getCurrentUser());
    }
}

