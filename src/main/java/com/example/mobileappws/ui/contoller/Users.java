package com.example.mobileappws.ui.contoller;

import com.example.mobileappws.exception.UserServiceException;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws.ui.model.resposne.*;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
}
