package com.dio.petmachine.exception;

/**
 * Exceção base de domínio para operações inválidas na Pet Machine.
 *
 * @author Rudson Lima
 */
public class PetMachineException extends RuntimeException {

    public PetMachineException(String message) {
        super(message);
    }

    public PetMachineException(String message, Throwable cause) {
        super(message, cause);
    }
}
