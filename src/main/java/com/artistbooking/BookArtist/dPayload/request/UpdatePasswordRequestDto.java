package com.artistbooking.BookArtist.dPayload.request;

import lombok.*;

import javax.persistence.Entity;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordRequestDto {

    private String oldPassword;
    private String newPassword;

    private String confirmNewPassword;
}
