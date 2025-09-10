package com.golance.backend.controller;


import com.golance.backend.model.User;
import com.golance.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //registering a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }

    //login user
    @PostMapping("/login")
    public String loginUser(@RequestBody User loginRequest){
        boolean isValid = userService.validateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if(isValid){
            return "Login Successful";
        }
        else{
            return "Invalid email or password";
        }
    }
}
