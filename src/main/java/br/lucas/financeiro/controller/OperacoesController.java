package br.lucas.financeiro.controller;

import br.lucas.financeiro.dao.ClienteDAO;
import br.lucas.financeiro.dao.ClienteDAOImpl;
import br.lucas.financeiro.exception.ContaNaoEncontradaException;
import br.lucas.financeiro.exception.SaldoInsuficienteException;
import br.lucas.financeiro.exception.ValorInvalidoException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import br.lucas.financeiro.model.Conta;
import br.lucas.financeiro.model.ContaCorrente;
import br.lucas.financeiro.service.BancoService;
import br.lucas.financeiro.util.Navegador;

public class OperacoesController {

    @FXML
    private TextField txtNumeroConta, txtValor, txtValorTransferencia, txtContaDestino;
    @FXML
    private Label lblTitular, lblSaldo;

    private ClienteDAO dao;
    private BancoService bancoService;
    private Conta contaAtual;

    @FXML
    public void initialize() {
        this.dao = new ClienteDAOImpl();
        this.bancoService = new BancoService(dao);
    }

    @FXML
    private void handleBuscarConta() {
        try {
            int numero = Integer.parseInt(txtNumeroConta.getText());
            contaAtual = bancoService.buscarConta(numero);

            lblTitular.setText("Titular: " + contaAtual.getTitular());
            atualizarLabelSaldo();
        } catch (NumberFormatException e) {
            mostrarErro("Erro de Entrada", "Digite um número de conta válido.");
        } catch (ContaNaoEncontradaException e) {
            mostrarErro("Conta Não Encontrada", e.getMessage());
            contaAtual = null;
            lblTitular.setText("Titular: -");
            lblSaldo.setText("Saldo: R$ 0.00");
        }
    }

    @FXML
    private void handleDepositar() {
        if (validarAcesso()) {
            try {
                double valor = Double.parseDouble(txtValor.getText());

                bancoService.depositar(contaAtual.getNumeroConta(), valor);
                contaAtual = bancoService.buscarConta(contaAtual.getNumeroConta());

                atualizarLabelSaldo();
                txtValor.clear();
                mostrarSucesso("Depósito realizado com sucesso!");

            } catch (ValorInvalidoException e) {
                mostrarErro("Valor Inválido", e.getMessage());
            } catch (Exception e) {
                mostrarErro("Erro", "Verifique o valor digitado.");
            }
        }
    }

    @FXML
    private void handleSacar() {
        if (validarAcesso()) {
            try {
                double valor = Double.parseDouble(txtValor.getText());

                bancoService.sacar(contaAtual.getNumeroConta(), valor);
                contaAtual = bancoService.buscarConta(contaAtual.getNumeroConta());

                atualizarLabelSaldo();
                txtValor.clear();

                if (contaAtual.getSaldo() < 0) {
                    mostrarSucesso("Saque realizado!\n\nAtenção: Você utilizou o seu Cheque Especial. Seu saldo agora é negativo.");
                } else {
                    mostrarSucesso("Saque realizado com sucesso!");
                }

            } catch (SaldoInsuficienteException | ValorInvalidoException e) {
                mostrarErro("Falha no Saque", e.getMessage());
            } catch (Exception e) {
                mostrarErro("Erro", "Verifique o valor digitado.");
            }
        }
    }

    @FXML
    private void handleTransferir() {
        if (validarAcesso()) {
            try {
                int destino = Integer.parseInt(txtContaDestino.getText());
                double valor = Double.parseDouble(txtValorTransferencia.getText());

                bancoService.transferir(contaAtual.getNumeroConta(), destino, valor);

                contaAtual = bancoService.buscarConta(contaAtual.getNumeroConta());

                atualizarLabelSaldo();
                txtValorTransferencia.clear();
                txtContaDestino.clear();

                mostrarSucesso("Transferência concluída!");

            } catch (ContaNaoEncontradaException | SaldoInsuficienteException | ValorInvalidoException e) {
                mostrarErro("Falha na Operação", e.getMessage());
            } catch (Exception e) {
                mostrarErro("Erro", "Verifique os valores digitados.");
            }
        }
    }

    private boolean validarAcesso() {
        if (contaAtual == null) {
            mostrarErro("Operação Inválida", "Busque uma conta antes de realizar operações.");
            return false;
        }
        return true;
    }

    private void atualizarLabelSaldo() {
        String texto = String.format("Saldo: R$ %.2f", contaAtual.getSaldo());
        if (contaAtual instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) contaAtual;
            texto += String.format(" | Limite: R$ %.2f", cc.getLimiteChequeEspecial());
        }

        lblSaldo.setText(texto);
    }

    private void mostrarErro(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarSucesso(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void irParaMenu(ActionEvent event) {
        Navegador.trocarTela(event, "hello-view.fxml", "Menu Principal");
    }

    @FXML
    private void irParaClientes(ActionEvent event) {
        Navegador.trocarTela(event, "clientes-view.fxml", "Gestão de Clientes");
    }

    @FXML
    private void irParaSimulador(ActionEvent event) {
        Navegador.trocarTela(event, "simulador-view.fxml", "Simulador Financeiro");
    }
}