package com.eduacsp.teste.itau.excep;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String errorMessage) {
        super(errorMessage);
    }
}

