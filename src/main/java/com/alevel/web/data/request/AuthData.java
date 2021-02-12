package com.alevel.web.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthData {

    private String email;
    private String password;
    private String passwordConfirm;
}
