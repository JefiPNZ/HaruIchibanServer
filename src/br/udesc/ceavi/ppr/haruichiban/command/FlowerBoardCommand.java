package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class FlowerBoardCommand implements Command {

    private Flor flor;
    private IModelBoardTile boardTile;

    public FlowerBoardCommand(Flor flor, IModelBoardTile boardTile) {
        this.flor = flor;
        this.boardTile = boardTile;
    }

    @Override
    public void execute() {
        boardTile.getFolha().colocarPecaNaFolha(flor);
    }

}
