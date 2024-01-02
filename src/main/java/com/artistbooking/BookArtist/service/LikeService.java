package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.exception.GeneralServiceException;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedAccessException;
import com.artistbooking.BookArtist.exception.UserNotFoundException;

public interface LikeService {

    String likePost(Long postId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException;

    String unlikePost(Long postId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException;

    String likeComment(Long commentId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException;

    String unlikeComment(Long commentId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException;
}
