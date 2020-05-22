package com.se68.rraptor.futurlarm.Class;

public class User {
    private String username, email, avatar;

    public User(){}

    public User(String username,
                String email,
                String avatar) {
        this.username = username;
        this.email = email;
        this.avatar = avatar;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
