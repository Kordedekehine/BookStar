package com.artistbooking.BookArtist.serviceImpl;


import com.artistbooking.BookArtist.model.Comment;
import com.artistbooking.BookArtist.repository.CommentRepository;
import com.artistbooking.BookArtist.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
