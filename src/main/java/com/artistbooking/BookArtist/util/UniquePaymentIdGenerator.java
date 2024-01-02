package com.artistbooking.BookArtist.util;

import java.util.UUID;

public class UniquePaymentIdGenerator {
    public static String generateId(){
        UUID id=UUID.randomUUID();
        return id.toString();
    }
}
