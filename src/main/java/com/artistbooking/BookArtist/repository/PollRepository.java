package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Poll;
import com.artistbooking.BookArtist.model.Post;
import com.artistbooking.BookArtist.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends JpaRepository<Poll,Long> {

    //boolean existsByPollAndUser(Poll poll, UserEntity user);


}
