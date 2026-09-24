package com.dio.maquinapet.maquina;

import com.dio.maquinapet.excecao.MaquinaOcupadaException;
import com.dio.maquinapet.excecao.MaquinaSujaException;
import com.dio.maquinapet.excecao.MaquinaVaziaException;
import com.dio.maquinapet.excecao.RecursosInsuficientesException;
import com.dio.maquinapet.modelo.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Máquina de Banho Pet (Regras de Negócio e Encapsulamento)")
class MaquinaBanhoPetTest {

    private MaquinaBanhoPet maquina;
    private Pet rex;

    @BeforeEach
    void iniciar() {
        maquina = new MaquinaBanhoPet();
        rex = new Pet("Rex");
    }

    @Nested
    @DisplayName("Cenários de Inicialização e Abastecimento")
    class TestesAbastecimento {

        @Test
        @DisplayName("Deve inicializar a máquina limpa e vazia")
        void deveInicializarLimpaEVazia() {
            assertTrue(maquina.estaLimpa(), "A máquina deve nascer limpa.");
            assertFalse(maquina.temPet(), "A máquina deve nascer sem pet.");
            assertNull(maquina.getPet());
            assertEquals(0, maquina.getAgua());
            assertEquals(0, maquina.getShampoo());
        }

        @Test
        @DisplayName("Deve abastecer água respeitando a capacidade máxima de 30L")
        void deveAbastecerAguaComLimite() {
            int adicionados1 = maquina.abastecerAgua(20);
            assertEquals(20, adicionados1);
            assertEquals(20, maquina.getAgua());

            // Tentativa de adicionar mais 20L quando cabem apenas 10L
            int adicionados2 = maquina.abastecerAgua(20);
            assertEquals(10, adicionados2);
            assertEquals(30, maquina.getAgua(), "O nível não deve ultrapassar 30L.");
        }

        @Test
        @DisplayName("Deve abastecer shampoo respeitando a capacidade máxima de 10L")
        void deveAbastecerShampooComLimite() {
            int adicionados1 = maquina.abastecerShampoo(5);
            assertEquals(5, adicionados1);
            assertEquals(5, maquina.getShampoo());

            // Tentativa de adicionar mais 10L quando cabem apenas 5L
            int adicionados2 = maquina.abastecerShampoo(10);
            assertEquals(5, adicionados2);
            assertEquals(10, maquina.getShampoo(), "O nível não deve ultrapassar 10L.");
        }

        @Test
        @DisplayName("Deve rejeitar abastecimento com valores negativos ou zero")
        void deveRejeitarAbastecimentoInvalido() {
            assertThrows(IllegalArgumentException.class, () -> maquina.abastecerAgua(0));
            assertThrows(IllegalArgumentException.class, () -> maquina.abastecerAgua(-5));
            assertThrows(IllegalArgumentException.class, () -> maquina.abastecerShampoo(0));
            assertThrows(IllegalArgumentException.class, () -> maquina.abastecerShampoo(-2));
        }
    }

    @Nested
    @DisplayName("Cenários de Entrada e Saída de Pets")
    class TestesEntradaSaidaPet {

        @Test
        @DisplayName("Deve permitir colocar um pet quando a máquina estiver limpa e vazia")
        void deveColocarPetComSucesso() {
            maquina.colocarPet(rex);
            assertTrue(maquina.temPet());
            assertEquals(rex, maquina.getPet());
        }

        @Test
        @DisplayName("Deve rejeitar entrada de pet nulo")
        void deveRejeitarPetNulo() {
            assertThrows(IllegalArgumentException.class, () -> maquina.colocarPet(null));
        }

        @Test
        @DisplayName("Deve impedir entrada de um pet quando a máquina já estiver ocupada")
        void deveImpedirEntradaQuandoOcupada() {
            maquina.colocarPet(rex);
            Pet bob = new Pet("Bob");

            assertThrows(MaquinaOcupadaException.class, () -> maquina.colocarPet(bob));
        }

        @Test
        @DisplayName("Deve impedir entrada de pet quando a máquina estiver suja")
        void deveImpedirEntradaQuandoSuja() {
            // Prepara a máquina com insumos e executa um banho
            maquina.abastecerAgua(10);
            maquina.abastecerShampoo(2);
            maquina.colocarPet(rex);
            maquina.darBanho();
            maquina.retirarPet();

            assertFalse(maquina.estaLimpa(), "A máquina deve estar suja após o banho.");

            Pet novoPet = new Pet("Thor");
            assertThrows(MaquinaSujaException.class, () -> maquina.colocarPet(novoPet));
        }

