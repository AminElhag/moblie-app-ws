package com.example.mobileappws.service.impl;

import com.example.mobileappws.io.entity.AddressEntity;
import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.io.repositories.AddressRepository;
import com.example.mobileappws.io.repositories.UserRepository;
import com.example.mobileappws.service.AddressService;
import com.example.mobileappws.ui.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AddressRepository addressRepository;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<AddressDto> getAddresses(String userID) {
        List<AddressDto> addressDtos = new ArrayList<>();
        UserEntity userEntity = userRepository.findByUserId(userID);
        if (userEntity == null) return addressDtos;

        Iterable<AddressEntity> addressEntities = addressRepository.findAllByUserDetails(userEntity);
        for (AddressEntity addressEntity : addressEntities) {
            addressDtos.add(mapper.map(addressEntity, AddressDto.class));
        }
        return addressDtos;
    }

    @Override
    public AddressDto getAddress(String userId, String addressId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) return null;
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        AddressDto addressDto = mapper.map(addressEntity, AddressDto.class);
        return addressDto;
    }
}
