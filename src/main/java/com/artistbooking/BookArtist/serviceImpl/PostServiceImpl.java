package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.dPayload.request.PostLifeStyleRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UpdatePostRequestDto;
import com.artistbooking.BookArtist.dPayload.response.PostLifeStyleResponseDto;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedToManagerException;
import com.artistbooking.BookArtist.model.Manager;
import com.artistbooking.BookArtist.model.Post;
import com.artistbooking.BookArtist.repository.ManagerRepository;
import com.artistbooking.BookArtist.repository.PostRepository;
import com.artistbooking.BookArtist.service.ManagerService;
import com.artistbooking.BookArtist.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private HttpSession session;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostLifeStyleResponseDto postLifeStyle(PostLifeStyleRequestDto postLifeStyleRequestDto) throws NotFoundException, RestrictedToManagerException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty())
        {
            throw new RestrictedToManagerException();
        }

        Post post = new Post();

        post.setContent(postLifeStyleRequestDto.getContent());
        post.setDescription(postLifeStyleRequestDto.getDescription());
        post.setManager(optionalManager.get());
        post.setCreatedOn(LocalDateTime.now());

        postRepository.save(post);

        modelMapper.map(post,postLifeStyleRequestDto);

        PostLifeStyleResponseDto postLifeStyleResponseDto = new PostLifeStyleResponseDto();

        modelMapper.map(postLifeStyleRequestDto,postLifeStyleResponseDto);

        return postLifeStyleResponseDto;
    }

    @Override
    public PostLifeStyleResponseDto updateLifeStylePost(Long id, UpdatePostRequestDto updatePostRequestDto) throws NotFoundException, RestrictedToManagerException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty())
        {
            throw new RestrictedToManagerException();
        }

        Optional<Post> lifeStyleOptional = postRepository.findById(id);

        if (lifeStyleOptional.isEmpty()){
            throw new NotFoundException();
        }

        Post post = lifeStyleOptional.get();
        post.setContent(updatePostRequestDto.getContent());
        post.setDescription(updatePostRequestDto.getDescription());
        post.setManager(optionalManager.get());
        post.setUpdatedOn(LocalDateTime.now());

        postRepository.save(post);

        modelMapper.map(post,updatePostRequestDto);

        PostLifeStyleResponseDto postLifeStyleResponseDto = new PostLifeStyleResponseDto();

        modelMapper.map(updatePostRequestDto,postLifeStyleResponseDto);

        return postLifeStyleResponseDto;
    }

    @Override
    public String deletePostLifeStyle(Long id) throws NotFoundException, RestrictedToManagerException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty())
        {
            throw new RestrictedToManagerException();
        }

        Optional<Post> lifeStyleOptional = postRepository.findById(id);

        if (lifeStyleOptional.isEmpty()){
            throw new NotFoundException();
        }

        Post post = lifeStyleOptional.get();

        postRepository.delete(post);

        return "Post Deleted!";
    }

    @Override
    public PostLifeStyleResponseDto getPostById(Long id) throws NotFoundException {

        Optional<Post> lifeStyleOptional = postRepository.findById(id);

        if (lifeStyleOptional.isEmpty()){
            throw new NotFoundException();
        }

        Post post = lifeStyleOptional.get();

        PostLifeStyleResponseDto postLifeStyleResponseDto = new PostLifeStyleResponseDto();

        modelMapper.map(post,postLifeStyleResponseDto);

        return postLifeStyleResponseDto;
    }

    @Override
    public List<Post> getAllPost() {

        return postRepository.findAll();

    }




}
