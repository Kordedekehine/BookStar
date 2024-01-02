package com.artistbooking.BookArtist.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(nullable = false, length = 5000000)
    private byte[] image;

    @Column(nullable = false)
    private String aboutStar;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "just_appearance_fee",nullable = false )
    private String currentAppearanceCharges;

    @Column(name = "performance_fee",nullable = false )
    private String currentPerformanceCharges;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp
    @Column(name = "created_on",nullable = false)
    private LocalDateTime createdOn;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @UpdateTimestamp
    @Column(name = "updated_on",nullable = false)
    private LocalDateTime updatedOn;

    @Embedded
    private Handles handles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;
}
