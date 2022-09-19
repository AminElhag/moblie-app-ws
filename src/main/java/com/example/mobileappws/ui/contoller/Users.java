package com.example.mobileappws.ui.contoller;

import com.example.mobileappws.service.UserService;
import com.example.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws.ui.model.resposne.UserRes;
import com.example.mobileappws.ui.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class Users {
    @Autowired
    UserService userService;


    @GetMapping(path = "/{id}")
    public UserRes getUser(@PathVariable String id) {
        UserRes res = new UserRes();
        UserDto userDto = userService.getUserDtoById(id);
        BeanUtils.copyProperties(userDto, res);
        return res;
    }

    @PostMapping
    public UserRes createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) {

        System.out.println(userDetailsRequestModel);

        UserRes userRes = new UserRes();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetailsRequestModel, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, userRes);

        return userRes;
    }

    @PutMapping
    public String updateUser() {
        return "Update User Call !!";
    }

    @DeleteMapping
    public String deleteUser() {
        return "Delete User Call !!";
    }
}
