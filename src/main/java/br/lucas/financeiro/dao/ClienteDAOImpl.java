package br.lucas.financeiro.dao;

import br.lucas.financeiro.model.*;
import br.lucas.financeiro.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void save(Cliente cliente) {
        String sqlCliente = "INSERT INTO clientes (nome, cpf) VALUES (?, ?)";
        String sqlConta = "INSERT INTO contas (cliente_id, tipo_conta, saldo, limite_cheque_especial) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                stmtCliente.setString(1, cliente.getNome());
                stmtCliente.setString(2, cliente.getCpf());

                stmtCliente.executeUpdate();

                ResultSet rs = stmtCliente.getGeneratedKeys();
                int clienteIdGerado = 0;
                if (rs.next()) {
                    clienteIdGerado = rs.getInt(1);
                    cliente.setId(clienteIdGerado);
                }

                for (Conta conta : cliente.getContas()) {
                    try (PreparedStatement stmtConta = conn.prepareStatement(sqlConta, Statement.RETURN_GENERATED_KEYS)) {
                        stmtConta.setInt(1, clienteIdGerado);

                        if (conta instanceof ContaCorrente) {
                            stmtConta.setString(2, "CORRENTE");
                            stmtConta.setDouble(4, 500.0);
                        } else if (conta instanceof ContaPoupanca) {
                            stmtConta.setString(2, "POUPANCA");
                            stmtConta.setNull(4, java.sql.Types.DECIMAL);
                        } else if (conta instanceof ContaInvestimento) {
                            stmtConta.setString(2, "INVESTIMENTO");
                            stmtConta.setNull(4, java.sql.Types.DECIMAL);
                        }
                        stmtConta.setDouble(3, conta.getSaldo());
                        stmtConta.executeUpdate();

                        try (ResultSet rsConta = stmtConta.getGeneratedKeys()) {
                            if (rsConta.next()) {
                                int numeroContaGerado = rsConta.getInt(1);
                                conta.setNumeroConta(numeroContaGerado);
                            }
                        }
                    }
                }
                System.out.println("Cliente e Conta salvos no MySQL");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro SQL: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> listAll() {
        List<Cliente> clientes = new ArrayList<>();

        String sqlCliente = "SELECT * FROM clientes";
        String sqlConta = "SELECT * FROM contas WHERE cliente_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlCliente);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");

                Cliente cliente = new Cliente(nome, cpf, id, new ArrayList<>());

                try (PreparedStatement stmtConta = conn.prepareStatement(sqlConta)) {
                    stmtConta.setInt(1, id);
                    try (ResultSet rsConta = stmtConta.executeQuery()) {
                        while (rsConta.next()) {
                            String tipo = rsConta.getString("tipo_conta");
                            Conta conta;

                            if ("CORRENTE".equals(tipo)) conta = new ContaCorrente(nome);
                            else if ("POUPANCA".equals(tipo)) conta = new ContaPoupanca(nome);
                            else conta = new ContaInvestimento(nome);

                            conta.setNumeroConta(rsConta.getInt("numero_conta"));
                            conta.setSaldo(rsConta.getDouble("saldo"));

                            cliente.adicionarConta(conta);
                        }
                    }
                }
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes e contas: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public Optional<Cliente> findById(Integer id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");

                    Cliente cliente = new Cliente(nome, cpf, rs.getInt("id"), new ArrayList<>());
                    return Optional.of(cliente);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cliente por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Conta> findContaByNumero(int numeroConta) {
        String sql = "SELECT c.*, cl.nome FROM contas c INNER JOIN clientes cl ON c.cliente_id = cl.id WHERE c.numero_conta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroConta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo_conta");
                    String nomeTitular = rs.getString("nome");
                    Conta conta;

                    if ("CORRENTE".equals(tipo)) {
                        conta = new ContaCorrente(nomeTitular);
                        ((ContaCorrente) conta).setLimiteChequeEspecial(rs.getDouble("limite_cheque_especial"));
                    } else if ("POUPANCA".equals(tipo)) {
                        conta = new ContaPoupanca(nomeTitular);
                    } else {
                        conta = new ContaInvestimento(nomeTitular);
                    }

                    conta.setNumeroConta(rs.getInt("numero_conta"));
                    conta.setSaldo(rs.getDouble("saldo"));

                    return Optional.of(conta);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar conta por número: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Cliente cliente) {
        String sql = "UPDATE clientes SET nome = ?, cpf = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setInt(3, cliente.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Dados do cliente atualizado com sucesso no Banco de Dados");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Cliente e suas contas deletados com suceso do Banco de Dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    @Override
    public void atualizarConta(Conta conta) {
        String sql = "UPDATE contas SET saldo = ? WHERE numero_conta = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, conta.getSaldo());
            stmt.setInt(2, conta.getNumeroConta());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo no banco: " + e.getMessage());
        }
    }
}