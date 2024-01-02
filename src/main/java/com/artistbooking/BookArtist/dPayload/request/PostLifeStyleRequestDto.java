package com.artistbooking.BookArtist.dPayload.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLifeStyleRequestDto {


    private String content;

    private String description;
}
