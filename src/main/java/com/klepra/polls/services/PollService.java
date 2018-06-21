/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.klepra.polls.services;

import com.klepra.polls.entity.Option;
import com.klepra.polls.entity.Poll;
import com.klepra.polls.repository.OptionRepository;
import com.klepra.polls.repository.PollRepository;
import com.klepra.polls.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author klemen
 */
@Service
public class PollService {
    
    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public PollService(PollRepository pollRepository, OptionRepository optionRepository, UserRepository userRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional
    public Poll savePoll(Poll poll) {
        
        Poll savedPoll = pollRepository.save(poll);
        poll.getOptions().stream().forEach(option -> {
            option.setPoll(savedPoll);
            optionRepository.save(option);
        });
        
        return savedPoll;
    }
    
    @Transactional
    public Poll updatePoll(Poll poll) {
        optionRepository.deleteByPollId(poll.getId());
        
        Poll savedPoll = pollRepository.save(poll);
        poll.getOptions().stream().forEach(option -> {
            option.setPoll(savedPoll);
            optionRepository.save(option);
        });
        
        return savedPoll;
    }
    
    public List<Poll> getAll() {
        return pollRepository.findAll();
    }
    
    public Poll getPollById(Long id) {
        return pollRepository.getOne(id);
    }
    
    public void deletePollById(Long id) {
        pollRepository.deleteById(id);
    }

    @Transactional
    public void vote(Long id, Long optionId) throws Exception {
        Poll poll = pollRepository.getOne(id);
        
        //TO-DO: check end-date
        
        //TO-DO: add IP
        
        
        List<Option> options = poll.getOptions().stream().filter(option -> option.getId()== optionId).collect(Collectors.toList());
        if(options.size()==1){
            Option option = options.get(0);
            option.setScore(option.getScore()+1);
            optionRepository.save(option);
        } else {
            throw new Exception("Option id for poll not unique!");
        }
    }
    
}