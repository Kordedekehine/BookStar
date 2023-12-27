package com.artistbooking.BookArtist.serviceImpl;


import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UserLoginDto;
import com.artistbooking.BookArtist.dPayload.request.UserRequestDto;
import com.artistbooking.BookArtist.dPayload.response.UserResponseDto;
import com.artistbooking.BookArtist.enums.Role;
import com.artistbooking.BookArtist.exception.IncorrectPasswordException;
import com.artistbooking.BookArtist.exception.PasswordMismatchException;
import com.artistbooking.BookArtist.exception.ResourceFoundException;
import com.artistbooking.BookArtist.exception.ResourceNotFoundException;
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
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceFoundException, PasswordMismatchException {

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userRequestDto.getEmail());
        if (optionalUser.isPresent())
        {
            //LOOK VERY WELL--already exists
            throw new ResourceFoundException();
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
        user.setRole(Role.USER);


        userRepository.save(user);

        modelMapper.map(user,userRequestDto);

        UserResponseDto userResponseDto = new UserResponseDto();

        modelMapper.map(userRequestDto,userResponseDto);

        return userResponseDto;

    }

    @Override
    public UserResponseDto login(UserLoginDto userLoginDto, HttpServletRequest request) throws ResourceNotFoundException, IncorrectPasswordException {

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userLoginDto.getEmail());
        if (optionalUser.isEmpty())
        {
            throw new ResourceNotFoundException();
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
    public String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws ResourceNotFoundException, IncorrectPasswordException, PasswordMismatchException {

        Long userid = (Long) session.getAttribute("userid");
        if (userid == null) {
            throw new ResourceNotFoundException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userid);
        if (optionalUser.isEmpty())
        {
            throw new ResourceNotFoundException();
        }

        UserEntity user = optionalUser.get();

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmNewPassword())) {
            throw new PasswordMismatchException();
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        userRepository.save(user);

        return "Password Updated!";
    }

}
