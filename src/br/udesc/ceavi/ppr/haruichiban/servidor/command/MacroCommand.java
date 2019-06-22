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
public class MacroCommand implements Command {
    
    private List<Command> lista = new ArrayList<>();
    
    public void addCommand(Command a) {
        this.lista.add(a);
    }
    
    @Override
    public void execute() {
        lista.forEach((command) -> {
            GameServidorController.getInstance().notifyServer(command);
            command.execute();
        });
    }

    @Override
    public String getDescricaoLog() {
        return "Múltiplos comandos...";
    }

    @Override
    public String getTipoLog() {
        return "Comando";
    }
    
}
