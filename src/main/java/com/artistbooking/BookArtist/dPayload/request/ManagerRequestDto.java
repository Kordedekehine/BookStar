package com.artistbooking.BookArtist.dPayload.request;

import com.artistbooking.BookArtist.enums.Role;
import lombok.*;

import javax.persistence.Entity;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerRequestDto {

    private String name;
    private String email;
    private String confirmPassword;
    private String address;
    private Role role;
    private String password;
}
