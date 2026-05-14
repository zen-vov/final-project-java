package kz.booking.user.mapper;

import kz.booking.user.dto.BekbolatovZholamanUserResponse;
import kz.booking.user.entity.BekbolatovZholamanUser;

public class BekbolatovZholamanUserMapper {
    private BekbolatovZholamanUserMapper() {
    }

    public static BekbolatovZholamanUserResponse toResponse(BekbolatovZholamanUser u) {
        BekbolatovZholamanUserResponse dto = new BekbolatovZholamanUserResponse();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setFullName(u.getFullName());
        dto.setRoles(u.getRoles().stream().map(r -> r.getName().name()).toList());
        return dto;
    }
}

