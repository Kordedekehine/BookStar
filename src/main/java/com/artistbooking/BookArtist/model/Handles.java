package com.artistbooking.BookArtist.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Handles {


    private String instagramHandle;

    private String twitterHandle;

    private String snapChatHandle;

    private String tiktokHandle;

    private String clubHouseHandle;

    private String faceBookHandle;

}
