package com.diego.prandini.exerciciotecnicoelotech.utils;

public class TelefoneUtils {

    public static boolean isTelefoneValido(String telefone) {
        if (StringUtils.isBlank(telefone))
            return false;
        return telefone.matches("\\d{11}");
    }
}
