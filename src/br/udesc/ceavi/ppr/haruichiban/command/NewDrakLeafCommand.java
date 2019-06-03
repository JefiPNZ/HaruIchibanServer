package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.control.IBoardController;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class NewDrakLeafCommand implements Command {

    private IBoardController boardController;
    private Point localLerf;

    public NewDrakLeafCommand(IBoardController boardController, Point localLerf) {
        this.boardController = boardController;
        this.localLerf = localLerf;
    }

    @Override
    public void execute() {
        boardController.getBoardTile(localLerf).getFolha().virarFolha();
        boardController.setFolhaEscura(localLerf);
    }

}
