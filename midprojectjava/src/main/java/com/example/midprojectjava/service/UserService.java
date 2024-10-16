package com.example.midprojectjava.service;

import com.example.midprojectjava.entity.SpmallUser;

public interface UserService {
	SpmallUser findById(Integer id);
    SpmallUser findByUsername(String name);
}
