package com.steiner.app.Models;

import java.io.Serializable;

public class UserModel implements Serializable {
    String type;
    String username;
    String email;
    String image;
    String token;

    public UserModel(String type, String username, String email, String image, String token) {
        this.type = type;
        this.username = username;
        this.email = email;
        this.image = image;
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
