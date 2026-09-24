package com.dio.maquinapet.excecao;

/**
 * Lançada quando uma operação que exige a presença do pet é executada com a máquina vazia.
 */
public class MaquinaVaziaException extends MaquinaPetException {
    public MaquinaVaziaException(String mensagem) {
        super(mensagem);
    }
}
