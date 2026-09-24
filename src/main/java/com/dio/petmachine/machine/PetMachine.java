package com.dio.petmachine.machine;

import com.dio.petmachine.exception.InsufficientResourcesException;
import com.dio.petmachine.exception.MachineEmptyException;
import com.dio.petmachine.exception.MachineNotCleanException;
import com.dio.petmachine.exception.MachineOccupiedException;
import com.dio.petmachine.model.Pet;

/**
 * Representa a máquina automatizada de banho para animais de estimação (Pet Machine).
 * <p>
 * Esta classe é o núcleo da demonstração de <strong>Encapsulamento</strong> e <strong>Regras de Negócio</strong>:
 * <ul>
 *   <li>Todos os atributos de estado interno são estritamente {@code private}.</li>
 *   <li>Modificações de estado ocorrem apenas através de métodos de domínio que garantem invariantes válidos.</li>
 *   <li>Recursos de água e shampoo nunca assumem valores negativos ou extrapolam suas capacidades máximas.</li>
 *   <li>Lança exceções de domínio expressivas para violações de pré-requisitos de negócio.</li>
 * </ul>
 *
 * @author Rudson Lima
 * @version 1.0.0
 */
public class PetMachine {

    // Constantes de Capacidade e Consumo (em Litros)
    public static final int MAX_WATER_CAPACITY = 30;
    public static final int MAX_SHAMPOO_CAPACITY = 10;
    public static final int WATER_PER_SHOWER = 10;
    public static final int SHAMPOO_PER_SHOWER = 2;
    public static final int WATER_PER_CLEAN = 3;
    public static final int SHAMPOO_PER_CLEAN = 1;

    // Estado interno estritamente encapsulado
    private boolean clean;
    private int water;
    private int shampoo;
    private Pet pet;

    /**
     * Inicializa a Pet Machine com reservatórios vazios e higienizada para o primeiro uso.
     */
    public PetMachine() {
        this.clean = true;
        this.water = 0;
        this.shampoo = 0;
        this.pet = null;
    }

    /**
     * Inicializa a Pet Machine permitindo configurar os níveis iniciais de água e shampoo.
     *
     * @param initialWater   Nível inicial de água (será limitado à capacidade máxima).
     * @param initialShampoo Nível inicial de shampoo (será limitado à capacidade máxima).
     */
    public PetMachine(int initialWater, int initialShampoo) {
        this.clean = true;
        this.pet = null;
        this.water = Math.max(0, Math.min(initialWater, MAX_WATER_CAPACITY));
        this.shampoo = Math.max(0, Math.min(initialShampoo, MAX_SHAMPOO_CAPACITY));
    }

    /**
     * Executa o ciclo de banho no pet que estiver dentro da máquina.
     *
     * @throws MachineEmptyException          se a máquina estiver vazia.
     * @throws InsufficientResourcesException se não houver água ou shampoo suficientes.
     */
    public void takeAShower() {
        if (!hasPet()) {
            throw new MachineEmptyException("Não é possível iniciar o banho: a máquina está vazia.");
        }

        if (this.water < WATER_PER_SHOWER) {
            throw new InsufficientResourcesException(String.format(
                    "Água insuficiente para o banho! Necessário: %dL | Disponível: %dL. Abasteça a máquina.",
                    WATER_PER_SHOWER, this.water));
        }

        if (this.shampoo < SHAMPOO_PER_SHOWER) {
            throw new InsufficientResourcesException(String.format(
                    "Shampoo insuficiente para o banho! Necessário: %dL | Disponível: %dL. Abasteça a máquina.",
                    SHAMPOO_PER_SHOWER, this.shampoo));
        }

        // Executa o banho, consome insumos e atualiza estados
        this.water -= WATER_PER_SHOWER;
        this.shampoo -= SHAMPOO_PER_SHOWER;
        this.pet.setClean(true);
        this.clean = false; // A máquina fica suja após o banho do pet
    }

    /**
     * Insere um pet na máquina para início do atendimento.
     *
     * @param pet Animal a ser colocado na máquina.
     * @throws IllegalArgumentException se o pet for nulo.
     * @throws MachineOccupiedException se a máquina já contiver um animal.
     * @throws MachineNotCleanException se a máquina estiver suja de um banho anterior.
     */
    public void setPet(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("O pet fornecido não pode ser nulo.");
        }

        if (hasPet()) {
            throw new MachineOccupiedException(String.format(
                    "A máquina já está ocupada pelo pet '%s'. Retire-o antes de colocar outro.",
                    this.pet.getName()));
        }

        if (!this.clean) {
            throw new MachineNotCleanException(
                    "A máquina está suja do banho anterior! Execute o ciclo de limpeza antes de colocar um novo pet.");
        }

