package com.dio.petmachine;

import com.dio.petmachine.exception.PetMachineException;
import com.dio.petmachine.machine.PetMachine;
import com.dio.petmachine.model.Pet;

import java.util.Scanner;

/**
 * Ponto de entrada (Main) do Simulador de Máquina de Banho Pet.
 * <p>
 * Apresenta um menu interativo no console permitindo ao usuário operar a máquina,
 * abastecer insumos, monitorar níveis e realizar banhos em animais.
 *
 * @author Rudson Lima
 * @version 1.0.0
 */
public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PetMachine machine = new PetMachine(10, 2); // Inicia já com 1 banho disponível como cortesia de teste

    public static void main(String[] args) {
        exibirBoasVindas();

        boolean running = true;
        while (running) {
            exibirMenu();
            int opcao = lerOpcao();

            try {
                switch (opcao) {
                    case 1 -> executarBanho();
                    case 2 -> abastecerAgua();
                    case 3 -> abastecerShampoo();
                    case 4 -> verificarNivelAgua();
                    case 5 -> verificarNivelShampoo();
                    case 6 -> verificarOcupacao();
                    case 7 -> colocarPet();
                    case 8 -> retirarPet();
                    case 9 -> limparMaquina();
                    case 10 -> exibirPainelCompleto();
                    case 0 -> {
                        running = false;
                        System.out.println("\n[SISTEMA] Encerrando o simulador Pet Machine. Até logo!");
                    }
                    default -> System.out.println("\n[AVISO] Opção inválida! Escolha um número entre 0 e 10.");
                }
            } catch (PetMachineException | IllegalArgumentException e) {
                System.out.printf("\n[FALHA NA OPERAÇÃO]: %s%n", e.getMessage());
            }

            if (running) {
                pausar();
            }
        }

        scanner.close();
    }

    private static void exibirBoasVindas() {
        System.out.println("""
            =============================================================
               🐾 PET MACHINE SIMULATOR - SISTEMA DE BANHO EM PETS 🐾
               Bootcamp Itaú & Santander Java | Demonstração de POO
            =============================================================""");
    }

    private static void exibirMenu() {
        System.out.println("""
            
            ----------------------- MENU PRINCIPAL ----------------------
             1 - Dar banho no pet
             2 - Abastecer água (+L)
             3 - Abastecer shampoo (+L)
             4 - Verificar nível de água
             5 - Verificar nível de shampoo
             6 - Verificar se tem pet na máquina
             7 - Colocar pet na máquina
             8 - Retirar pet da máquina
             9 - Higienizar e limpar a máquina
            10 - Exibir painel completo de status
             0 - Sair do programa
            -------------------------------------------------------------""");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void executarBanho() {
        System.out.println("\n[AÇÃO] Iniciando ciclo de banho automatizado...");
        machine.takeAShower();
        System.out.printf("[SUCESSO] Banho concluído com sucesso no pet '%s'! O pet está 100%% limpo e cheiroso.%n",
                machine.getPet().getName());
        System.out.println("[INFO] Atenção: A máquina agora está suja e precisará ser higienizada após a saída do pet.");
    }

    private static void abastecerAgua() {
        System.out.printf("Informe a quantidade de água a abastecer em litros (Capacidade máxima: %dL | Atual: %dL): ",
                PetMachine.MAX_WATER_CAPACITY, machine.getWater());
        int litros = lerInteiroPositivo();
        if (litros > 0) {
            int adicionados = machine.addWater(litros);
            System.out.printf("[SUCESSO] Foram adicionados %dL de água. Nível atual: %dL/%dL.%n",
                    adicionados, machine.getWater(), PetMachine.MAX_WATER_CAPACITY);
            if (adicionados < litros) {
                System.out.println("[INFO] O reservatório atingiu a capacidade máxima e o excedente foi dispensado.");
            }
        }
    }

    private static void abastecerShampoo() {
        System.out.printf("Informe a quantidade de shampoo a abastecer em litros (Capacidade máxima: %dL | Atual: %dL): ",
                PetMachine.MAX_SHAMPOO_CAPACITY, machine.getShampoo());
        int litros = lerInteiroPositivo();
        if (litros > 0) {
            int adicionados = machine.addShampoo(litros);
            System.out.printf("[SUCESSO] Foram adicionados %dL de shampoo. Nível atual: %dL/%dL.%n",
                    adicionados, machine.getShampoo(), PetMachine.MAX_SHAMPOO_CAPACITY);
            if (adicionados < litros) {
                System.out.println("[INFO] O reservatório atingiu a capacidade máxima e o excedente foi dispensado.");
            }
        }
    }

    private static void verificarNivelAgua() {
        System.out.printf("%n[STATUS] Nível de Água: %dL de %dL (%.1f%% preenchido)%n",
                machine.getWater(),
                PetMachine.MAX_WATER_CAPACITY,
                (machine.getWater() * 100.0) / PetMachine.MAX_WATER_CAPACITY);
    }

    private static void verificarNivelShampoo() {
        System.out.printf("%n[STATUS] Nível de Shampoo: %dL de %dL (%.1f%% preenchido)%n",
                machine.getShampoo(),
                PetMachine.MAX_SHAMPOO_CAPACITY,
                (machine.getShampoo() * 100.0) / PetMachine.MAX_SHAMPOO_CAPACITY);
    }

    private static void verificarOcupacao() {
        if (machine.hasPet()) {
            Pet pet = machine.getPet();
            System.out.printf("%n[OCUPAÇÃO] Há um pet na máquina: '%s' | Status: %s%n",
                    pet.getName(), pet.isClean() ? "Limpo" : "Precisando de banho");
        } else {
            System.out.println("\n[OCUPAÇÃO] A máquina está livre (vazia).");
        }
    }

    private static void colocarPet() {
        System.out.print("Informe o nome do pet a ser colocado na máquina: ");
        String nome = scanner.nextLine().trim();
        Pet pet = new Pet(nome);
        machine.setPet(pet);
        System.out.printf("[SUCESSO] Pet '%s' colocado com segurança na máquina!%n", pet.getName());
    }

    private static void retirarPet() {
        Pet pet = machine.removePet();
        System.out.printf("[SUCESSO] Pet '%s' retirado da máquina! Estado: %s%n",
                pet.getName(), pet.isClean() ? "Limpo e cheiroso!" : "Ainda sujo (o banho não foi concluído).");
    }

    private static void limparMaquina() {
        System.out.println("\n[AÇÃO] Executando ciclo de higienização interna da máquina...");
        machine.cleanMachine();
        System.out.println("[SUCESSO] Máquina higienizada e pronta para receber o próximo pet!");
    }

    private static void exibirPainelCompleto() {
        System.out.println();
        System.out.println(machine.getStatusReport());
    }

    private static int lerInteiroPositivo() {
        try {
            String input = scanner.nextLine().trim();
            int valor = Integer.parseInt(input);
            if (valor <= 0) {
                System.out.println("[AVISO] O valor deve ser maior que zero.");
                return -1;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("[AVISO] Entrada inválida! Digite apenas números inteiros.");
            return -1;
        }
    }

    private static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
