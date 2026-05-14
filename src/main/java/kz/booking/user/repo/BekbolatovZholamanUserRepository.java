package kz.booking.user.repo;

import kz.booking.user.entity.BekbolatovZholamanUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BekbolatovZholamanUserRepository extends JpaRepository<BekbolatovZholamanUser, Long> {
    Optional<BekbolatovZholamanUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}

