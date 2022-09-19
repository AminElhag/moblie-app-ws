package com.example.mobileappws.ui.contoller;

import com.example.mobileappws.exception.UserServiceException;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws.ui.model.resposne.ErrorMessages;
import com.example.mobileappws.ui.model.resposne.UserRes;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class Users {
    @Autowired
    UserService userService;


    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRes getUser(@PathVariable String id) {
        UserRes res = new UserRes();
        UserDto userDto = userService.getUserDtoById(id);
        BeanUtils.copyProperties(userDto, res);
        return res;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRes createUser(@RequestBody UserDetailsRequestModel userDetailsRequest) {

        if (userDetailsRequest.getFirstName() == null || userDetailsRequest.getLastName() == null || userDetailsRequest.getEmail() == null || userDetailsRequest.getPassword() == null)
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FILED.getErrorMessage());


        UserRes userRes = new UserRes();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequest, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, userRes);

        return userRes;
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

        UserDto user = userService.updateUser(id,userDto);
        BeanUtils.copyProperties(user, userRes);

        return userRes;
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete User Call !!";
    }
}
