package com.example.mobileappws.service.impl;

import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.io.repositories.UserRepository;
import com.example.mobileappws.ui.shared.Utils;
import com.example.mobileappws.ui.shared.dto.AddressDto;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    Utils utils;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    UserEntity userEntity;
    UserDto userDto;
    String testId = "asfdjgakdf5";
    String testEncryptedPassword = "asfdjgakdf5";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Test");
        userEntity.setLastName("Test");
        userEntity.setUserId(testId);
        userEntity.setEncryptedPassword(testEncryptedPassword);
        userEntity.setEmail("yesy@test.com");
        userEntity.setEmailVerificationToken("DASJLKDHKAFSBjaDL:FJ");

        userDto = new UserDto();
        userDto.setEmail("test@test.com");
        userDto.setFirstName("test");
        userDto.setLastName("test");
        userDto.setPassword("password");
        userDto.setAddress(getAddressDtos());
    }

    private List<AddressDto> getAddressDtos() {
        List<AddressDto> addressDtos = new ArrayList<>();
        AddressDto addressDto = new AddressDto();
        addressDto.setCity("Test");
        addressDto.setCountry("Test");
        addressDto.setStreetName("Test");
        addressDto.setPostCode("1234");
        addressDto.setType("Test");
        addressDtos.add(addressDto);
        AddressDto addressDto1 = new AddressDto();
        addressDto1.setCity("Test");
        addressDto1.setCountry("Test");
        addressDto1.setStreetName("Test");
        addressDto1.setPostCode("1234");
        addressDto1.setType("Test");
        addressDtos.add(addressDto1);
        return addressDtos;
    }

    @Test
    final void getUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = userService.getUserDtoByEmail("test@test.com");
        assertNotNull(userDto);
        assertEquals("Test", userDto.getFirstName());
    }

    @Test
    final void getUserThrowUserNameNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrowsExactly(UsernameNotFoundException.class, () -> {
            userService.getUserDtoByEmail("Test@Test.com");
        });
    }

    @Test
    final void createUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateRandomId(anyInt())).thenReturn(testId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(testEncryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto user = userService.createUser(userDto);
        assertNotNull(user);
        assertEquals(userEntity.getFirstName(), user.getFirstName());
        verify(utils, times(3)).generateRandomId(30);
//        verify(bCryptPasswordEncoder,times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    final void createUserRuntimeException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        assertThrowsExactly(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });
    }
}