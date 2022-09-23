package com.example.mobileappws.ui.model.resposne;

import java.util.List;

public class UserRes {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRes> address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AddressRes> getAddress() {
        return address;
    }

    public void setAddress(List<AddressRes> address) {
        this.address = address;
    }
}
