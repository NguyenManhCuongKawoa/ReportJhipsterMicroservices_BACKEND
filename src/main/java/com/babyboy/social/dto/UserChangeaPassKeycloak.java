package com.babyboy.social.dto;

public class UserChangeaPassKeycloak {
    private String value;
    private String type = "password";
    private boolean temporary = false;

    public UserChangeaPassKeycloak(String password) {
        this.value = password;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }
}
