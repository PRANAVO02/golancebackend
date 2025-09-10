package com.golance.backend.service;

import com.golance.backend.model.User;
import com.golance.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    // to Register new user , import from user module
    public User registerUser(User user){
        return userRepository.save(user);
    }

    // Find user by email (for login)
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    //logic to validate the user by email which we got earlier
    public boolean validateUser(String email,String password){
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isPresent()){
            User user=userOpt.get();
            return user.getPassword().equals(password);
        }
        else{
            return false;
        }
    }


}
