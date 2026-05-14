package kz.booking.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import kz.booking.common.entity.BekbolatovZholamanBaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class BekbolatovZholamanUser extends BekbolatovZholamanBaseEntity {
    @Column(nullable = false, unique = true, length = 64)
    private String email;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 200)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<BekbolatovZholamanRole> roles = new HashSet<>();

    protected BekbolatovZholamanUser() {
    }

    public BekbolatovZholamanUser(String email, String fullName, String passwordHash) {
        this.email = email;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<BekbolatovZholamanRole> getRoles() {
        return roles;
    }
}

