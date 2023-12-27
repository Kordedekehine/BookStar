package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Manager;
import com.artistbooking.BookArtist.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    Optional<Manager> findByEmail(String email);

    Optional<Manager> findByName(String email);

}
