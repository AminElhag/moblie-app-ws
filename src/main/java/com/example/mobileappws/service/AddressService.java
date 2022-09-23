package com.example.mobileappws.service;

import com.example.mobileappws.ui.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddresses(String userID);

    AddressDto getAddress(String userId, String addressId);
}
