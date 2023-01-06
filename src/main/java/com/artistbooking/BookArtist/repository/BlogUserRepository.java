package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, Long> {

    BlogUser findUserByEmail(String email);
    BlogUser findByUsername(String username);

}
