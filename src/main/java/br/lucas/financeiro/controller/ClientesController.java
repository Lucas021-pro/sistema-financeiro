package br.lucas.financeiro.controller;

import br.lucas.financeiro.dao.ClienteDAO;
import br.lucas.financeiro.dao.ClienteDAOImpl;
import br.lucas.financeiro.model.*;
import br.lucas.financeiro.util.Navegador;
import br.lucas.financeiro.util.Validador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Optional;

public class ClientesController {

    @FXML
    private TextField txtNome, txtCpf;
    @FXML
    private ComboBox<String> cbTipoConta;
    @FXML
    private TableView<Cliente> tableClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colNome, colCpf;

    private Integer idClienteEmEdicao = null;
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private Cliente clienteSelecionado;

    @FXML
    public void initialize() {
        cbTipoConta.setItems(FXCollections.observableArrayList("Corrente", "Poupança", "Investimento"));
        configurarTabela();
        handleAtualizarTabela();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
    }

    @FXML
    private void handleSalvar() {
        try {
            String nomeValidado = Validador.validarNomeCompleto(txtNome.getText());
            String cpfValidado = Validador.validarCpf(txtCpf.getText());

            if (clienteSelecionado == null) {
                Cliente novo = new Cliente(nomeValidado, cpfValidado, null, new ArrayList<>());
                String tipo = cbTipoConta.getValue();
                Conta novaConta;

                if ("Corrente".equals(tipo)) {
                    novaConta = new ContaCorrente(novo.getNome());
                } else if ("Poupança".equals(tipo)) {
                    novaConta = new ContaPoupanca(novo.getNome());
                } else if ("Investimento".equals(tipo)) {
                    novaConta = new ContaInvestimento(novo.getNome());
                } else {
                    mostrarAlerta("Erro", "Selecione um tipo de conta.");
                    return;
                }

                novo.adicionarConta(novaConta);

                clienteDAO.save(novo);

                mostrarAlerta("Sucesso", "Cliente salvo com sucesso no Banco!");
            } else {
                clienteSelecionado.setNome(nomeValidado);
                clienteSelecionado.setCpf(cpfValidado);

                clienteDAO.update(clienteSelecionado);
                mostrarAlerta("Sucesso", "Dados atualizados com sucesso!");
            }

            handleLimpar();
            handleAtualizarTabela();

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Preenchimento", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Falha de Conexão", "Erro ao processar o salvamento: " + e.getMessage());
        }
    }

    @FXML
    private void handleExcluir() {
        Cliente clienteSelecionado = tableClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Atenção: Você está prestes a excluir o cliente " + clienteSelecionado.getNome());
            confirmacao.setContentText("Tem certeza de que deseja continuar? Esta ação apagará a conta e não poderá ser desfeita.");

            Optional<ButtonType> resposta = confirmacao.showAndWait();

            if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
                try {
                    clienteDAO.delete(clienteSelecionado.getId());

                    mostrarSucesso("Cliente excluído com sucesso!");

                    handleLimpar();
                    handleAtualizarTabela();

                } catch (Exception e) {
                    mostrarErro("Erro de Exclusão", "Não foi possível excluir: " + e.getMessage());
                }
            }
        } else {
            mostrarErro("Aviso", "Por favor, selecione um cliente na tabela primeiro.");
        }
    }

    @FXML
    private void handleEditar() {
        this.clienteSelecionado = tableClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            Cliente clienteAtualizado = clienteDAO.findById(clienteSelecionado.getId()).orElse(null);

            if (clienteAtualizado != null) {
                txtNome.setText(clienteAtualizado.getNome());
                txtCpf.setText(clienteAtualizado.getCpf());
                cbTipoConta.setDisable(true);

                this.clienteSelecionado = clienteAtualizado;
            }
        } else {
            mostrarAlerta("Aviso", "Selecione um cliente na tabela primeiro.");
        }
    }

    @FXML
    private void handleAtualizarTabela() {
        try {
            ObservableList<Cliente> obsList = FXCollections.observableArrayList(clienteDAO.listAll());
            tableClientes.setItems(obsList);
        } catch (Exception e) {
            System.out.println("Aviso: Banco offline.");
            tableClientes.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleLimpar() {
        txtNome.clear();
        txtCpf.clear();
        cbTipoConta.setDisable(false);
        this.idClienteEmEdicao = null;
        this.clienteSelecionado = null;
    }

    @FXML
    private void irParaMenu(ActionEvent event) {
        Navegador.trocarTela(event, "hello-view.fxml", "Menu Principal");
    }

    @FXML
    private void irParaOperacoes(ActionEvent event) {
        Navegador.trocarTela(event, "operacoes-view.fxml", "Operações Bancárias");
    }

    @FXML
    private void irParaSimulador(ActionEvent event) {
        Navegador.trocarTela(event, "simulador-view.fxml", "Simulador Financeiro");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(titulo.contains("Erro") || titulo.contains("Falha") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}