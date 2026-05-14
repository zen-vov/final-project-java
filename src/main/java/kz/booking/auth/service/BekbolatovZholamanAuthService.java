package kz.booking.auth.service;

import kz.booking.auth.dto.BekbolatovZholamanAuthRequest;
import kz.booking.auth.dto.BekbolatovZholamanAuthResponse;
import kz.booking.auth.dto.BekbolatovZholamanRegisterRequest;
import kz.booking.common.exception.BekbolatovZholamanBadRequestException;
import kz.booking.security.BekbolatovZholamanJwtService;
import kz.booking.user.entity.BekbolatovZholamanRole;
import kz.booking.user.entity.BekbolatovZholamanRoleName;
import kz.booking.user.entity.BekbolatovZholamanUser;
import kz.booking.user.repo.BekbolatovZholamanRoleRepository;
import kz.booking.user.repo.BekbolatovZholamanUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BekbolatovZholamanAuthService {
    private final BekbolatovZholamanUserRepository userRepository;
    private final BekbolatovZholamanRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BekbolatovZholamanJwtService jwtService;

    public BekbolatovZholamanAuthService(
            BekbolatovZholamanUserRepository userRepository,
            BekbolatovZholamanRoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            BekbolatovZholamanJwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public BekbolatovZholamanAuthResponse register(BekbolatovZholamanRegisterRequest req) {
        if (userRepository.existsByEmailIgnoreCase(req.getEmail())) {
            throw new BekbolatovZholamanBadRequestException("Email already registered");
        }

        BekbolatovZholamanRole userRole = roleRepository.findByName(BekbolatovZholamanRoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not initialized"));

        BekbolatovZholamanUser user = new BekbolatovZholamanUser(
                req.getEmail().trim().toLowerCase(),
                req.getFullName().trim(),
                passwordEncoder.encode(req.getPassword())
        );
        user.getRoles().add(userRole);
        userRepository.save(user);

        BekbolatovZholamanAuthRequest loginReq = new BekbolatovZholamanAuthRequest();
        loginReq.setEmail(req.getEmail());
        loginReq.setPassword(req.getPassword());
        return authenticate(loginReq);
    }

    @Transactional(readOnly = true)
    public BekbolatovZholamanAuthResponse authenticate(BekbolatovZholamanAuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        List<String> roles = auth.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        String token = jwtService.generateToken(auth.getName(), claims);

        return new BekbolatovZholamanAuthResponse(token, auth.getName(), roles);
    }
}
