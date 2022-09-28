package com.example.mobileappws.service;

import com.example.mobileappws.ui.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserDtoByEmail(String email);

    UserDto getUserDtoById(String id);

    UserDto updateUser(String id, UserDto userDto);

    Void deleteUser(String id);

    List<UserDto> getUsers(int page, int limit);

    boolean verifyEmailToken(String token);
}
