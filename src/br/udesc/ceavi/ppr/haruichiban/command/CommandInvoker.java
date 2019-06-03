package br.udesc.ceavi.ppr.haruichiban.command;

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

    public void addCommand(Command a) {
        this.listaCommandNotExeuted.add(a);
    }

    public void executeCommand(Command a) {
        a.execute();
        this.listaCommandExecutados.add(a);
    }

    public void executeAllCommandNotExeuted() {
        for (Command command : listaCommandNotExeuted) {
            command.execute();
        }
        this.listaCommandExecutados.addAll(listaCommandNotExeuted);
        listaCommandNotExeuted.clear();
    }

    public void guardarCommand(Command guardar) {
        this.commandGuardado = guardar;
    }

    public void executeCommandGuardado() {
        commandGuardado.execute();
        listaCommandExecutados.add(commandGuardado);
        commandGuardado = null;
    }

}
