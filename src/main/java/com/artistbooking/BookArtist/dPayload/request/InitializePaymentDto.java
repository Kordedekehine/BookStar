package com.artistbooking.BookArtist.dPayload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class InitializePaymentDto {

    @NotNull(message = "Amount cannot be null")
    @JsonProperty("amount")
    private String amount;

    @NotNull(message = "Email cannot be null")
    @JsonProperty("email")
    private String email;


    @NotNull(message = "Channels cannot be null")
    @JsonProperty("channels")
    private String[] channels;
}

