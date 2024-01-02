package com.artistbooking.BookArtist.dPayload.request;

import lombok.*;


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
    private String password;
}
