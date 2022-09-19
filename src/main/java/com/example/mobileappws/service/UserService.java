package com.example.mobileappws.service;

import com.example.mobileappws.ui.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserDtoByEmail(String email);

    UserDto getUserDtoById(String id);
}
