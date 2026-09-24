package com.dio.petmachine.machine;

import com.dio.petmachine.exception.InsufficientResourcesException;
import com.dio.petmachine.exception.MachineEmptyException;
import com.dio.petmachine.exception.MachineNotCleanException;
import com.dio.petmachine.exception.MachineOccupiedException;
import com.dio.petmachine.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Pet Machine (Regras de Negócio e Encapsulamento)")
class PetMachineTest {

    private PetMachine machine;
    private Pet rex;

    @BeforeEach
    void setUp() {
        machine = new PetMachine();
        rex = new Pet("Rex");
    }

    @Nested
    @DisplayName("Cenários de Inicialização e Abastecimento")
    class AbastecimentoTests {

        @Test
        @DisplayName("Deve inicializar a máquina limpa e sem pet")
        void deveInicializarLimpaEVazia() {
            assertTrue(machine.isClean(), "A máquina deve nascer limpa.");
            assertFalse(machine.hasPet(), "A máquina deve nascer sem pet.");
            assertNull(machine.getPet());
            assertEquals(0, machine.getWater());
            assertEquals(0, machine.getShampoo());
        }

        @Test
        @DisplayName("Deve abastecer água respeitando a capacidade máxima de 30L")
        void deveAbastecerAguaComLimite() {
            int adicionados1 = machine.addWater(20);
            assertEquals(20, adicionados1);
            assertEquals(20, machine.getWater());

            // Tentativa de adicionar mais 20L quando cabem apenas 10L
            int adicionados2 = machine.addWater(20);
            assertEquals(10, adicionados2);
            assertEquals(30, machine.getWater(), "O nível não deve ultrapassar 30L.");
        }

        @Test
        @DisplayName("Deve abastecer shampoo respeitando a capacidade máxima de 10L")
        void deveAbastecerShampooComLimite() {
            int adicionados1 = machine.addShampoo(5);
            assertEquals(5, adicionados1);
            assertEquals(5, machine.getShampoo());

            // Tentativa de adicionar mais 10L quando cabem apenas 5L
            int adicionados2 = machine.addShampoo(10);
            assertEquals(5, adicionados2);
            assertEquals(10, machine.getShampoo(), "O nível não deve ultrapassar 10L.");
        }

        @Test
        @DisplayName("Deve rejeitar abastecimento com valores negativos ou zero")
        void deveRejeitarAbastecimentoInvalido() {
            assertThrows(IllegalArgumentException.class, () -> machine.addWater(0));
            assertThrows(IllegalArgumentException.class, () -> machine.addWater(-5));
            assertThrows(IllegalArgumentException.class, () -> machine.addShampoo(0));
            assertThrows(IllegalArgumentException.class, () -> machine.addShampoo(-2));
        }
    }

    @Nested
    @DisplayName("Cenários de Entrada e Saída de Pets")
    class EntradaSaidaPetTests {

        @Test
        @DisplayName("Deve permitir colocar um pet quando a máquina estiver limpa e vazia")
        void deveColocarPetComSucesso() {
            machine.setPet(rex);
            assertTrue(machine.hasPet());
            assertEquals(rex, machine.getPet());
        }

        @Test
        @DisplayName("Deve rejeitar entrada de pet nulo")
        void deveRejeitarPetNulo() {
            assertThrows(IllegalArgumentException.class, () -> machine.setPet(null));
        }

        @Test
        @DisplayName("Deve impedir entrada de um pet quando a máquina já estiver ocupada")
        void deveImpedirEntradaQuandoOcupada() {
            machine.setPet(rex);
            Pet bob = new Pet("Bob");

            assertThrows(MachineOccupiedException.class, () -> machine.setPet(bob));
        }

        @Test
        @DisplayName("Deve impedir entrada de pet quando a máquina estiver suja")
        void deveImpedirEntradaQuandoSuja() {
            // Prepara a máquina com insumos e executa um banho
            machine.addWater(10);
            machine.addShampoo(2);
            machine.setPet(rex);
            machine.takeAShower();
            machine.removePet();

            assertFalse(machine.isClean(), "A máquina deve estar suja após o banho.");

            Pet novoPet = new Pet("Thor");
            assertThrows(MachineNotCleanException.class, () -> machine.setPet(novoPet));
        }

