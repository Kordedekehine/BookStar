package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

     Optional<UserEntity> findByEmail(String email);

     Optional<UserEntity> findByName(String email);
}
