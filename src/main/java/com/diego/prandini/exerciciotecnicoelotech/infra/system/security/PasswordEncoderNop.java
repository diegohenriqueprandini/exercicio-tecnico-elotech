package com.diego.prandini.exerciciotecnicoelotech.infra.system.security;

public class PasswordEncoderNop implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return "encrypted: " + password;
    }

    @Override
    public boolean check(String encoded, String verified) {
        return ("encrypted: " + verified).equals(encoded);
    }
}