        this.pet = pet;
    }

    /**
     * Retira o pet atualmente presente na máquina.
     *
     * @return O objeto {@link Pet} retirado.
     * @throws MachineEmptyException se a máquina estiver vazia.
     */
    public Pet removePet() {
        if (!hasPet()) {
            throw new MachineEmptyException("Não há pet na máquina para ser retirado.");
        }

        Pet petRemovido = this.pet;
        this.pet = null;
        return petRemovido;
    }

    /**
     * Abastece o reservatório de água da máquina.
     *
     * @param liters Quantidade em litros a abastecer.
     * @return A quantidade real de litros adicionada (respeitando a capacidade máxima de 30L).
     * @throws IllegalArgumentException se a quantidade for menor ou igual a zero.
     */
    public int addWater(int liters) {
        if (liters <= 0) {
            throw new IllegalArgumentException("A quantidade de água a ser abastecida deve ser positiva.");
        }

        int espacoDisponivel = MAX_WATER_CAPACITY - this.water;
        int quantidadeEfetiva = Math.min(liters, espacoDisponivel);
        this.water += quantidadeEfetiva;
        return quantidadeEfetiva;
    }

    /**
     * Abastece o reservatório de shampoo da máquina.
     *
     * @param liters Quantidade em litros a abastecer.
     * @return A quantidade real de litros adicionada (respeitando a capacidade máxima de 10L).
     * @throws IllegalArgumentException se a quantidade for menor ou igual a zero.
     */
    public int addShampoo(int liters) {
        if (liters <= 0) {
            throw new IllegalArgumentException("A quantidade de shampoo a ser abastecida deve ser positiva.");
        }

        int espacoDisponivel = MAX_SHAMPOO_CAPACITY - this.shampoo;
        int quantidadeEfetiva = Math.min(liters, espacoDisponivel);
        this.shampoo += quantidadeEfetiva;
        return quantidadeEfetiva;
    }

    /**
     * Executa a autolimpeza e higienização da câmara interna da máquina.
     *
     * @throws MachineOccupiedException       se houver um pet dentro da máquina.
     * @throws InsufficientResourcesException se faltar água ou shampoo para a limpeza.
     */
    public void cleanMachine() {
        if (hasPet()) {
            throw new MachineOccupiedException(
                    "Operação abortada: Não é permitido higienizar a máquina com o pet no seu interior!");
        }

        if (this.clean) {
            return; // A máquina já está higienizada
        }

        if (this.water < WATER_PER_CLEAN) {
            throw new InsufficientResourcesException(String.format(
                    "Água insuficiente para limpar a máquina! Necessário: %dL | Disponível: %dL.",
                    WATER_PER_CLEAN, this.water));
        }

        if (this.shampoo < SHAMPOO_PER_CLEAN) {
            throw new InsufficientResourcesException(String.format(
                    "Shampoo insuficiente para limpar a máquina! Necessário: %dL | Disponível: %dL.",
                    SHAMPOO_PER_CLEAN, this.shampoo));
        }

        this.water -= WATER_PER_CLEAN;
        this.shampoo -= SHAMPOO_PER_CLEAN;
        this.clean = true;
    }

    /**
     * Verifica se há um pet dentro da máquina.
     *
     * @return {@code true} se houver pet presente, {@code false} caso contrário.
     */
    public boolean hasPet() {
        return this.pet != null;
    }

    // Getters de consulta controlada
    public boolean isClean() {
        return clean;
    }

    public int getWater() {
        return water;
    }

    public int getShampoo() {
        return shampoo;
    }

    public Pet getPet() {
        return pet;
    }

    /**
     * Gera um relatório estruturado do estado atual da máquina.
     *
     * @return String formatada com os indicadores de nível, higiene e ocupação.
     */
    public String getStatusReport() {
        return String.format(
                """
                +----------------- PAINEL DA MÁQUINA -----------------+
                | Higienização da Máquina: %-26s |
                | Nível de Água:          %2d / %-2d Litros (%3d%%)       |
                | Nível de Shampoo:       %2d / %-2d Litros (%3d%%)       |
                | Ocupação:               %-26s |
                +-----------------------------------------------------+""",
                clean ? "LIMPA (Pronta para uso)" : "SUJA (Precisa de limpeza)",
                water, MAX_WATER_CAPACITY, (water * 100) / MAX_WATER_CAPACITY,
                shampoo, MAX_SHAMPOO_CAPACITY, (shampoo * 100) / MAX_SHAMPOO_CAPACITY,
                hasPet() ? pet.getName() + " (" + (pet.isClean() ? "Limpo" : "Precisando de Banho") + ")" : "Vazia"
        );
    }
}
