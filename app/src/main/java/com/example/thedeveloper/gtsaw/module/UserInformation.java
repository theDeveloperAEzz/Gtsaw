package com.example.thedeveloper.gtsaw.module;

import java.util.HashMap;
import java.util.Map;

public class UserInformation {
    private String name;
    private String email;
    private String key;
    private String avatar;
    public boolean isOnline;


    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }


    public UserInformation() {

    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("avatar", avatar);
        return result;
    }

    public UserInformation(String name, String email, String avatar,String key) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.key = key;
        isOnline=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
