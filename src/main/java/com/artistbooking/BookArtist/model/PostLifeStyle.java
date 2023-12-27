package com.artistbooking.BookArtist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostLifeStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    @ToString.Exclude
    private Comment comment;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "likes_id")
    @ToString.Exclude
    private Likes likes;
}
