package br.lucas.financeiro.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Navegador {

    public static void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegador.class.getResource("/br/lucas/financeiro/" + fxml));
            Scene scene = new Scene(loader.load());

            Stage stageAtual = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stageAtual.setTitle(titulo);
            stageAtual.setScene(scene);
            stageAtual.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela " + fxml);
            e.printStackTrace();
        }
    }
}