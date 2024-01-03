package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote,Long> {

    boolean existsByUserIdAndPollId(Long userId, Long pollId);
    Optional<Vote> findByUserIdAndPollId(Long userId, Long pollId);
}
