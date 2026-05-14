package kz.booking.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kz.booking.common.entity.BekbolatovZholamanBaseEntity;

@Entity
@Table(name = "roles")
public class BekbolatovZholamanRole extends BekbolatovZholamanBaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 32)
    private BekbolatovZholamanRoleName name;

    protected BekbolatovZholamanRole() {
    }

    public BekbolatovZholamanRole(BekbolatovZholamanRoleName name) {
        this.name = name;
    }

    public BekbolatovZholamanRoleName getName() {
        return name;
    }
}

