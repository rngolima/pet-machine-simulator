package com.dio.petmachine.exception;

/**
 * Lançada quando uma operação que exige a presença de um pet é executada na máquina vazia.
 */
public class MachineEmptyException extends PetMachineException {
    public MachineEmptyException(String message) {
        super(message);
    }
}
