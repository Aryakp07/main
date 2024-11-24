package com.user.register;



import jakarta.transaction.Transactional;



import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

	
	
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(UserRegistrationmodel user) {
    	
    	if (user.getName() == null || user.getEmail() == null || user.getPassword() == null || user.getGender() == null) {
            return "All fields (name, email, password, gender) are required!";
        }
        if (userRegistrationRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User with this email already exists!";
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRegistrationRepository.save(user);
        return "User registered successfully!";
    }
    
    public List<UserRegistrationmodel> getAllUsers() {
        return userRegistrationRepository.findAll();
    }
    
    public Optional<UserRegistrationmodel> getUserByEmail(String email) {
        return userRegistrationRepository.findByEmail(email);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        
        if (userRegistrationRepository.findByEmail(email).isPresent()) {
            userRegistrationRepository.deleteByEmail(email);
        }
    }
}