        @Test
        @DisplayName("Deve permitir retirar o pet com sucesso")
        void deveRetirarPetComSucesso() {
            machine.setPet(rex);
            Pet removido = machine.removePet();

            assertEquals(rex, removido);
            assertFalse(machine.hasPet());
            assertNull(machine.getPet());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar retirar pet de máquina vazia")
        void deveLancarExcecaoAoRetirarDeMaquinaVazia() {
            assertThrows(MachineEmptyException.class, () -> machine.removePet());
        }
    }

    @Nested
    @DisplayName("Cenários de Execução de Banho")
    class ExecucaoBanhoTests {

        @Test
        @DisplayName("Deve executar o banho com sucesso, consumindo água e shampoo e sujando a máquina")
        void deveExecutarBanhoComSucesso() {
            machine.addWater(15);
            machine.addShampoo(5);
            machine.setPet(rex);

            assertFalse(rex.isClean(), "O pet deve iniciar precisando de banho.");

            machine.takeAShower();

            assertTrue(rex.isClean(), "Após o banho, o pet deve estar limpo.");
            assertFalse(machine.isClean(), "Após o banho, a máquina deve estar suja.");
            assertEquals(5, machine.getWater(), "Devem sobrar 5L de água (15 - 10).");
            assertEquals(3, machine.getShampoo(), "Devem sobrar 3L de shampoo (5 - 2).");
        }

        @Test
        @DisplayName("Deve impedir banho quando a máquina estiver vazia")
        void deveImpedirBanhoSemPet() {
            machine.addWater(20);
            machine.addShampoo(5);

            assertThrows(MachineEmptyException.class, () -> machine.takeAShower());
        }

        @Test
        @DisplayName("Deve impedir banho quando faltar água")
        void deveImpedirBanhoComAguaInsuficiente() {
            machine.addWater(9); // Necessário: 10L
            machine.addShampoo(5);
            machine.setPet(rex);

            assertThrows(InsufficientResourcesException.class, () -> machine.takeAShower());
            assertFalse(rex.isClean(), "O pet não deve ter sido limpo.");
        }

        @Test
        @DisplayName("Deve impedir banho quando faltar shampoo")
        void deveImpedirBanhoComShampooInsuficiente() {
            machine.addWater(20);
            machine.addShampoo(1); // Necessário: 2L
            machine.setPet(rex);

            assertThrows(InsufficientResourcesException.class, () -> machine.takeAShower());
            assertFalse(rex.isClean(), "O pet não deve ter sido limpo.");
        }
    }

    @Nested
    @DisplayName("Cenários de Higienização da Máquina")
    class HigienizacaoTests {

        @Test
        @DisplayName("Deve higienizar a máquina consumindo 3L de água e 1L de shampoo")
        void deveLimparMaquinaComSucesso() {
            // Deixa a máquina suja
            machine.addWater(20);
            machine.addShampoo(5);
            machine.setPet(rex);
            machine.takeAShower();
            machine.removePet();

            assertFalse(machine.isClean());
            int aguaAntes = machine.getWater(); // 10L
            int shampooAntes = machine.getShampoo(); // 3L

            machine.cleanMachine();

            assertTrue(machine.isClean(), "A máquina deve voltar ao estado limpo.");
            assertEquals(aguaAntes - 3, machine.getWater());
            assertEquals(shampooAntes - 1, machine.getShampoo());
        }

        @Test
        @DisplayName("Deve impedir higienização com pet dentro da máquina")
        void deveImpedirLimpezaComPetDentro() {
            machine.addWater(20);
            machine.addShampoo(5);
            machine.setPet(rex);

            assertThrows(MachineOccupiedException.class, () -> machine.cleanMachine());
        }

        @Test
        @DisplayName("Deve impedir higienização quando não houver insumos suficientes")
        void deveImpedirLimpezaSemInsumos() {
            // Suja a máquina
            machine.addWater(10);
            machine.addShampoo(2);
            machine.setPet(rex);
            machine.takeAShower();
            machine.removePet();

            // Água zerou e shampoo zerou
            assertEquals(0, machine.getWater());
            assertEquals(0, machine.getShampoo());

            assertThrows(InsufficientResourcesException.class, () -> machine.cleanMachine());
        }
    }
}
