package br.lucas.financeiro.service;

import br.lucas.financeiro.dao.ClienteDAO;
import br.lucas.financeiro.exception.ContaNaoEncontradaException;
import br.lucas.financeiro.exception.SaldoInsuficienteException;
import br.lucas.financeiro.exception.ValorInvalidoException;
import br.lucas.financeiro.model.Cliente;
import br.lucas.financeiro.model.Conta;
import br.lucas.financeiro.model.Rentavel;

public class BancoService {
    private final ClienteDAO clienteRepository;

    public BancoService(ClienteDAO clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void cadastrarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
        System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso!");
    }

    public Conta buscarConta(int numeroConta) throws ContaNaoEncontradaException {
        return clienteRepository.findContaByNumero(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaException("A conta número " + numeroConta + " não foi localizada."));
    }

    public void sacar(int numeroConta, double valor) throws ContaNaoEncontradaException, SaldoInsuficienteException, ValorInvalidoException {

        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do saque deve ser maior que zero.");
        }

        Conta conta = buscarConta(numeroConta);
        conta.sacar(valor);
        clienteRepository.atualizarConta(conta);
    }

    public void depositar(int numeroConta, double valor) throws ContaNaoEncontradaException, ValorInvalidoException {

        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do depósito deve ser maior que zero.");
        }

        Conta conta = buscarConta(numeroConta);
        conta.depositar(valor);
        clienteRepository.atualizarConta(conta);
    }

    public void transferir(int numeroOrigem, int numeroDestino, double valor) throws ContaNaoEncontradaException, SaldoInsuficienteException {

        if (valor <= 0) {
            throw new ValorInvalidoException("O valor da transferência deve ser maior que zero.");
        }

        Conta origem = buscarConta(numeroOrigem);
        Conta destino = buscarConta(numeroDestino);

        origem.sacar(valor);
        destino.depositar(valor);

        clienteRepository.atualizarConta(origem);
        clienteRepository.atualizarConta(destino);

        System.out.println("Transferência de R$" + valor + " concluída com sucesso!");
    }

    public void renderContas() {
        int contasRendidas = 0;
        for (Cliente cliente : clienteRepository.listAll()) {
            for (Conta conta : cliente.getContas()) {
                if (conta instanceof Rentavel) {
                    ((Rentavel) conta).aplicarRendimento();
                    clienteRepository.atualizarConta(conta);
                    contasRendidas++;
                }
            }
        }


        System.out.println("Virada de mês processada. Rendimentos aplicados em " + contasRendidas + " contas.");
    }
}