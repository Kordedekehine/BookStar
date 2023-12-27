package com.artistbooking.BookArtist.dPayload.request;


import lombok.*;

import javax.persistence.Entity;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerLoginDto {

    private String email;

    private String password;
}
