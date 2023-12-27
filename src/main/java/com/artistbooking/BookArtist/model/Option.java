package com.artistbooking.BookArtist.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private int voteCount;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;
}
