package br.lucas.financeiro.util;

public class Validador {

    public static final String NAO_INFORMADO = "NÃO INFORMADO";

    public static String validarNomeCompleto(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        nome = nome.trim();

        if (!nome.matches("[a-zA-Z\\s]+")) {
            throw new IllegalArgumentException("Nome deve conter nome e sobrenome, somente letras");
        }
        return nome;
    }

    public static String validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser vazio");
        }

        cpf = cpf.trim();
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new IllegalArgumentException("CPF não pode conter letras ou caracteres especiais. Insira no seguinte formato: 000.000.000-00");
        }
        return cpf;
    }
}