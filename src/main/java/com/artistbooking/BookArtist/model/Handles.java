package com.artistbooking.BookArtist.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Handles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String instagramHandle;

    private String twitterHandle;

    private String snapChatHandle;

    private String tiktokHandle;

    private String clubHouseHandle;

    private String faceBookHandle;

    @ManyToMany(mappedBy = "handles")
    private Set<Artiste> artistes = new HashSet<>();
}
