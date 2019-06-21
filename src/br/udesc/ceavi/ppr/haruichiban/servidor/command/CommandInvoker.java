package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GustavoSantos
 * @since 03/05/2019
 *
 */
public class CommandInvoker {

    private List<Command> listaCommandNotExeuted;
    private List<Command> listaCommandExecutados;
    private Command commandGuardado;

    public CommandInvoker() {
        listaCommandNotExeuted = new ArrayList<>();
        listaCommandExecutados = new ArrayList<>();
    }

    public void addCommand(Command comando) {
        this.listaCommandNotExeuted.add(comando);
    }

    public void executeCommand(Command comando) {
        GameServidorController.getInstance().notifyServer(comando);
        comando.execute();
        this.listaCommandExecutados.add(comando);
    }

    public void executeAllCommandNotExeuted() {
        listaCommandNotExeuted.stream().map((command) -> {
            GameServidorController.getInstance().notifyServer(command);
            return command;
        }).forEachOrdered((command) -> {
            command.execute();
        });
        this.listaCommandExecutados.addAll(listaCommandNotExeuted);
        listaCommandNotExeuted.clear();
    }

    public void guardarCommand(Command guardar) {
        this.commandGuardado = guardar;
    }

    public void executeCommandGuardado() {
        commandGuardado.execute();
        GameServidorController.getInstance().notifyServer(commandGuardado);
        listaCommandExecutados.add(commandGuardado);
        commandGuardado = null;
    }

}
