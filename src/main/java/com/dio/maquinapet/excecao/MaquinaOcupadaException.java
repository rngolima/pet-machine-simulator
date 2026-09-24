package com.dio.maquinapet.excecao;

/**
 * Lançada ao tentar colocar um pet na máquina quando já existe outro pet dentro dela,
 * ou ao tentar higienizar a máquina com o animal ainda em seu interior.
 */
public class MaquinaOcupadaException extends MaquinaPetException {
    public MaquinaOcupadaException(String mensagem) {
        super(mensagem);
    }
}
