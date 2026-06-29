module br.lucas.financeiro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens br.lucas.financeiro.controller to javafx.fxml;
    exports br.lucas.financeiro.controller;

    exports br.lucas.financeiro.model;
    exports br.lucas.financeiro.exception;
    exports br.lucas.financeiro.dao;
    exports br.lucas.financeiro.service;

    exports br.lucas.financeiro.util;
}