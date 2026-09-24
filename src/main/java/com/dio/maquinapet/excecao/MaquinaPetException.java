package com.dio.maquinapet.excecao;

/**
 * Exceção base de domínio para operações inválidas na Máquina de Banho Pet.
 *
 * @author Rudson Lima
 */
public class MaquinaPetException extends RuntimeException {

    public MaquinaPetException(String mensagem) {
        super(mensagem);
    }

    public MaquinaPetException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
