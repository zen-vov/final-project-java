package kz.booking.user.init;

import kz.booking.user.entity.BekbolatovZholamanRole;
import kz.booking.user.entity.BekbolatovZholamanRoleName;
import kz.booking.user.entity.BekbolatovZholamanUser;
import kz.booking.user.repo.BekbolatovZholamanRoleRepository;
import kz.booking.user.repo.BekbolatovZholamanUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BekbolatovZholamanDataInitializer {
    private static final Logger log = LoggerFactory.getLogger(BekbolatovZholamanDataInitializer.class);

    @Bean
    ApplicationRunner initRoles(
            BekbolatovZholamanRoleRepository roleRepository,
            BekbolatovZholamanUserRepository userRepository,
            PasswordEncoder encoder,
            @Value("${app.bootstrap.admin.email:admin@local}") String adminEmail,
            @Value("${app.bootstrap.admin.password:admin12345}") String adminPassword
    ) {
        return args -> {
            roleRepository.findByName(BekbolatovZholamanRoleName.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new BekbolatovZholamanRole(BekbolatovZholamanRoleName.ROLE_USER)));
            BekbolatovZholamanRole adminRole = roleRepository.findByName(BekbolatovZholamanRoleName.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new BekbolatovZholamanRole(BekbolatovZholamanRoleName.ROLE_ADMIN)));

            if (!userRepository.existsByEmailIgnoreCase(adminEmail)) {
                BekbolatovZholamanUser admin = new BekbolatovZholamanUser(adminEmail, "Admin", encoder.encode(adminPassword));
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
                log.warn("Bootstrapped admin user email={} password={}", adminEmail, adminPassword);
            }
        };
    }
}

