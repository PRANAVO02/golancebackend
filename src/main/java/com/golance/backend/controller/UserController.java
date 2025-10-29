package com.golance.backend.controller;

import com.golance.backend.model.User;
import com.golance.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {


    @Autowired
    private UserService userService;


    //registering a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody User user) {
//        try {
//            User savedUser = userService.registerUser(user);
//            return ResponseEntity.ok(savedUser);
//        } catch (RuntimeException e) {
//            // Return 400 Bad Request if email/username exists
//            return ResponseEntity
//                    .badRequest()
//                    .body(Map.of("message", e.getMessage()));
//        }
//    }
//
//
//
//    // GET /api/users/{id}
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id) {
//        // The getUserById method in UserService already throws RuntimeException if not found;
//        // You can handle this with ResponseEntity as below.
//        try {
//            User user = userService.getUserById(id);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    // ✅ Get user details by ID
//    @GetMapping("/{id}")
//    public Map<String, Object> getUserById(@PathVariable Long id) {
//        User user = userService.getUserById(id);
//
//        // Return only relevant details (omit password)
//        return Map.of(
//                "id", user.getId(),
//                "username", user.getUsername(),
//                "email", user.getEmail(),
//                "role", user.getRole(),
//                "skills", user.getSkills(),
//                "studyingYear", user.getStudyingYear(),
//                "department", user.getDepartment()
//        );
//    }

//    // 2️⃣ Login user
//    @PostMapping("/login")
//    public Object loginUser(@RequestBody User loginRequest) {
//        Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            if (user.getPassword().equals(loginRequest.getPassword())) {
//                return user; // return full User entity including skills/year/department
//            }
//        }
//        return Map.of("message", "Invalid email or password");
//    }
//
//    // 3️⃣ Get user details by ID
//    @GetMapping("/{id}")
//    public Object getUserById(@PathVariable Long id) {
//        try {
//            User user = userService.getUserById(id);
//            return user; // return full User entity
//        } catch (Exception e) {
//            return Map.of("message", "User not found with id: " + id);
//        }
//    }
}
