package com.artistbooking.BookArtist.dPayload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String name;
    private String email;
    private String address;
    private String password;
}
