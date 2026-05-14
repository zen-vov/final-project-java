package kz.booking.user.service;

import kz.booking.common.exception.BekbolatovZholamanForbiddenException;
import kz.booking.common.exception.BekbolatovZholamanNotFoundException;
import kz.booking.common.security.BekbolatovZholamanSecurityUtils;
import kz.booking.user.entity.BekbolatovZholamanUser;
import kz.booking.user.repo.BekbolatovZholamanUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BekbolatovZholamanUserService {
    private final BekbolatovZholamanUserRepository userRepository;

    public BekbolatovZholamanUserService(BekbolatovZholamanUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public BekbolatovZholamanUser getCurrentUser() {
        String email = BekbolatovZholamanSecurityUtils.currentEmailOrNull();
        if (email == null) {
            throw new BekbolatovZholamanForbiddenException("Unauthorized");
        }
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BekbolatovZholamanNotFoundException("User not found: email=" + email));
    }
}

