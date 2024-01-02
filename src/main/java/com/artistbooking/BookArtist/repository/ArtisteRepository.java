package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Artiste;
import com.artistbooking.BookArtist.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtisteRepository extends JpaRepository<Artiste,Long> {

    Optional<Artiste> findByName(String name);
}
