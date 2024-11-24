package com.user.register;

import com.user.register.UserRegistrationmodel;
import com.user.register.JwtUtil;
import com.user.register.UserRegistrationService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userregistration")
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationmodel user) {
        
        String result = userRegistrationService.register(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRegistrationmodel>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(null); 
        }

       
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        if (!jwtUtil.validateToken(token, null)) {
            return ResponseEntity.status(401).body(null); 
        }
        Optional<UserRegistrationmodel> user = userRegistrationService.getUserByEmail(email);
        
        
        List<UserRegistrationmodel> users = userRegistrationService.getAllUsers();
        return ResponseEntity.ok(users); 
    }
   

    
    @DeleteMapping("/users/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized access. Token is missing.");
        }

        
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token, null)) {
            return ResponseEntity.status(401).body("Unauthorized access. Invalid token.");
        }

        Optional<UserRegistrationmodel> user = userRegistrationService.getUserByEmail(email);
        
        if (user.isPresent()) {
            userRegistrationService.deleteUserByEmail(email); 
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }
}