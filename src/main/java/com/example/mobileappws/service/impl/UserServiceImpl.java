package com.example.mobileappws.service.impl;

import com.example.mobileappws.exception.UserServiceException;
import com.example.mobileappws.io.entity.PasswordResetTokenEntity;
import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.io.repositories.PasswordResetTokenRepository;
import com.example.mobileappws.io.repositories.UserRepository;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.ui.model.resposne.ErrorMessages;
import com.example.mobileappws.ui.shared.Utils;
import com.example.mobileappws.ui.shared.dto.AddressDto;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null)
            throw new RuntimeException("Record already exist !!");

        ModelMapper mapper = new ModelMapper();

        for (int i = 0; i < userDto.getAddress().size(); i++) {
            AddressDto addressDto = userDto.getAddress().get(i);
            addressDto.setUserDetails(userDto);
            addressDto.setAddressId(utils.generateRandomId(30));
            userDto.getAddress().set(i, addressDto);
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setUserId(utils.generateRandomId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(userEntity.getUserId()));
        userEntity.setEmailVerificationStatus(false);
        com.example.mobileappws.io.entity.UserEntity storeUserDetails = userRepository.save(userEntity);

        return mapper.map(storeUserDetails, UserDto.class);
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        com.example.mobileappws.io.entity.UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserDtoById(String id) {
        com.example.mobileappws.io.entity.UserEntity user = userRepository.findByUserId(id);
        if (user == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserDto user = new UserDto();

        com.example.mobileappws.io.entity.UserEntity entity = userRepository.findByUserId(id);
        if (entity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        com.example.mobileappws.io.entity.UserEntity save = userRepository.save(entity);

        BeanUtils.copyProperties(save, user);
        return user;
    }

    @Override
    public Void deleteUser(String id) {
        com.example.mobileappws.io.entity.UserEntity entity = userRepository.findByUserId(id);
        if (entity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(entity);
        return null;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> users = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, limit);
        Page<com.example.mobileappws.io.entity.UserEntity> entityPage = userRepository.findAll(pageable);
        List<com.example.mobileappws.io.entity.UserEntity> usersEntity = entityPage.getContent();

        for (com.example.mobileappws.io.entity.UserEntity user : usersEntity) {
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(user, dto);
            users.add(dto);
        }
        return users;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        UserEntity user = userRepository.findUserByEmailVerificationToken(token);
        if (user != null) {
            boolean hasTokenExpired = Utils.hasTokenExpired(token);
            if (!hasTokenExpired) {
                user.setEmailVerificationToken(null);
                user.setEmailVerificationStatus(true);
                userRepository.save(user);
                return true;
            }
        }
        return returnValue;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return false;
        }
        String token = Utils.generatePasswordResetToken(userEntity.getUserId());

        PasswordResetTokenEntity tokenEntity = new PasswordResetTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(tokenEntity);
        /*Send email by one of mail send Service */
        return true;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        if (Utils.hasTokenExpired(token)) return false;
        PasswordResetTokenEntity passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) return false;
        UserEntity userEntity = passwordResetToken.getUserDetails();
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
        UserEntity saveUser = userRepository.save(userEntity);
        if (!Objects.equals(saveUser.getEncryptedPassword(), bCryptPasswordEncoder.encode(password))){
            return false;
        }
        passwordResetTokenRepository.delete(passwordResetToken);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.mobileappws.io.entity.UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) throw new UsernameNotFoundException(email);
        return new User(entity.getEmail(), entity.getEncryptedPassword(), entity.isEmailVerificationStatus(), true, true, true, new ArrayList<>());
    }
}
