package com.diego.prandini.exerciciotecnicoelotech.infra.system.security;

public interface PasswordEncoder {

    String encode(String password);

    boolean check(String password, String verified);
}
