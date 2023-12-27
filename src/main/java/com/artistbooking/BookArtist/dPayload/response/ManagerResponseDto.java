package com.artistbooking.BookArtist.dPayload.response;

import com.artistbooking.BookArtist.enums.Role;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerResponseDto {

    private String name;
    private String email;
    private String address;
    private Role role;
    private String password;
}
