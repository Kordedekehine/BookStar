package com.artistbooking.BookArtist.emailService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class EmailResponse {

    private String message;

    private String status;
}
