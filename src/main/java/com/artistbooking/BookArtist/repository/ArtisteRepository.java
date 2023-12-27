package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Artiste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtisteRepository extends JpaRepository<Artiste,Long> {

}
