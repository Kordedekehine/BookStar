package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.exception.GeneralServiceException;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedAccessException;
import com.artistbooking.BookArtist.exception.UserNotFoundException;
import com.artistbooking.BookArtist.model.Comment;
import com.artistbooking.BookArtist.model.Likes;
import com.artistbooking.BookArtist.model.Post;
import com.artistbooking.BookArtist.model.UserEntity;
import com.artistbooking.BookArtist.repository.CommentRepository;
import com.artistbooking.BookArtist.repository.LikeRepository;
import com.artistbooking.BookArtist.repository.PostRepository;
import com.artistbooking.BookArtist.repository.UserRepository;
import com.artistbooking.BookArtist.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * ONLY USERS CAN LIKE TO AVOID THE MANAGER DISPLAYING SENTIMENTS OR MISTAKENLY LIKING A TROLL COMMENT OR SUMTIN
 */

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public String likePost(Long postId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        Long userId = (Long) session.getAttribute("userid");
        System.out.println("userId: " + userId);
        if (userId == null) {
            throw new RestrictedAccessException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();

        // Retrieve the post from the database
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException();
        }
        Post post = optionalPost.get();

        // Check if the user has already liked the post
        if (likeRepository.existsByPostAndUser(post, user)) {
            throw new GeneralServiceException("User already liked the post");
        }

        Likes like = new Likes();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);


        if (post.getLikeCount() == null) {
            post.setLikeCount(0);
        }
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return "You have liked the post";
    }

    @Override
    public String unlikePost(Long postId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        Long userId = (Long) session.getAttribute("userid");
        System.out.println("userId: " + userId);
        if (userId == null) {
            throw new RestrictedAccessException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();


        // Retrieve the post from the database
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new NotFoundException();
        }
        Post post = optionalPost.get();


        Likes like = likeRepository.findByPostAndUser(post, user);
        if (like == null) {
            throw new GeneralServiceException("User is yet to like the post");
        }

        likeRepository.delete(like);

        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
        return "you have successfully unliked the post";
    }

    @Override
    public String likeComment(Long commentId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        Long userId = (Long) session.getAttribute("userid");
        System.out.println("userId: " + userId);
        if (userId == null) {
            throw new RestrictedAccessException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();

        // Retrieve the post from the database
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NotFoundException();
        }
        Comment comment = optionalComment.get();

        // Check if the user has already liked the post
        if (likeRepository.existsByCommentAndUser(comment, user)) {
            throw new GeneralServiceException("User already liked the comment");
        }

        Likes like = new Likes();
        like.setComment(comment);
        like.setUser(user);
        likeRepository.save(like);


        if (comment.getLikeCount() == null) {
            comment.setLikeCount(0);
        }
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);

        return "You have liked the comment";
    }

    @Override
    public String unlikeComment(Long commentId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        Long userId = (Long) session.getAttribute("userid");
        System.out.println("userId: " + userId);
        if (userId == null) {
            throw new RestrictedAccessException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();


        // Retrieve the post from the database
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NotFoundException();
        }
        Comment comment = optionalComment.get();


        Likes like = likeRepository.findByCommentAndUser(comment, user);
        if (like == null) {
            throw new GeneralServiceException("User is yet to like the comment");
        }

        likeRepository.delete(like);

        comment.setLikeCount(comment.getLikeCount() - 1);
        commentRepository.save(comment);
        return "you have successfully unliked the comment";
    }


}
