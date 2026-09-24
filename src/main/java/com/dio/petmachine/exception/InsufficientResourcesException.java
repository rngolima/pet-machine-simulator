package com.dio.petmachine.exception;

/**
 * Lançada quando a máquina não possui água ou shampoo suficientes para completar uma operação.
 */
public class InsufficientResourcesException extends PetMachineException {
    public InsufficientResourcesException(String message) {
        super(message);
    }
}
