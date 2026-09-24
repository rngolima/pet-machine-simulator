# 🧠 Manual Teórico — POO Avançada & Encapsulamento Estrito

> **PROJETO:** Simulador de Máquina de Banho em Pets  
> **ÁREA:** Engenharia de Software & Programação Orientada a Objetos em Java 21 LTS  
> **AUTOR:** Rudson Americo ([GitHub](https://github.com/rngolima))

---

## 🏛️ 1. O Que É Encapsulamento e Por Que Ele É Crítico?

Na Programação Orientada a Objetos, o **Encapsulamento** não se resume a colocar atributos como `private` e criar `getters` e `setters` para todos eles. Fazer isso gera o que o mercado chama de **Modelo Anêmico (*Anemic Domain Model*)** — um objeto sem proteção real que qualquer código externo pode alterar indiscriminadamente.

### 🚫 Exemplo de Código Frágil (Anti-Padrão):
```java
// RUIM: Qualquer classe externa altera o estado sem validação
maquina.setAgua(-500); 
maquina.setLimpa(true);
```

### ✅ O Padrão Adotado no Pet Machine (Modelo Rico):
```java
// BOM: O método expressa uma operação de negócio e valida os limites
maquina.abastecerAgua(15); 
maquina.limparMaquina(); // Valida se não há pet dentro e consome água/shampoo
```

---

## ⚡ 2. O Princípio Fail-Fast

O princípio **Fail-Fast (Falhe Rápido)** prega que qualquer violação de regra de negócio deve interromper a execução imediatamente no ponto onde o erro ocorreu, impedindo que dados corrompidos trafeguem pelo sistema.

### Comparativo:

| Abordagem | Como Funciona | Por que Evitamos / Adotamos |
| :--- | :--- | :--- |
| **Retorno de Booleano (`false`)** | Retorna `false` se falhar | ❌ O chamador pode esquecer de verificar o `if` e continuar a execução com dados inválidos. |
| **Códigos de Erro (-1, 404)** | Retorna números mágicos | ❌ Difícil manutenção e falta de expressividade semântica. |
| **Exceções de Domínio (Fail-Fast)** | Lança `MaquinaSujaException` | ✅ **Adotado.** O fluxo é cortado na hora com uma mensagem clara e contexto completo. |

---

## 🧪 3. Como Estruturar Testes Unitários de Alta Legibilidade com JUnit 5

No projeto, utilizamos a anotação `@Nested` do JUnit 5 para criar classes de teste internas contextuais:

```java
@Nested
@DisplayName("Testes de Abastecimento")
class TestesAbastecimento {
    @Test
    @DisplayName("Deve respeitar o limite máximo de água sem transbordar")
    void deveRespeitarCapacidadeMaximaAgua() { ... }
}

@Nested
@DisplayName("Testes de Higienização")
class TestesHigienizacao { ... }
```

### Benefício:
O relatório do Maven Surefire imprime os testes agrupados como uma especificação executável (*Living Documentation*), permitindo que qualquer desenvolvedor ou recrutador entenda exatamente o que está sendo testado.
