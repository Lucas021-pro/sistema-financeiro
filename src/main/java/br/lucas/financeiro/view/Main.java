package br.lucas.financeiro.view;

import br.lucas.financeiro.dao.ClienteDAO;
import br.lucas.financeiro.dao.ClienteDAOImpl;
import br.lucas.financeiro.service.BancoService;
import br.lucas.financeiro.service.SimuladorFinanceiro;

public class Main {
    public static void main(String[] args) {

        ClienteDAO repository = new ClienteDAOImpl();

        BancoService banco = new BancoService(repository);
        SimuladorFinanceiro simulador = new SimuladorFinanceiro();

        TerminalView view = new TerminalView(banco, simulador);
        view.iniciar();
    }
}