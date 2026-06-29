package br.lucas.financeiro.controller;

import br.lucas.financeiro.dao.ClienteDAO;
import br.lucas.financeiro.dao.ClienteDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import br.lucas.financeiro.service.BancoService;
import br.lucas.financeiro.util.Navegador;

public class HelloController {

    @FXML
    protected void abrirTelaCadastro(ActionEvent event) {
        Navegador.trocarTela(event, "clientes-view.fxml", "Gestão de Clientes");
    }

    @FXML
    protected void abrirTelaOperacoes(ActionEvent event) {
        Navegador.trocarTela(event, "operacoes-view.fxml", "Operações Bancárias");
    }

    @FXML
    protected void abrirTelaSimulador(ActionEvent event) {
        Navegador.trocarTela(event, "simulador-view.fxml", "Simulador de Investimentos");
    }

    @FXML
    protected void processarRendimentos(ActionEvent event) {
        ClienteDAO dao = new ClienteDAOImpl();
        BancoService banco = new BancoService(dao);
        banco.renderContas();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Rendimentos aplicados nas contas rentáveis!");
        alert.showAndWait();
    }
}