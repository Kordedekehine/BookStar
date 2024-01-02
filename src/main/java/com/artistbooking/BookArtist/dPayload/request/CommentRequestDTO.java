package com.artistbooking.BookArtist.dPayload.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequestDTO {
    private String text;
}
