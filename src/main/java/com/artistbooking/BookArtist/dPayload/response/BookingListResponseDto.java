package com.artistbooking.BookArtist.dPayload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingListResponseDto {

    private int sizeOfList;
    private List<BookingResponseDto> bookingResponseDtoList= new ArrayList<>();

}
