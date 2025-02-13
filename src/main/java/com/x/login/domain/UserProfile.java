package com.x.login.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserProfile {
    private String id;
    @JsonProperty("profile_image_url")
    private String profilePictureUrl;
    @JsonProperty("name")
    private String fullName;
    private String username;
    @JsonProperty("description")
    private String bio;
}
