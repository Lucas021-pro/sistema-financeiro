package br.lucas.financeiro.view;

import br.lucas.financeiro.exception.ContaNaoEncontradaException;
import br.lucas.financeiro.exception.SaldoInsuficienteException;
import br.lucas.financeiro.model.*;
import br.lucas.financeiro.service.BancoService;
import br.lucas.financeiro.service.SimuladorFinanceiro;
import br.lucas.financeiro.util.Validador;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TerminalView {
    private final BancoService banco;
    private final SimuladorFinanceiro simulador;
    private final Scanner sc;
    private int geradorIdCliente = 1;

    public TerminalView(BancoService banco, SimuladorFinanceiro simulador) {
        this.banco = banco;
        this.simulador = simulador;
        this.sc = new Scanner(System.in);
    }

    public void iniciar() {
        int escolha = 0;

        while (true) {
            exibirMenuPrincipal();

            try {
                escolha = sc.nextInt();
                sc.nextLine();

                switch (escolha) {
                    case 1:
                        cadastrarNovoCliente();
                        break;
                    case 2:
                        acessarOperacoes();
                        break;
                    case 3:
                        simularFinanciamento();
                        break;
                    case 4:
                        banco.renderContas();
                        break;
                    case 5:
                        System.out.println("Encerrando o sistema...");
                        sc.close();
                        return;
                    default:
                        System.out.println("Opção inválida no menu.");
                }

            } catch (InputMismatchException e) {
                System.out.println("\nErro: Por favor, insira apenas números nos menus e valores numéricos.");
                sc.nextLine();
            } catch (ContaNaoEncontradaException | SaldoInsuficienteException e) {
                System.out.println("\nErro de Operação: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\nOcorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n--- Sistema Financeiro ---");
        System.out.println("1 - Cadastrar Novo Cliente e Conta");
        System.out.println("2 - Acessar Operações (Depósito/Saque/Transferência)");
        System.out.println("3 - Simular Financiamento (Matriz de Amortização)");
        System.out.println("4 - Processar Rendimentos");
        System.out.println("5 - Sair do Sistema");
        System.out.print("Escolha uma opção: ");
    }

    private void cadastrarNovoCliente() {
        String nome = "";
        String cpf = "";

        while (true) {
            try {
                System.out.print("Nome do Cliente: ");
                nome = Validador.validarNomeCompleto(sc.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("CPF (000.000.000-00): ");
                cpf = Validador.validarCpf(sc.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        Cliente novoCliente = new Cliente(nome, cpf, geradorIdCliente++, new ArrayList<>());
        System.out.print("Tipo de Conta (1-Corrente, 2-Poupança, 3-Investimento): ");
        int tipoConta = sc.nextInt();
        sc.nextLine();

        Conta novaConta = null;
        if (tipoConta == 1) novaConta = new ContaCorrente(nome);
        else if (tipoConta == 2) novaConta = new ContaPoupanca(nome);
        else if (tipoConta == 3) novaConta = new ContaInvestimento(nome);
        else {
            System.out.println("Tipo Inválido. Operação cancelada.");
            return;
        }

        novoCliente.adicionarConta(novaConta);
        banco.cadastrarCliente(novoCliente);
        System.out.println("Conta gerada com o número: " + novaConta.getNumeroConta());
    }

    private void acessarOperacoes() throws SaldoInsuficienteException {
        System.out.print("Digite o número da Conta: ");
        int numeroBusca = sc.nextInt();
        Conta contaAcessada = banco.buscarConta(numeroBusca);

        System.out.println("\nTitular: " + contaAcessada.getTitular() + " | Saldo: R$" + contaAcessada.getSaldo());
        System.out.println("1- Depositar\n2- Sacar\n3- Transferir");
        System.out.print("Escolha: ");
        int opcao = sc.nextInt();

        if (opcao == 1) {
            System.out.print("Valor do Depósito: ");
            contaAcessada.depositar(sc.nextDouble());
            System.out.println("Novo Saldo: R$" + contaAcessada.getSaldo());
        } else if (opcao == 2) {
            System.out.print("Valor do Saque: R$");
            contaAcessada.sacar(sc.nextDouble());
        } else if (opcao == 3) {
            System.out.print("Número da Conta Destino: ");
            int destino = sc.nextInt();
            System.out.print("Valor da Transferência: ");
            double valor = sc.nextDouble();
            banco.transferir(contaAcessada.getNumeroConta(), destino, valor);
        }
    }

    private void simularFinanciamento() {
        System.out.println("\n-- Amortização --");
        System.out.print("Valor Total: ");
        double valorFinanciamento = sc.nextDouble();
        System.out.print("Taxa de Juros Mensal (%): ");
        double taxa = sc.nextDouble();
        System.out.print("Quantidade de Parcelas: ");
        int parcelas = sc.nextInt();
        sc.nextLine();
        System.out.print("Sistema (SAC ou PRICE): ");
        String tipoSis = sc.nextLine();

        double[][] matriz = simulador.gerarCronogramaAmortizacao(valorFinanciamento, taxa, parcelas, tipoSis);
        System.out.println("\n--- Matriz (" + tipoSis.toUpperCase() + ") ---");
        System.out.println("Mês | Amortização | Juros   | Parcela Total | Saldo Devedor");
        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%02d  | R$%-9.2f | R$%-6.2f | R$%-11.2f | R$%.2f\n",
                    (int) matriz[i][0], matriz[i][1], matriz[i][2], matriz[i][3], matriz[i][4]);
        }
    }
}