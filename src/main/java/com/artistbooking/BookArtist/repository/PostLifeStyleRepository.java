package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.PostLifeStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLifeStyleRepository extends JpaRepository<PostLifeStyle,Long> {

}
