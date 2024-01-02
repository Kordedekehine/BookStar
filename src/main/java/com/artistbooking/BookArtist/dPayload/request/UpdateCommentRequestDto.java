package com.artistbooking.BookArtist.dPayload.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCommentRequestDto {

    private String text;
}
