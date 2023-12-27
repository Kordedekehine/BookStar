package com.artistbooking.BookArtist.model;

import com.artistbooking.BookArtist.enums.Role;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "user_name",nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "address", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;


    @CreationTimestamp
    @Column(name = "created_on", updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private String email;
}
