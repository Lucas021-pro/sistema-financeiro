package br.lucas.financeiro.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import br.lucas.financeiro.service.SimuladorFinanceiro;
import br.lucas.financeiro.util.Navegador;

public class SimuladorController {

    @FXML
    private TextField txtValor, txtTaxa, txtParcelas;
    @FXML
    private ComboBox<String> cbSistema;
    @FXML
    private TableView<LinhaAmortizacao> tableSimulacao;
    @FXML
    private TableColumn<LinhaAmortizacao, Integer> colMes;
    @FXML
    private TableColumn<LinhaAmortizacao, Double> colAmortizacao, colJuros, colParcela, colSaldo;

    private SimuladorFinanceiro service = new SimuladorFinanceiro();

    @FXML
    public void initialize() {
        cbSistema.setItems(FXCollections.observableArrayList("SAC", "PRICE"));
        colMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        colAmortizacao.setCellValueFactory(new PropertyValueFactory<>("amortizacao"));
        colJuros.setCellValueFactory(new PropertyValueFactory<>("juros"));
        colParcela.setCellValueFactory(new PropertyValueFactory<>("parcela"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));

        formatarColunaMoeda(colAmortizacao);
        formatarColunaMoeda(colJuros);
        formatarColunaMoeda(colParcela);
        formatarColunaMoeda(colSaldo);
    }

    @FXML
    private void handleSimular() {
        try {
            double valor = Double.parseDouble(txtValor.getText());
            double taxa = Double.parseDouble(txtTaxa.getText());
            int parcelas = Integer.parseInt(txtParcelas.getText());
            String sistema = cbSistema.getValue();

            double[][] matriz = service.gerarCronogramaAmortizacao(valor, taxa, parcelas, sistema);

            ObservableList<LinhaAmortizacao> lista = FXCollections.observableArrayList();
            for (double[] linha : matriz) {
                lista.add(new LinhaAmortizacao((int) linha[0], linha[1], linha[2], linha[3], linha[4]));
            }
            tableSimulacao.setItems(lista);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Entrada");
            alert.setContentText("Por favor, digite apenas números válidos usando ponto para decimais.");
            alert.showAndWait();
        }
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
    private void irParaOperacoes(ActionEvent event) {
        Navegador.trocarTela(event, "operacoes-view.fxml", "Operações Bancárias");
    }

    public static class LinhaAmortizacao {
        private final SimpleIntegerProperty mes;
        private final SimpleDoubleProperty amortizacao, juros, parcela, saldo;

        public LinhaAmortizacao(int mes, double amort, double juros, double parc, double saldo) {
            this.mes = new SimpleIntegerProperty(mes);
            this.amortizacao = new SimpleDoubleProperty(amort);
            this.juros = new SimpleDoubleProperty(juros);
            this.parcela = new SimpleDoubleProperty(parc);
            this.saldo = new SimpleDoubleProperty(saldo);
        }

        public int getMes() {
            return mes.get();
        }

        public double getAmortizacao() {
            return amortizacao.get();
        }

        public double getJuros() {
            return juros.get();
        }

        public double getParcela() {
            return parcela.get();
        }

        public double getSaldo() {
            return saldo.get();
        }
    }

    private void formatarColunaMoeda(TableColumn<LinhaAmortizacao, Double> coluna) {
        coluna.setCellFactory(tc -> new TableCell<LinhaAmortizacao, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", valor));
                }
            }
        });
    }
}