package com.example.mobileappws.io.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "password_resets_tokens")
public class PasswordResetTokenEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
