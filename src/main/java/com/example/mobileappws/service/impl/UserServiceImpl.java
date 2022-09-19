package com.example.mobileappws.service.impl;

import com.example.mobileappws.exception.UserServiceException;
import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.io.repositories.UserRepository;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.ui.model.resposne.ErrorMessages;
import com.example.mobileappws.ui.shared.Utils;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto userDto) {

        if (repository.findByEmail(userDto.getEmail()) != null) throw new RuntimeException("Record already exist !!");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setUserId(utils.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity storeUserDetails = repository.save(userEntity);

        UserDto returnDto = new UserDto();
        BeanUtils.copyProperties(storeUserDetails, returnDto);
        return returnDto;
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        UserEntity user = repository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserDtoById(String id) {
        UserEntity user = repository.findByUserId(id);
        if (user == null) throw new UsernameNotFoundException(id);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserDto user = new UserDto();

        UserEntity entity = repository.findByUserId(id);
        if (entity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        UserEntity save = repository.save(entity);

        BeanUtils.copyProperties(save,user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity entity = repository.findByEmail(email);
        if (entity == null) throw new UsernameNotFoundException(email);
        return new User(entity.getEmail(), entity.getEncryptedPassword(), new ArrayList<>());
    }
}
