package com.golance.backend.service;

import com.golance.backend.model.User;
import com.golance.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




//    // to Register new user , import from user module
//    public User registerUser(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }
public User registerUser(User user) {
    // Check if email already exists
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        throw new RuntimeException("Email already exists");
    }

    // Check if username already exists
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        throw new RuntimeException("Username already exists");
    }

    // Hash password and save
    user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("STUDENT")));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    // Add this method inside your com.golance.backend.service.UserService class

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}