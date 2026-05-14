package kz.booking.user.repo;

import kz.booking.user.entity.BekbolatovZholamanRole;
import kz.booking.user.entity.BekbolatovZholamanRoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BekbolatovZholamanRoleRepository extends JpaRepository<BekbolatovZholamanRole, Long> {
    Optional<BekbolatovZholamanRole> findByName(BekbolatovZholamanRoleName name);
}

