package com.artistbooking.BookArtist.repository;

import com.artistbooking.BookArtist.model.Comment;
import com.artistbooking.BookArtist.model.Likes;
import com.artistbooking.BookArtist.model.PostLifeStyle;
import com.artistbooking.BookArtist.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
//    boolean existsByPostLifeStyleAndUser(PostLifeStyle postLifeStyle, UserEntity userEntity);
//    boolean existsByCommentAndUser(Comment comment, UserEntity user);
//    Likes findByCommentAndUser(Comment comment, UserEntity user);
//    Likes findByPostLifeStyleAndUser(PostLifeStyle postLifeStyle, UserEntity userEntity);


}
