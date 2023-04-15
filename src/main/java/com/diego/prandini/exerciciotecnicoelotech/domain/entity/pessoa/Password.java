package com.diego.prandini.exerciciotecnicoelotech.domain.entity.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.exception.PasswordVazioException;
import com.diego.prandini.exerciciotecnicoelotech.infra.system.security.PasswordEncoder;
import com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Password {
    private final String value;

    public Password(String password, PasswordEncoder passwordEncoder) {
        if (StringUtils.isBlank(password))
            throw new PasswordVazioException();
        this.value = passwordEncoder.encode(password);
    }

    public Password(String password) {
        if (StringUtils.isBlank(password))
            throw new PasswordVazioException();
        this.value = password;
    }

    public String get() {
        return value;
    }
}
