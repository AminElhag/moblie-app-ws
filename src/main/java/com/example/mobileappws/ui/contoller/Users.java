package com.example.mobileappws.ui.contoller;

import com.example.mobileappws.exception.UserServiceException;
import com.example.mobileappws.service.AddressService;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws.ui.model.resposne.*;
import com.example.mobileappws.ui.shared.dto.AddressDto;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class Users {
    @Autowired
    UserService userService;
    @Autowired
    AddressService addressService;
    ModelMapper mapper = new ModelMapper();

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRes getUser(@PathVariable String id) {
        UserRes res = new UserRes();
        UserDto userDto = userService.getUserDtoById(id);
        BeanUtils.copyProperties(userDto, res);
        return res;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRes> getUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        List<UserRes> userResList = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRes res = new UserRes();
            BeanUtils.copyProperties(userDto, res);
            userResList.add(res);
        }
        return userResList;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRes createUser(@RequestBody UserDetailsRequestModel userDetailsRequest) {

        if (userDetailsRequest.getFirstName() == null || userDetailsRequest.getLastName() == null || userDetailsRequest.getEmail() == null || userDetailsRequest.getPassword() == null)
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FILED.getErrorMessage());

        UserDto userDto = mapper.map(userDetailsRequest, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        return mapper.map(createdUser, UserRes.class);
    }

    @PutMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UserRes updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel requestModel) {
        UserRes userRes = new UserRes();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(requestModel, userDto);

        UserDto user = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(user, userRes);

        return userRes;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel operationStatus = new OperationStatusModel();
        operationStatus.setOperationName(OperationName.DELETE.name());
        userService.deleteUser(id);
        operationStatus.setOperationStatue(OperationStatus.SUCCESS.name());
        return operationStatus;
    }

    @GetMapping(path = "/{userId}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<AddressRes> getAddresses(@PathVariable String userId) {
        List<AddressRes> addressResList = new ArrayList<>();

        List<AddressDto> addressDtos = addressService.getAddresses(userId);

        if (addressDtos == null || addressDtos.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        Type type = new TypeToken<List<AddressRes>>() {
        }.getType();
        addressResList = mapper.map(addressDtos, type);

        return addressResList;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public AddressRes getAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressRes addressRes = new AddressRes();

        AddressDto addressDto = addressService.getAddress(userId, addressId);

        if (addressDto == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        addressRes = mapper.map(addressDto, AddressRes.class);

        Link userLink = WebMvcLinkBuilder.linkTo(Users.class).slash(userId).withRel("user");
        Link addressesLink = WebMvcLinkBuilder.linkTo(Users.class).slash(userId).slash("addresses").withRel("addresses");
        Link selfLink = WebMvcLinkBuilder.linkTo(Users.class).slash(userId).slash("addresses").slash(addressId).withRel("addresses");

        addressRes.add(userLink);
        addressRes.add(addressesLink);
        addressRes.add(selfLink);
        return addressRes;
    }
}
