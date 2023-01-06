package com.artistbooking.BookArtist.serviceImpl;


import com.artistbooking.BookArtist.model.Post;
import com.artistbooking.BookArtist.repository.PostRepository;
import com.artistbooking.BookArtist.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Collection<Post> getAll() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }

    @Override
    public Post save(Post post) {
        return postRepository.saveAndFlush(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
