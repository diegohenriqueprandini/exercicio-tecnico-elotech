package com.diego.prandini.exerciciotecnicoelotech.utils;

import java.util.ArrayList;
import java.util.List;

import static com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils.isBlank;
import static com.diego.prandini.exerciciotecnicoelotech.utils.StringUtils.trimToEmpty;

public class CpfUtils {

    private static final List<String> CPF_INVALIDO = new ArrayList<>();

    static {
        CPF_INVALIDO.add("00000000000");
        CPF_INVALIDO.add("11111111111");
        CPF_INVALIDO.add("22222222222");
        CPF_INVALIDO.add("33333333333");
        CPF_INVALIDO.add("44444444444");
        CPF_INVALIDO.add("55555555555");
        CPF_INVALIDO.add("66666666666");
        CPF_INVALIDO.add("77777777777");
        CPF_INVALIDO.add("88888888888");
        CPF_INVALIDO.add("99999999999");
    }

    public static boolean isCpfValido(String value) {
        if (isBlank(value))
            return false;
        String cpf = cpfSemMascara(value);
        if (CPF_INVALIDO.contains(cpf))
            return false;
        return cpf.matches("\\d{11}") && isCpfValidoDadoDigitos(cpf.substring(0, 10)) && isCpfValidoDadoDigitos(cpf);
    }

    private static boolean isCpfValidoDadoDigitos(String digitos) {
        if (Long.parseLong(digitos) % 10 == 0)
            return somaPonderadaCpf(digitos) % 11 < 2;
        return somaPonderadaCpf(digitos) % 11 == 0;
    }

    private static int somaPonderadaCpf(String digitos) {
        char[] cs = digitos.toCharArray();
        int soma = 0;
        for (int i = 0; i < cs.length; i++)
            soma += Character.digit(cs[i], 10) * (cs.length - i);
        return soma;
    }

    public static String cpfSemMascara(String cpf) {
        if (cpf == null)
            return null;
        cpf = trimToEmpty(cpf);
        return cpf.replaceAll("\\.", "").replaceAll("-", "");
    }
}
