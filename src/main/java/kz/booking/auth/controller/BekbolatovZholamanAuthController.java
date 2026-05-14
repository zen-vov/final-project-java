package kz.booking.auth.controller;

import jakarta.validation.Valid;
import kz.booking.auth.dto.BekbolatovZholamanAuthRequest;
import kz.booking.auth.dto.BekbolatovZholamanAuthResponse;
import kz.booking.auth.dto.BekbolatovZholamanRegisterRequest;
import kz.booking.auth.service.BekbolatovZholamanAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class BekbolatovZholamanAuthController {
    private final BekbolatovZholamanAuthService authService;

    public BekbolatovZholamanAuthController(BekbolatovZholamanAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public BekbolatovZholamanAuthResponse register(@Valid @RequestBody BekbolatovZholamanRegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public BekbolatovZholamanAuthResponse login(@Valid @RequestBody BekbolatovZholamanAuthRequest req) {
        return authService.authenticate(req);
    }
}

