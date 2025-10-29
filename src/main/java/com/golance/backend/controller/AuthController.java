package com.golance.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.golance.backend.service.UserService;
import com.golance.backend.model.User;
import com.golance.backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")  //api for authentication entry
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // Generate token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // ✅ Get full User entity (with id, email, etc.)
            User dbUser = userService.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found after login"));

            Map<String, Object> response = Map.of(
                    "token", token,
                    "user", Map.of(
                            "id", dbUser.getId(),
                            "username", dbUser.getUsername(),
                            "email", dbUser.getEmail()
                    )
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }


}
//            // return token + basic user info
//            Map<String, Object> response = Map.of(
//                    "token", token,
//                    "user", Map.of(
//
//                            "username", userDetails.getUsername(),
//                            "email", userDetails.getAuthorities())
//                    // optionally add roles, email, etc.
//
//	        );
//
//            return ResponseEntity.ok(response);
//
//        } catch(Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid"));
//        }
//    }
//
//}