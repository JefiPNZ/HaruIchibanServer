package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class FlowerBoardCommand implements Command {

    private Flor flor;
    private IModelBoardTile boardTile;
    private IBoardController boardController;

    public FlowerBoardCommand(Flor flor, IModelBoardTile boardTile) {
        this.flor = flor;
        this.boardTile = boardTile;
        this.boardController = GameServidorController.getInstance().getBoardController();
    }

    @Override
    public void execute() {
        boardTile.getFolha().colocarPecaNaFolha(flor);
    }

    @Override
    public String getDescricaoLog() {
        return "Colocada flor: " + flor + " no tile: " + this.boardTile;
    }

    @Override
    public String getTipoLog() {
        return "Comando";
    }

}
