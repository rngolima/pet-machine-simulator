package com.dio.petmachine.model;

import java.util.Objects;

/**
 * Representa um animal de estimação (Pet) que utiliza o serviço de banho automatizado.
 * <p>
 * Demonstra encapsulamento e proteção do estado interno:
 * <ul>
 *   <li>Atributos privados {@code name} e {@code clean}.</li>
 *   <li>Validação defensiva no construtor para evitar instâncias em estado inconsistente.</li>
 *   <li>Métodos de acesso controlados.</li>
 * </ul>
 *
 * @author Rudson Lima
 * @version 1.0.0
 */
public class Pet {

    private final String name;
    private boolean clean;

    /**
     * Construtor para inicializar um novo Pet.
     * Por padrão, todo Pet chega para atendimento precisando de banho ({@code clean = false}).
     *
     * @param name Nome do pet (não pode ser nulo ou vazio).
     * @throws IllegalArgumentException se o nome for nulo ou composto apenas de espaços.
     */
    public Pet(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do pet não pode ser nulo ou vazio.");
        }
        this.name = name.trim();
        this.clean = false;
    }

    /**
     * Construtor de conveniência permitindo definir o estado inicial de limpeza.
     *
     * @param name  Nome do pet.
     * @param clean Estado inicial de limpeza.
     */
    public Pet(String name, boolean clean) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do pet não pode ser nulo ou vazio.");
        }
        this.name = name.trim();
        this.clean = clean;
    }

    public String getName() {
        return name;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet pet)) return false;
        return clean == pet.clean && Objects.equals(name, pet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, clean);
    }

    @Override
    public String toString() {
        return String.format("Pet[nome='%s', limpo=%s]", name, clean ? "Sim" : "Não");
    }
}
