package com.dio.petmachine.exception;

/**
 * Lançada quando se tenta inserir um pet na máquina já ocupada.
 */
public class MachineOccupiedException extends PetMachineException {
    public MachineOccupiedException(String message) {
        super(message);
    }
}
