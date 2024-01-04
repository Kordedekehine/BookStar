package com.artistbooking.BookArtist.serviceImpl;


import com.artistbooking.BookArtist.dPayload.request.OptionDto;
import com.artistbooking.BookArtist.dPayload.request.PollDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.model.*;
import com.artistbooking.BookArtist.repository.*;
import com.artistbooking.BookArtist.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private ManagerRepository managerRepository;

   @Autowired
    private UserRepository userRepository;

   @Autowired
   private VoteRepository voteRepository;

    @Override
    public List<PollDto> getAllPolls() throws NotFoundException, ManagerNotFoundException, UserNotFoundException {

        Long managerId = (Long) session.getAttribute("managerid");

        Long userId = (Long) session.getAttribute("userid");

        if (managerId == null && userId == null) {
            throw new NotFoundException();
        }

        if (managerId != null) {
            Optional<Manager> optionalManager = managerRepository.findById(managerId);
            if (optionalManager.isEmpty()) {
                throw new ManagerNotFoundException();
            }

        }

        if (userId != null) {
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException();
            }
        }

     List<Poll> polls = pollRepository.findAll();

        return polls.stream()
                .map(poll -> PollDto.builder()
                        .question(poll.getQuestion())
                        .createdAt(LocalDateTime.now())
                        .options(poll.getOptions().stream()
                                .map(option -> OptionDto.builder()
                                        .text(option.getText())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public PollDto createPoll(PollDto pollDto) throws GeneralServiceException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty()) {
            throw new ManagerNotFoundException();
        }

        List<OptionDto> optionDtos = pollDto.getOptions();
        if (optionDtos == null && optionDtos.isEmpty()) {
            throw new GeneralServiceException("\"Poll must have at least one option");
        }

        if (optionDtos.size() < 2) {
            throw new GeneralServiceException("\"Poll must have at least three options");
        }


            // Convert PollDTO to Poll entity
            Poll poll = new Poll();
            poll.setQuestion(pollDto.getQuestion());

            Poll savedPoll = pollRepository.save(poll);

            List<Option> options = optionDtos.stream()
                    .map(optionDTO -> {
                        Option option = new Option();
                        option.setText(optionDTO.getText());
                        option.setPoll(savedPoll);
                        return option;
                    })
                    .collect(Collectors.toList());

            optionRepository.saveAll(options);

            savedPoll.setOptions(options);
            pollRepository.save(savedPoll);

        return PollDto.builder()
                .question(savedPoll.getQuestion())
                .createdAt(LocalDateTime.now())
                .options(savedPoll.getOptions().stream()
                        .map(option -> OptionDto.builder()
                                .text(option.getText())
                                .build())
                        .collect(Collectors.toList())).build();
        }
     @Transactional
     @Override
    public String vote(Long pollId, Long optionId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

         //only users can vote
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


        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        Optional<Option> optionalOption = optionRepository.findById(optionId);

        if (optionalPoll.isEmpty() && optionalOption.isEmpty()) {
            throw new GeneralServiceException("The poll cannot be retrieved");
        }
            Poll poll = optionalPoll.get();
            Option option = optionalOption.get();

            // Update vote count for the selected option
            option.setVoteCount(option.getVoteCount() + 1);
            optionRepository.save(option);

            List<Option> pollOptions = (List<Option>) poll.getOptions();
            pollOptions.add(option);
            poll.setOptions( pollOptions);

            // Save the updated poll
            pollRepository.save(poll);

         // Record the user's vote to prevent double voting
         recordUserVote(user, poll.getId());

         //fixed

        return "Successfully Voted!!";
    }

    private boolean hasUserVoted(UserEntity user, Long pollId) {
        return voteRepository.existsByUserIdAndPollId(user.getId(), pollId);
    }

    private void recordUserVote(UserEntity user, Long pollId) throws GeneralServiceException {
        if (hasUserVoted(user, pollId)) {
         throw new GeneralServiceException("User already voted");
        }
            Vote userVote = new Vote();
            userVote.setUser(user);
            userVote.setPollId(pollId);
            voteRepository.save(userVote);
        }



    @Override
    public PollDto getPollById(Long pollId) throws NotFoundException {

        Optional<Poll> polls = pollRepository.findById(pollId);

        if (polls.isEmpty()){
            throw new NotFoundException();
        }

        Poll poll = polls.get();

        return PollDto.builder()
                .question(poll.getQuestion())
                .createdAt(LocalDateTime.now())
                .options(poll.getOptions().stream()
                        .map(option -> OptionDto.builder()
                                .text(option.getText())
                                .build())
                        .collect(Collectors.toList())).build();
    }


    @Override
    public PollDto updatePoll(Long pollId, PollDto pollDto) throws NotFoundException, RestrictedToManagerException, ManagerNotFoundException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty()) {
            throw new ManagerNotFoundException();
        }

        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isEmpty()) {
            throw new NotFoundException();
        }
            Poll existingPoll = optionalPoll.get();
            existingPoll.setQuestion(pollDto.getQuestion());

            List<OptionDto> optionDTOs = pollDto.getOptions();
            List<Option> updatedOptions = optionDTOs.stream()
                    .map(optionDTO -> {
                        Option option = new Option();
                        option.setText(optionDTO.getText());
                        option.setPoll(existingPoll);
                        return optionRepository.save(option);
                    })
                    .collect(Collectors.toList());

            existingPoll.setOptions(updatedOptions);

             pollRepository.save(existingPoll);

             return  PollDto.builder()
                     .question(existingPoll.getQuestion())
                     .createdAt(LocalDateTime.now())
                     .options(existingPoll.getOptions().stream()
                             .map(option -> OptionDto.builder()
                                     .text(option.getText())
                                     .build())
                             .collect(Collectors.toList())).build();
    }

    @Override
    public String deletePoll(Long pollId) throws RestrictedToManagerException, NotFoundException, ManagerNotFoundException {

        Long managerid = (Long) session.getAttribute("managerid");
        if (managerid == null) {
            throw new RestrictedToManagerException();
        }

        Optional<Manager> optionalManager = managerRepository.findById(managerid);
        if (optionalManager.isEmpty()) {
            throw new ManagerNotFoundException();
        }

        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isEmpty()) {
            throw new NotFoundException();
        }

        Poll poll = optionalPoll.get();

        pollRepository.delete(poll);

        return "Poll Deleted!";
    }



    @Transactional
    @Override
    public String unvote(Long pollId) throws RestrictedAccessException, UserNotFoundException, GeneralServiceException {

        Long userId = (Long) session.getAttribute("userid");
        System.out.println("userId: " + userId);
        if (userId == null) {
            throw new RestrictedAccessException();
        }

        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        UserEntity user = optionalUser.get();

        Optional<Vote> userVote = voteRepository.findByUserIdAndPollId(user.getId(), pollId);
        if (userVote.isEmpty()) {
            throw new GeneralServiceException("User has not voted in this poll");
        }

        voteRepository.delete(userVote.get());

        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isEmpty()) {
            throw new GeneralServiceException("The poll cannot be retrieved");
        }

        Poll poll = optionalPoll.get();

        // Find the option the user voted for
        Optional<Option> votedOption = findVotedOption(userVote.get(), poll.getOptions());

        if (votedOption.isPresent()) {
            votedOption.get().setVoteCount(votedOption.get().getVoteCount() - 1);
            optionRepository.save(votedOption.get());

            poll.getOptions().remove(votedOption.get()); //safe fail

            // Save the updated poll
            pollRepository.save(poll);
        }

        return "Successfully Unvoted!!";
    }

    private Optional<Option> findVotedOption(Vote vote, List<Option> options) {
        return options.stream()
                .filter(option -> option.getId().equals(vote.getId()))
                .findFirst();
    }
}
