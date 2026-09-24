package com.dio.maquinapet.maquina;

import com.dio.maquinapet.excecao.MaquinaOcupadaException;
import com.dio.maquinapet.excecao.MaquinaSujaException;
import com.dio.maquinapet.excecao.MaquinaVaziaException;
import com.dio.maquinapet.excecao.RecursosInsuficientesException;
import com.dio.maquinapet.modelo.Pet;

/**
 * Representa a máquina automatizada de banho para animais de estimação (Pet / Cachorro).
 * <p>
 * Esta classe é o núcleo da demonstração de <strong>Encapsulamento</strong> e <strong>Regras de Negócio</strong>:
 * <ul>
 *   <li>Todos os atributos de estado interno são estritamente {@code private}.</li>
 *   <li>Modificações de estado ocorrem apenas através de métodos de domínio em português que garantem integridade.</li>
 *   <li>Recursos de água e shampoo nunca assumem valores negativos ou ultrapassam as capacidades máximas.</li>
 *   <li>Lança exceções de domínio expressivas quando qualquer regra de negócio é violada.</li>
 * </ul>
 *
 * @author Rudson Lima
 * @version 1.0.0
 */
public class MaquinaBanhoPet {

    // Constantes de Capacidade e Consumo (em Litros)
    public static final int CAPACIDADE_MAXIMA_AGUA = 30;
    public static final int CAPACIDADE_MAXIMA_SHAMPOO = 10;
    public static final int AGUA_POR_BANHO = 10;
    public static final int SHAMPOO_POR_BANHO = 2;
    public static final int AGUA_POR_LIMPEZA = 3;
    public static final int SHAMPOO_POR_LIMPEZA = 1;

    // Estado interno estritamente encapsulado (privado)
    private boolean limpa;
    private int agua;
    private int shampoo;
    private Pet pet;

    /**
     * Inicializa a Máquina de Banho higienizada, vazia e sem insumos.
     */
    public MaquinaBanhoPet() {
        this.limpa = true;
        this.agua = 0;
        this.shampoo = 0;
        this.pet = null;
    }

    /**
     * Inicializa a Máquina de Banho permitindo definir os níveis iniciais de água e shampoo.
     *
     * @param aguaInicial    Quantidade inicial de água em litros.
     * @param shampooInicial Quantidade inicial de shampoo em litros.
     */
    public MaquinaBanhoPet(int aguaInicial, int shampooInicial) {
        this.limpa = true;
        this.pet = null;
        this.agua = Math.max(0, Math.min(aguaInicial, CAPACIDADE_MAXIMA_AGUA));
        this.shampoo = Math.max(0, Math.min(shampooInicial, CAPACIDADE_MAXIMA_SHAMPOO));
    }

    /**
     * Executa o ciclo automatizado de banho no pet que estiver dentro da máquina.
     *
     * @throws MaquinaVaziaException          se não houver pet na máquina.
     * @throws RecursosInsuficientesException se faltar água ou shampoo.
     */
    public void darBanho() {
        if (!temPet()) {
            throw new MaquinaVaziaException("Não é possível iniciar o banho: a máquina está vazia.");
        }

        if (this.agua < AGUA_POR_BANHO) {
            throw new RecursosInsuficientesException(String.format(
                    "Água insuficiente para o banho! Necessário: %dL | Disponível: %dL. Abasteça a máquina.",
                    AGUA_POR_BANHO, this.agua));
        }

        if (this.shampoo < SHAMPOO_POR_BANHO) {
            throw new RecursosInsuficientesException(String.format(
                    "Shampoo insuficiente para o banho! Necessário: %dL | Disponível: %dL. Abasteça a máquina.",
                    SHAMPOO_POR_BANHO, this.shampoo));
        }

        // Executa o banho, consome insumos e atualiza os estados do pet e da máquina
        this.agua -= AGUA_POR_BANHO;
        this.shampoo -= SHAMPOO_POR_BANHO;
        this.pet.setLimpo(true);
        this.limpa = false; // A máquina fica suja após o banho do pet
    }

    /**
     * Insere um pet na máquina para início do atendimento.
     *
     * @param pet Animal a ser colocado na máquina.
     * @throws IllegalArgumentException se o pet informado for nulo.
     * @throws MaquinaOcupadaException  se a máquina já contiver um animal.
     * @throws MaquinaSujaException     se a máquina estiver suja do banho anterior.
     */
    public void colocarPet(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("O pet fornecido não pode ser nulo.");
        }

        if (temPet()) {
            throw new MaquinaOcupadaException(String.format(
                    "A máquina já está ocupada pelo pet '%s'. Retire-o antes de colocar outro.",
                    this.pet.getNome()));
        }

        if (!this.limpa) {
            throw new MaquinaSujaException(
                    "A máquina está suja do banho anterior! Execute o ciclo de limpeza antes de colocar um novo pet.");
        }

