package com.diego.prandini.exerciciotecnicoelotech.infra.system.security;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderOK implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return password;
    }

    @Override
    public boolean check(String password, String verified) {
        return password.equals(verified);
    }
}
