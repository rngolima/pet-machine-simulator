package com.dio.maquinapet.modelo;

import java.util.Objects;

/**
 * Representa um animal de estimação (Pet / Cachorro) que utiliza o serviço de banho automatizado.
 * <p>
 * Demonstra encapsulamento e proteção do estado interno:
 * <ul>
 *   <li>Atributos privados {@code nome} e {@code limpo}.</li>
 *   <li>Validação defensiva no construtor para evitar instâncias com dados inválidos.</li>
 *   <li>Métodos de acesso controlados em português.</li>
 * </ul>
 *
 * @author Rudson Lima
 * @version 1.0.0
 */
public class Pet {

    private final String nome;
    private boolean limpo;

    /**
     * Construtor para inicializar um Pet.
     * Por padrão, todo Pet chega para atendimento precisando de banho ({@code limpo = false}).
     *
     * @param nome Nome do pet (não pode ser nulo ou em branco).
     * @throws IllegalArgumentException se o nome for nulo ou composto apenas por espaços.
     */
    public Pet(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do pet não pode ser nulo ou vazio.");
        }
        this.nome = nome.trim();
        this.limpo = false;
    }

    /**
     * Construtor alternativo permitindo definir o estado inicial de limpeza.
     *
     * @param nome  Nome do pet.
     * @param limpo Estado inicial de limpeza (true = limpo, false = sujo).
     */
    public Pet(String nome, boolean limpo) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do pet não pode ser nulo ou vazio.");
        }
        this.nome = nome.trim();
        this.limpo = limpo;
    }

    public String getNome() {
        return nome;
    }

    public boolean estaLimpo() {
        return limpo;
    }

    public void setLimpo(boolean limpo) {
        this.limpo = limpo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet pet)) return false;
        return limpo == pet.limpo && Objects.equals(nome, pet.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, limpo);
    }

    @Override
    public String toString() {
        return String.format("Pet[nome='%s', limpo=%s]", nome, limpo ? "Sim" : "Não");
    }
}
