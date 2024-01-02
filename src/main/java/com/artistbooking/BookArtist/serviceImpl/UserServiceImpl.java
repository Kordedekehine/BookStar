package com.artistbooking.BookArtist.serviceImpl;


import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UserLoginDto;
import com.artistbooking.BookArtist.dPayload.request.UserRequestDto;
import com.artistbooking.BookArtist.dPayload.response.UserResponseDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.model.UserEntity;
import com.artistbooking.BookArtist.repository.UserRepository;
import com.artistbooking.BookArtist.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceFoundException, PasswordMismatchException, UserNotFoundException {

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userRequestDto.getEmail());
        if (optionalUser.isPresent())
        {
            //LOOK VERY WELL--already exists
            throw new UserNotFoundException();
        }

        Optional<UserEntity> optionalUsername = userRepository.findByName(userRequestDto.getName());
        if (optionalUsername.isPresent())
        {
            throw new ResourceFoundException();
        }

     if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())){
         throw new PasswordMismatchException();
     }

        UserEntity user = new UserEntity();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setAddress(userRequestDto.getAddress());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setCreatedOn(LocalDateTime.now());
        user.setUpdatedOn(LocalDateTime.now());


        userRepository.save(user);

        modelMapper.map(user,userRequestDto);

        UserResponseDto userResponseDto = new UserResponseDto();

        modelMapper.map(userRequestDto,userResponseDto);

        return userResponseDto;

    }

    @Override
    public UserResponseDto login(UserLoginDto userLoginDto, HttpServletRequest request) throws NotFoundException, IncorrectPasswordException, UserNotFoundException {

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userLoginDto.getEmail());
        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())){
            throw new IncorrectPasswordException();
        }

        session = request.getSession();
        session.setAttribute("userid",user.getId());

        UserResponseDto userLoginResponseDto = new UserResponseDto();

        modelMapper.map(user,userLoginResponseDto);

        return userLoginResponseDto;
    }

    @Override
    public String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws NotFoundException, IncorrectPasswordException, PasswordMismatchException, RestrictedAccessException, UserNotFoundException {

        Long userid = (Long) session.getAttribute("userid");
        if (userid == null) {
            throw new RestrictedAccessException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userid);
        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmNewPassword())) {
            throw new PasswordMismatchException();
        }

        user.setUpdatedOn(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        userRepository.save(user);

        return "Password Updated!";
    }

}
