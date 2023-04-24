package com.diego.prandini.exerciciotecnicoelotech.infra.system.security;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderNoOp implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return "encrypted: " + password;
    }

    @Override
    public boolean check(String encoded, String verified) {
        return ("encrypted: " + verified).equals(encoded);
    }
}
