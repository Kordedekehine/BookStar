package com.artistbooking.BookArtist.serviceImpl;

import com.artistbooking.BookArtist.dPayload.request.ArtisteDto;
import com.artistbooking.BookArtist.dPayload.response.ArtisteResponseDto;
import com.artistbooking.BookArtist.exception.DuplicateUsernameException;
import com.artistbooking.BookArtist.exception.ManagerNotFoundException;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedToManagerException;
import com.artistbooking.BookArtist.model.Artiste;
import com.artistbooking.BookArtist.model.Handles;
import com.artistbooking.BookArtist.model.Manager;
import com.artistbooking.BookArtist.repository.ArtisteRepository;
import com.artistbooking.BookArtist.repository.ManagerRepository;
import com.artistbooking.BookArtist.service.ArtisteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtisteServiceImpl implements ArtisteService {

    @Autowired
   private ManagerRepository managerRepository;

    @Autowired
    private ArtisteRepository artisteRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ArtisteResponseDto createArtisteProfile(ArtisteDto artisteDto,HttpSession session) throws RestrictedToManagerException, NotFoundException, DuplicateUsernameException, ManagerNotFoundException {

      Long managerId =  (Long) session.getAttribute("managerid");

        if (managerId == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if (optionalManager.isEmpty()) {
            throw new ManagerNotFoundException();
        }

        Manager manager = optionalManager.get();
        //NOTE: JUST NAME VALIDATION--NO SAME ARTISTE CAN SHARE A NAME AS THAT CAN AFFECT THEIR BRAND
        //EVEN BUJU AS TO CHANGE HIS NAME TO BNXN AS A MATTER OF FACT
        Optional<Artiste> artisteOptional = artisteRepository.findByName(artisteDto.getName());

        if (artisteOptional.isPresent()){
            throw new DuplicateUsernameException();
        }

        Artiste artistes = new Artiste();

        artistes.setManager(manager);
        Artiste artiste = mapDtoToEntity(artisteDto);
         artisteRepository.save(artiste);

         ArtisteResponseDto artisteResponseDto = new ArtisteResponseDto();
         modelMapper.map(artisteDto,artisteResponseDto);

         return artisteResponseDto;
    }

    @Override
    public ArtisteResponseDto updateArtisteProfileDto(Long artisteId, ArtisteDto artisteDto,HttpSession session) throws NotFoundException, RestrictedToManagerException {

        checkManagerSession(session);

        Artiste existingArtiste = artisteRepository.findById(artisteId)
                .orElseThrow(NotFoundException::new);

        existingArtiste.setName(artisteDto.getName());
        existingArtiste.setAboutStar(artisteDto.getAboutStar());
        existingArtiste.setLocation(artisteDto.getLocation());
        existingArtiste.setCurrentAppearanceCharges(artisteDto.getCurrentAppearanceCharges());
        existingArtiste.setCurrentPerformanceCharges(artisteDto.getCurrentPerformanceCharges());
        existingArtiste.setImage(artisteDto.getImage().getBytes(StandardCharsets.UTF_8));
        existingArtiste.setUpdatedOn(LocalDateTime.now());

        if (artisteDto.getHandles() != null) {
            existingArtiste.setHandles(updateHandlesFromDto(existingArtiste.getHandles(), artisteDto.getHandles()));
        }

         artisteRepository.save(existingArtiste);

        ArtisteResponseDto artisteResponseDto = new ArtisteResponseDto();

        modelMapper.map(artisteDto,artisteResponseDto);

        return artisteResponseDto;
    }

    @Override
    public ArtisteResponseDto getArtistById(Long artisteId) throws NotFoundException {

    Optional<Artiste> artisteOptional = artisteRepository.findById(artisteId);
    if (artisteOptional.isEmpty()){
        throw new NotFoundException();
    }

    Artiste artiste = artisteOptional.get();

    ArtisteResponseDto artisteResponseDto = new ArtisteResponseDto();

    modelMapper.map(artiste,artisteResponseDto);

    return artisteResponseDto;
    }

    @Override
    public ArtisteResponseDto getByArtisteName(String name) throws NotFoundException {

        Optional<Artiste> artisteOptional = artisteRepository.findByName(name);
        if (artisteOptional.isEmpty()){
            throw new NotFoundException();
        }

        Artiste artiste = artisteOptional.get();

        artisteRepository.save(artiste);

        ArtisteResponseDto artisteResponseDto = new ArtisteResponseDto();
        modelMapper.map(artiste,artisteResponseDto);

        return artisteResponseDto;
    }

    @Override
    public List<ArtisteResponseDto> getAllArtiste() {

        List<Artiste> artistes = artisteRepository.findAll();

         return artistes.stream()
                .map(artiste -> ArtisteResponseDto.builder()
                        .name(artiste.getName())
                        .aboutStar(artiste.getAboutStar())
                        .image(Arrays.toString(artiste.getImage()))
                        .currentAppearanceCharges(artiste.getCurrentAppearanceCharges())
                        .currentPerformanceCharges(artiste.getCurrentPerformanceCharges())
                        .createdOn(artiste.getCreatedOn())
                        .updatedOn(artiste.getUpdatedOn())
                        .location(artiste.getLocation())
                        .handles(artiste.getHandles())
                       .build())
                .collect(Collectors.toList());
    }

    private Handles updateHandlesFromDto(Handles existingHandles, Handles updatedHandlesDto) {
        existingHandles.setTwitterHandle(updatedHandlesDto.getTwitterHandle());
        existingHandles.setInstagramHandle(updatedHandlesDto.getInstagramHandle());
        existingHandles.setTiktokHandle(updatedHandlesDto.getTiktokHandle());
        existingHandles.setFaceBookHandle(updatedHandlesDto.getFaceBookHandle());
        existingHandles.setSnapChatHandle(updatedHandlesDto.getSnapChatHandle());
        existingHandles.setClubHouseHandle(updatedHandlesDto.getClubHouseHandle());

        return existingHandles;
    }

    private Artiste mapDtoToEntity(ArtisteDto artisteDto) {
        Artiste artiste = new Artiste();
        artiste.setName(artisteDto.getName());
        artiste.setAboutStar(artisteDto.getAboutStar());
        artiste.setCurrentAppearanceCharges(artisteDto.getCurrentAppearanceCharges());
        artiste.setCurrentPerformanceCharges(artisteDto.getCurrentPerformanceCharges());
        artiste.setImage(artisteDto.getImage().getBytes(StandardCharsets.UTF_8));
        artiste.setLocation(artisteDto.getLocation());
        artiste.setCreatedOn(LocalDateTime.now());
        artiste.setUpdatedOn(LocalDateTime.now());


        if (artisteDto.getHandles() != null) {
            Handles handles = mapHandlesDtoToEntity(artisteDto.getHandles());
            artiste.setHandles(handles);
        }


        return artiste;
    }


    private Handles mapHandlesDtoToEntity(Handles handlesDto) {
        Handles handles = new Handles();
        handles.setTwitterHandle(handlesDto.getTwitterHandle());
        handles.setInstagramHandle(handlesDto.getInstagramHandle());
        handles.setClubHouseHandle(handlesDto.getClubHouseHandle());
        handles.setTiktokHandle(handlesDto.getTiktokHandle());
        handles.setSnapChatHandle(handlesDto.getSnapChatHandle());
        handles.setFaceBookHandle(handlesDto.getFaceBookHandle());

        return handles;
    }



    public void checkManagerSession(HttpSession session) throws RestrictedToManagerException, NotFoundException {
        Long managerId = getManagerIdFromSession(session);
        validateManagerId(managerId);
    }

    private Long getManagerIdFromSession(HttpSession session) {
        return
                (Long) session.getAttribute("managerid");
    }

    private void validateManagerId(Long managerId) throws RestrictedToManagerException, NotFoundException {
        if (managerId == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if (optionalManager.isEmpty()) {
            throw new NotFoundException();
        }
    }
}

