package br.lucas.financeiro.dao;

import br.lucas.financeiro.model.Cliente;
import br.lucas.financeiro.model.Conta;

import java.util.List;
import java.util.Optional;

public interface ClienteDAO {
    void save(Cliente cliente);

    List<Cliente> listAll();

    Optional<Cliente> findById(Integer Id);

    Optional<Conta> findContaByNumero(int numeroConta);

    void update(Cliente cliente);

    void delete(Integer id);

    void atualizarConta(Conta conta);
}