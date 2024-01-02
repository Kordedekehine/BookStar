package com.artistbooking.BookArtist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @NotBlank
    private String text;

    private int voteCount;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

}
