package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.dPayload.request.ManagerLoginDto;
import com.artistbooking.BookArtist.dPayload.request.ManagerRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.response.ManagerResponseDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.model.Manager;
import com.artistbooking.BookArtist.repository.ManagerRepository;
import com.artistbooking.BookArtist.service.ManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private HttpSession session;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ManagerResponseDto createManager(ManagerRequestDto managerRequestDto) throws ResourceFoundException, PasswordMismatchException, ManagerNotFoundException {

        Optional<Manager> optionalManager = managerRepository.findByEmail(managerRequestDto.getEmail());
        if (optionalManager.isPresent())
        {
            //LOOK VERY WELL--already exists
            throw new ManagerNotFoundException();
        }

        Optional<Manager> optionalUsername = managerRepository.findByName(managerRequestDto.getName());
        if (optionalUsername.isPresent())
        {
            throw new ManagerNotFoundException();
        }

        if (!managerRequestDto.getPassword().equals(managerRequestDto.getConfirmPassword())){
            throw new PasswordMismatchException();
        }

        Manager manager = new Manager();
        manager.setName(managerRequestDto.getName());
        manager.setEmail(managerRequestDto.getEmail());
        manager.setAddress(managerRequestDto.getAddress());
        manager.setPassword(passwordEncoder.encode(managerRequestDto.getPassword()));
        manager.setCreatedOn(LocalDateTime.now());
        manager.setCreatedOn(LocalDateTime.now());



        managerRepository.save(manager);

        modelMapper.map(manager,managerRequestDto);

       ManagerResponseDto managerResponseDto = new ManagerResponseDto();

        modelMapper.map(managerRequestDto,managerResponseDto);

        return managerResponseDto;
    }

    @Override
    public ManagerResponseDto login(ManagerLoginDto managerLoginDto, HttpServletRequest request) throws NotFoundException, IncorrectPasswordException, ManagerNotFoundException {

        Optional<Manager> optionalManager = managerRepository.findByEmail(managerLoginDto.getEmail());
        if (optionalManager.isEmpty())
        {
            throw new ManagerNotFoundException();
        }

        Manager manager = optionalManager.get();

        if (!passwordEncoder.matches(managerLoginDto.getPassword(), manager.getPassword())){
            throw new IncorrectPasswordException();
        }

        session = request.getSession();
        session.setAttribute("managerid",manager.getId());

        ManagerResponseDto managerResponseDto = new ManagerResponseDto();

        modelMapper.map(manager,managerResponseDto);

        return managerResponseDto;
    }

    @Override
    public String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws NotFoundException, IncorrectPasswordException, PasswordMismatchException, RestrictedToManagerException, ManagerNotFoundException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty())
        {
            throw new ManagerNotFoundException();
        }

        Manager manager = optionalManager.get();

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), manager.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmNewPassword())) {
            throw new PasswordMismatchException();
        }

        manager.setUpdatedOn(LocalDateTime.now());
        manager.setPassword(passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        managerRepository.save(manager);

        return "Password Updated!";
    }

    @Override
    public boolean isManager(Long managerId) {

        if (managerId != null) {

            Optional<Manager> optionalAdmin = managerRepository.findById(managerId);
            return optionalAdmin.isPresent();
        }


        Long loggedInManagerId = (Long) session.getAttribute("managerid");


        if (loggedInManagerId != null) {

            Optional<Manager> optionalManager = managerRepository.findById(loggedInManagerId);
            return optionalManager.isPresent();
        }

        return false;
    }
}
