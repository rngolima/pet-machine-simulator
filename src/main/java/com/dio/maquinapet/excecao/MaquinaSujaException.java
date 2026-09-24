package com.dio.maquinapet.excecao;

/**
 * Lançada ao tentar colocar um pet na máquina quando ela ainda está suja de um banho anterior.
 */
public class MaquinaSujaException extends MaquinaPetException {
    public MaquinaSujaException(String mensagem) {
        super(mensagem);
    }
}