        @Test
        @DisplayName("Deve permitir retirar o pet com sucesso")
        void deveRetirarPetComSucesso() {
            maquina.colocarPet(rex);
            Pet removido = maquina.retirarPet();

            assertEquals(rex, removido);
            assertFalse(maquina.temPet());
            assertNull(maquina.getPet());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar retirar pet de máquina vazia")
        void deveLancarExcecaoAoRetirarDeMaquinaVazia() {
            assertThrows(MaquinaVaziaException.class, () -> maquina.retirarPet());
        }
    }

    @Nested
    @DisplayName("Cenários de Execução de Banho")
    class TestesExecucaoBanho {

        @Test
        @DisplayName("Deve executar o banho com sucesso, consumindo água e shampoo e sujando a máquina")
        void deveExecutarBanhoComSucesso() {
            maquina.abastecerAgua(15);
            maquina.abastecerShampoo(5);
            maquina.colocarPet(rex);

            assertFalse(rex.estaLimpo(), "O pet deve iniciar precisando de banho.");

            maquina.darBanho();

            assertTrue(rex.estaLimpo(), "Após o banho, o pet deve estar limpo.");
            assertFalse(maquina.estaLimpa(), "Após o banho, a máquina deve estar suja.");
            assertEquals(5, maquina.getAgua(), "Devem sobrar 5L de água (15 - 10).");
            assertEquals(3, maquina.getShampoo(), "Devem sobrar 3L de shampoo (5 - 2).");
        }

        @Test
        @DisplayName("Deve impedir banho quando a máquina estiver vazia")
        void deveImpedirBanhoSemPet() {
            maquina.abastecerAgua(20);
            maquina.abastecerShampoo(5);

            assertThrows(MaquinaVaziaException.class, () -> maquina.darBanho());
        }

        @Test
        @DisplayName("Deve impedir banho quando faltar água")
        void deveImpedirBanhoComAguaInsuficiente() {
            maquina.abastecerAgua(9); // Necessário: 10L
            maquina.abastecerShampoo(5);
            maquina.colocarPet(rex);

            assertThrows(RecursosInsuficientesException.class, () -> maquina.darBanho());
            assertFalse(rex.estaLimpo(), "O pet não deve ter sido limpo.");
        }

        @Test
        @DisplayName("Deve impedir banho quando faltar shampoo")
        void deveImpedirBanhoComShampooInsuficiente() {
            maquina.abastecerAgua(20);
            maquina.abastecerShampoo(1); // Necessário: 2L
            maquina.colocarPet(rex);

            assertThrows(RecursosInsuficientesException.class, () -> maquina.darBanho());
            assertFalse(rex.estaLimpo(), "O pet não deve ter sido limpo.");
        }
    }

    @Nested
    @DisplayName("Cenários de Higienização da Máquina")
    class TestesHigienizacao {

        @Test
        @DisplayName("Deve higienizar a máquina consumindo 3L de água e 1L de shampoo")
        void deveLimparMaquinaComSucesso() {
            // Deixa a máquina suja após um banho
            maquina.abastecerAgua(20);
            maquina.abastecerShampoo(5);
            maquina.colocarPet(rex);
            maquina.darBanho();
            maquina.retirarPet();

            assertFalse(maquina.estaLimpa());
            int aguaAntes = maquina.getAgua(); // 10L
            int shampooAntes = maquina.getShampoo(); // 3L

            maquina.limparMaquina();

            assertTrue(maquina.estaLimpa(), "A máquina deve voltar ao estado limpo.");
            assertEquals(aguaAntes - 3, maquina.getAgua());
            assertEquals(shampooAntes - 1, maquina.getShampoo());
        }

        @Test
        @DisplayName("Deve impedir higienização com pet dentro da máquina")
        void deveImpedirLimpezaComPetDentro() {
            maquina.abastecerAgua(20);
            maquina.abastecerShampoo(5);
            maquina.colocarPet(rex);

            assertThrows(MaquinaOcupadaException.class, () -> maquina.limparMaquina());
        }

        @Test
        @DisplayName("Deve impedir higienização quando não houver insumos suficientes")
        void deveImpedirLimpezaSemInsumos() {
            // Suja a máquina e esgota os insumos
            maquina.abastecerAgua(10);
            maquina.abastecerShampoo(2);
            maquina.colocarPet(rex);
            maquina.darBanho();
            maquina.retirarPet();

            assertEquals(0, maquina.getAgua());
            assertEquals(0, maquina.getShampoo());

            assertThrows(RecursosInsuficientesException.class, () -> maquina.limparMaquina());
        }
    }
}
