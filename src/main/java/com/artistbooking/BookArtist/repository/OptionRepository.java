package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option,Long> {

}
