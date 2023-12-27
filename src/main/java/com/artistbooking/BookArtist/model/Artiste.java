package com.artistbooking.BookArtist.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Artiste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "artiste_name",nullable = false)
    private String name;

    @Column(nullable = false)
    private String aboutStar;

    @Column(name = "address", nullable = false)
    private String address;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "artiste_id", referencedColumnName = "Id")
    private Handles handles;

}
