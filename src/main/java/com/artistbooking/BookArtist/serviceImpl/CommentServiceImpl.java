package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.dPayload.request.CommentRequestDTO;
import com.artistbooking.BookArtist.dPayload.request.UpdateCommentRequestDto;
import com.artistbooking.BookArtist.dPayload.response.CommentResponseDTO;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.model.Comment;
import com.artistbooking.BookArtist.model.Manager;
import com.artistbooking.BookArtist.model.Post;
import com.artistbooking.BookArtist.model.UserEntity;
import com.artistbooking.BookArtist.repository.CommentRepository;
import com.artistbooking.BookArtist.repository.ManagerRepository;
import com.artistbooking.BookArtist.repository.PostRepository;
import com.artistbooking.BookArtist.repository.UserRepository;
import com.artistbooking.BookArtist.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private  ManagerServiceImpl managerService;

    @Autowired
    private ManagerRepository managerRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HttpSession session;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO, Long postId, HttpSession session) throws NotFoundException {

        Long managerId = (Long) session.getAttribute("managerid");
        Long userId = (Long) session.getAttribute("userid");

        if (managerId == null && userId == null) {
            throw new NotFoundException();
        }

        // Check if the manager is logged in
        if (managerId != null) {
            Optional<Manager> optionalManager = managerRepository.findById(managerId);
            if (optionalManager.isEmpty()) {
                throw new NotFoundException();
            }
            // Manager is logged in, proceed with creating the comment
        }

        // Check if the user is logged in
        if (userId != null) {
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new NotFoundException();
            }
            // User is logged in, proceed with creating the comment
        }

        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isEmpty()) {
            throw new NotFoundException();
        }

        Post post = optionalPost.get();

        if (commentRequestDTO.getText().length() < 1) {
            throw new RuntimeException("Text cannot be empty.");
        }

        Comment comment = new Comment();
        comment.setText(commentRequestDTO.getText());
        comment.setPost(post);
        comment.setCreatedOn(LocalDateTime.now());

        // Set the user or manager based on who is logged in
        if (managerId != null) {
            comment.setManager(managerRepository.findById(managerId).orElseThrow(() -> new NotFoundException()));
        } else if (userId != null) {
            comment.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException()));
        }

        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .text(commentRequestDTO.getText())
                .createdAt(LocalDateTime.now())
                .postId(post.getId())
                .build();
    }

    @Override
    public CommentResponseDTO updateComment(Long commentId,UpdateCommentRequestDto updateCommentRequestDto) throws NotFoundException, RestrictedAccessException, GeneralServiceException {

        // Check if a user or manager is logged in
        Long userId = (Long) session.getAttribute("userid");
        Long managerId = (Long) session.getAttribute("managerid");

        if (userId == null && managerId == null) {
            throw new RestrictedAccessException();
        }

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralServiceException("Comment not found"));

        // Log the user and manager IDs for debugging
        System.out.println("userId: " + userId);
        System.out.println("managerId: " + managerId);
        System.out.println("existingComment.getUser().getId(): " + existingComment.getUser().getId());
        System.out.println("existingComment.getManager(): " + existingComment.getManager());


        // Check if the user or manager has permission to update the comment
        if (!existingComment.getUser().getId().equals(userId) || !managerService.isManager(managerId)) {
            throw new RestrictedAccessException();
        }

            existingComment.setText(updateCommentRequestDto.getText());
            existingComment.setUpdatedOn(LocalDateTime.now());

            commentRepository.save(existingComment);

            CommentResponseDTO commentResponseDTO = new CommentResponseDTO();

            modelMapper.map(existingComment, commentResponseDTO);

            return commentResponseDTO;
        }


    @Override
    public String deleteComment(Long commentId) throws NotFoundException, RestrictedAccessException, GeneralServiceException {

        Long managerId = (Long) session.getAttribute("managerid");

        Long userId = (Long) session.getAttribute("userid");

        if (managerId == null && userId == null) {
            throw new RestrictedAccessException();
        }

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralServiceException("Comment not found"));


        // Check if the user or manager has permission to delete the comment
        if (userId != null && !Objects.equals(userId, existingComment.getUser().getId())) {
            throw new RestrictedAccessException();
        }

        if (managerId != null && (existingComment.getManager() == null || !Objects.equals(managerId, existingComment.getManager().getId()))) {
            throw new RestrictedAccessException();
        }

        commentRepository.delete(existingComment);

        return "Comment deleted!";

    }

    @Override
    public CommentResponseDTO getCommentById(Long commentId) throws NotFoundException, RestrictedAccessException {

        Long managerId = (Long) session.getAttribute("managerid");

        Long userId = (Long) session.getAttribute("userid");

        if (managerId == null && userId == null) {
            throw new NotFoundException();
        }

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()){
            throw new NotFoundException();
        }

        Comment comment = optionalComment.get();

        // Check if the manager is logged in
        if (managerId != null) {
            Optional<Manager> optionalManager = managerRepository.findById(managerId);
            if (optionalManager.isEmpty()) {
                throw new NotFoundException();
            }
            // Manager is logged in, proceed with creating the comment
        }

        // Check if the user is logged in
        if (userId != null) {
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new NotFoundException();
            }
            // User is logged in, proceed with creating the comment
        }

        // Set the user or manager based on who is logged in
        if (managerId != null) {
            comment.setManager(managerRepository.findById(managerId).orElseThrow(() -> new NotFoundException()));
        } else if (userId != null) {
            comment.setUser(userRepository.findById(userId).orElseThrow(() -> new NotFoundException()));
        }

     return CommentResponseDTO.builder()
                .text(comment.getText())
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedOn())
                .updatedAt(comment.getUpdatedOn()).build();

    }

    @Override
    public List<CommentResponseDTO> getCommentByPostId(Long postId) throws NotFoundException, RestrictedAccessException, ManagerNotFoundException, UserNotFoundException {

        Long managerId = (Long) session.getAttribute("managerid");

        Long userId = (Long) session.getAttribute("userid");

        if (managerId == null && userId == null) {
            throw new NotFoundException();
        }

        // Check if the manager is logged in
        if (managerId != null) {
            Optional<Manager> optionalManager = managerRepository.findById(managerId);
            if (optionalManager.isEmpty()) {
                throw new ManagerNotFoundException();
            }
            // Manager is logged in, proceed with creating the comment
        }

        // Check if the user is logged in
        if (userId != null) {
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException();
            }
            // User is logged in, proceed with creating the comment
        }

        List<Comment> comments = commentRepository.findByPostId(postId);

        return  comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .commentId(comment.getId())
                        .text(comment.getText())
                        .createdAt(comment.getCreatedOn())
                        .updatedAt(comment.getUpdatedOn())
                        .postId(comment.getPost().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
