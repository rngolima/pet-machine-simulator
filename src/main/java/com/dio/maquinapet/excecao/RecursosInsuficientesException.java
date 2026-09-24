package com.dio.maquinapet.excecao;

/**
 * Lançada quando a máquina não possui quantidade suficiente de água ou shampoo
 * para completar o ciclo de banho ou o ciclo de higienização.
 */
public class RecursosInsuficientesException extends MaquinaPetException {
    public RecursosInsuficientesException(String mensagem) {
        super(mensagem);
    }
}
