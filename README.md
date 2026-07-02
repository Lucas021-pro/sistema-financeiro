# 🏦 Sistema de Gestão Financeira (JavaFX)

> Uma aplicação desktop robusta para gestão bancária e simulação financeira, construída com foco em **Arquitetura de Software**, **Design Patterns** e **Boas Práticas de Orientação a Objetos**.

Este projeto foi desenvolvido como requisito acadêmico, mas estruturado com padrões de mercado corporativo (Domain-Driven Package Structure). Ele simula o ecossistema de um banco, permitindo o gerenciamento de clientes, diferentes tipos de contas e operações financeiras.

## Funcionalidades Principais

* **Gestão de Clientes (CRUD Completo):** Cadastro, listagem, edição e exclusão segura de clientes com validação rigorosa de dados (RegEx para CPF e Nome).
* **Múltiplos Tipos de Conta:**
  * **Conta Corrente:** Com cálculo automático de Limite de Cheque Especial e deduções corretas em caso de saldo negativo.
  * **Conta Poupança & Investimento:** Com implementação de interface de rentabilidade e taxas dinâmicas.
* **Operações Bancárias:** Saques, depósitos e transferências seguras com validação matemática de regras de negócio.
* **Simulador Financeiro:** Módulo de simulação de amortização de empréstimos e projeção de juros.
* **UX/UI Defensiva:** Alertas nativos de confirmação para ações destrutivas (ex: exclusão de clientes) prevenindo *misclicks*.

---

## Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Interface Gráfica:** JavaFX (com FXML e SceneBuilder)
* **Banco de Dados:** MySQL 8.0
* **Gerenciamento de Dependências:** Maven
* **Testes de Software:** JUnit 5 (Testes Unitários)
* **Controle de Versão:** Git & GitHub

---

## Arquitetura e Padrões de Projeto

O sistema foi desenhado para garantir o máximo de isolamento e baixo acoplamento:

* **Padrão MVC (Model-View-Controller):** Separação estrita onde a `View` (FXML) é passiva, os `Controllers` atuam de forma magra (*Thin Controllers*), e a lógica permanece isolada no `Model`.
* **Padrão DAO (Data Access Object):** Todo o acesso ao banco de dados foi abstraído em interfaces e implementações DAO, tornando o sistema agnóstico à tecnologia de persistência.
* **Service-Repository:** Regras de transação complexas foram movidas para classes de Serviço (ex: `BancoService`), evitando "Controllers Gordos".
* **Segurança de Dados:** Uso exclusivo de `PreparedStatement` nas transações SQL para prevenir ataques de *SQL Injection*.
* **Gerenciamento de Recursos:** Utilização de blocos `try-with-resources` para garantir o fechamento de Sockets TCP/IP com o MySQL, prevenindo *Memory Leaks*.

---

## Testes Unitários

O núcleo do sistema está coberto por testes unitários focados nas regras de negócio e validações críticas, garantindo a integridade dos cálculos financeiros e a segurança da porta de entrada do banco de dados (rejeitando CPFs inválidos ou formatos textuais maliciosos).

---

## Como Executar o Projeto Localmente

**Pré-requisitos:** Java 21+, Maven e MySQL instalados.

1. Clone este repositório:
   ```bash
   git clone [https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git](https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git)
