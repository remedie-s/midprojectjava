package com.example.midprojectjava.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.midprojectjava.entity.SpmallUser;
import com.example.midprojectjava.exception.DataNotFoundException;
import com.example.midprojectjava.repository.SpmallUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpmallUserService implements UserService {
    
    private final SpmallUserRepository spmallUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SpmallUser findById(Integer id) {
        return spmallUserRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    @Override
    public SpmallUser findByUsername(String name) {
        return spmallUserRepository.findByUsername(name)
            .orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    public SpmallUser create(String username, String password, String firstName, String lastName, 
                             String phoneNumber, String email) {
        SpmallUser user = new SpmallUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setUserGrade(0);
        user.setCreateDate(LocalDateTime.now());
        return spmallUserRepository.save(user);
    }
}
