package com.artistbooking.BookArtist.service;


import com.artistbooking.BookArtist.model.Comment;

public interface CommentService {

    Comment save(Comment comment);

    void delete(Comment comment);

}
