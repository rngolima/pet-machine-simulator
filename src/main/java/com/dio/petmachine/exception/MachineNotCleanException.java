package com.dio.petmachine.exception;

/**
 * Lançada quando se tenta colocar um pet em uma máquina que ainda não foi higienizada.
 */
public class MachineNotCleanException extends PetMachineException {
    public MachineNotCleanException(String message) {
        super(message);
    }
}
