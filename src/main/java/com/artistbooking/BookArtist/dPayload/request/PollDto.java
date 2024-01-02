package com.artistbooking.BookArtist.dPayload.request;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PollDto {

    private String question;
    private LocalDateTime createdAt;
    private List<OptionDto> options;

}
