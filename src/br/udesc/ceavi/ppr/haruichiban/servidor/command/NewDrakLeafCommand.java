package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class NewDrakLeafCommand implements Command {

    private IBoardController boardController;
    private Point newDrakLeaf;

    /**
     * @param newDrakLeaf Localizacao Nova Folha Escura
     */
    public NewDrakLeafCommand(Point newDrakLeaf) {
        this.newDrakLeaf = newDrakLeaf;
        this.boardController = GameServidorController.getInstance().getBoardController();
    }

    @Override
    public void execute() {
        boardController.getBoardTile(newDrakLeaf).getFolha().virarFolha();
        boardController.setFolhaEscura(newDrakLeaf);
    }

}
