package com.babyboy.social.dto.request;

public class RoleKeycloakRequest {
    private String realm;
    private String name;

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
