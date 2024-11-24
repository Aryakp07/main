package com.user.register;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;



class UserRegistrationServiceTest {

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Mock
    private UserRegistrationRepository userRegistrationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testRegister_SuccessfulRegistration() {
        
        UserRegistrationmodel user = new UserRegistrationmodel();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setName("Test User");
        user.setGender("Male");

        when(userRegistrationRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        
        String result = userRegistrationService.register(user);

        
        assertEquals("User registered successfully!", result);
        verify(userRegistrationRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    
    @Test
    void testGetAllUsers() {
        
        UserRegistrationmodel user1 = new UserRegistrationmodel();
        user1.setEmail("user1@example.com");

        UserRegistrationmodel user2 = new UserRegistrationmodel();
        user2.setEmail("user2@example.com");

        List<UserRegistrationmodel> users = Arrays.asList(user1, user2);

        when(userRegistrationRepository.findAll()).thenReturn(users);

        
        List<UserRegistrationmodel> result = userRegistrationService.getAllUsers();

       
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());
        verify(userRegistrationRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByEmail_UserExists() {
       
        UserRegistrationmodel user = new UserRegistrationmodel();
        user.setEmail("test@example.com");

        when(userRegistrationRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

       
        Optional<UserRegistrationmodel> result = userRegistrationService.getUserByEmail(user.getEmail());

        
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRegistrationRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetUserByEmail_UserDoesNotExist() {
        
        String email = "nonexistent@example.com";

        when(userRegistrationRepository.findByEmail(email)).thenReturn(Optional.empty());

    
        Optional<UserRegistrationmodel> result = userRegistrationService.getUserByEmail(email);

       
        assertFalse(result.isPresent());
        verify(userRegistrationRepository, times(1)).findByEmail(email);
    }
}
