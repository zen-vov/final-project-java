package kz.booking.security;

import kz.booking.user.entity.BekbolatovZholamanUser;
import kz.booking.user.repo.BekbolatovZholamanUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BekbolatovZholamanUserDetailsService implements UserDetailsService {
    private final BekbolatovZholamanUserRepository userRepository;

    public BekbolatovZholamanUserDetailsService(BekbolatovZholamanUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BekbolatovZholamanUser user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> auth = user.getRoles()
                .stream()
                .map(r -> (GrantedAuthority) new SimpleGrantedAuthority(r.getName().name()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .disabled(!user.isEnabled())
                .authorities(auth)
                .build();
    }
}