        this.pet = pet;
    }

    /**
     * Retira o pet atualmente presente na máquina.
     *
     * @return O objeto {@link Pet} retirado.
     * @throws MaquinaVaziaException se a máquina estiver vazia.
     */
    public Pet retirarPet() {
        if (!temPet()) {
            throw new MaquinaVaziaException("Não há pet na máquina para ser retirado.");
        }

        Pet petRemovido = this.pet;
        this.pet = null;
        return petRemovido;
    }

    /**
     * Abastece o reservatório de água da máquina.
     *
     * @param litros Quantidade em litros a abastecer.
     * @return Quantidade real de litros adicionada (respeitando o teto de 30L).
     * @throws IllegalArgumentException se a quantidade for menor ou igual a zero.
     */
    public int abastecerAgua(int litros) {
        if (litros <= 0) {
            throw new IllegalArgumentException("A quantidade de água a ser abastecida deve ser positiva.");
        }

        int espacoDisponivel = CAPACIDADE_MAXIMA_AGUA - this.agua;
        int quantidadeEfetiva = Math.min(litros, espacoDisponivel);
        this.agua += quantidadeEfetiva;
        return quantidadeEfetiva;
    }

    /**
     * Abastece o reservatório de shampoo da máquina.
     *
     * @param litros Quantidade em litros a abastecer.
     * @return Quantidade real de litros adicionada (respeitando o teto de 10L).
     * @throws IllegalArgumentException se a quantidade for menor ou igual a zero.
     */
    public int abastecerShampoo(int litros) {
        if (litros <= 0) {
            throw new IllegalArgumentException("A quantidade de shampoo a ser abastecida deve ser positiva.");
        }

        int espacoDisponivel = CAPACIDADE_MAXIMA_SHAMPOO - this.shampoo;
        int quantidadeEfetiva = Math.min(litros, espacoDisponivel);
        this.shampoo += quantidadeEfetiva;
        return quantidadeEfetiva;
    }

    /**
     * Executa a autolimpeza e higienização da câmara interna da máquina.
     *
     * @throws MaquinaOcupadaException        se houver um pet dentro da máquina.
     * @throws RecursosInsuficientesException se faltar água ou shampoo para a limpeza.
     */
    public void limparMaquina() {
        if (temPet()) {
            throw new MaquinaOcupadaException(
                    "Operação abortada: Não é permitido higienizar a máquina com o pet em seu interior!");
        }

        if (this.limpa) {
            return; // A máquina já está higienizada
        }

        if (this.agua < AGUA_POR_LIMPEZA) {
            throw new RecursosInsuficientesException(String.format(
                    "Água insuficiente para limpar a máquina! Necessário: %dL | Disponível: %dL.",
                    AGUA_POR_LIMPEZA, this.agua));
        }

        if (this.shampoo < SHAMPOO_POR_LIMPEZA) {
            throw new RecursosInsuficientesException(String.format(
                    "Shampoo insuficiente para limpar a máquina! Necessário: %dL | Disponível: %dL.",
                    SHAMPOO_POR_LIMPEZA, this.shampoo));
        }

        this.agua -= AGUA_POR_LIMPEZA;
        this.shampoo -= SHAMPOO_POR_LIMPEZA;
        this.limpa = true;
    }

    /**
     * Informa se há um animal dentro da máquina.
     *
     * @return {@code true} se houver pet, {@code false} se estiver vazia.
     */
    public boolean temPet() {
        return this.pet != null;
    }

    public boolean estaLimpa() {
        return limpa;
    }

    public int getAgua() {
        return agua;
    }

    public int getShampoo() {
        return shampoo;
    }

    public Pet getPet() {
        return pet;
    }

    /**
     * Gera um relatório estruturado do estado da máquina no terminal.
     *
     * @return String formatada com os indicadores de níveis, higiene e ocupação.
     */
    public String gerarRelatorioStatus() {
        return String.format(
                """
                +----------------- PAINEL DA MÁQUINA -----------------+
                | Higienização da Máquina: %-26s |
                | Nível de Água:          %2d / %-2d Litros (%3d%%)       |
                | Nível de Shampoo:       %2d / %-2d Litros (%3d%%)       |
                | Ocupação:               %-26s |
                +-----------------------------------------------------+""",
                limpa ? "LIMPA (Pronta para uso)" : "SUJA (Precisa de limpeza)",
                agua, CAPACIDADE_MAXIMA_AGUA, (agua * 100) / CAPACIDADE_MAXIMA_AGUA,
                shampoo, CAPACIDADE_MAXIMA_SHAMPOO, (shampoo * 100) / CAPACIDADE_MAXIMA_SHAMPOO,
                temPet() ? pet.getNome() + " (" + (pet.estaLimpo() ? "Limpo" : "Precisando de Banho") + ")" : "Vazia"
        );
    }
}
