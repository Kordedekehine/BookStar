package com.artistbooking.BookArtist.dPayload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequestDto {

    private String content;

    private String description;
}
