package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.PostLifeStyleRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UpdatePostRequestDto;
import com.artistbooking.BookArtist.dPayload.response.PostLifeStyleResponseDto;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedToManagerException;
import com.artistbooking.BookArtist.model.Post;

import java.util.List;

public interface PostService {

    PostLifeStyleResponseDto postLifeStyle(PostLifeStyleRequestDto postLifeStyleRequestDto) throws NotFoundException, RestrictedToManagerException;

    PostLifeStyleResponseDto updateLifeStylePost(Long id, UpdatePostRequestDto updatePostRequestDto) throws NotFoundException, RestrictedToManagerException;

    String deletePostLifeStyle(Long id) throws NotFoundException, RestrictedToManagerException;

    PostLifeStyleResponseDto getPostById(Long id) throws NotFoundException;

    List<Post> getAllPost();
}
