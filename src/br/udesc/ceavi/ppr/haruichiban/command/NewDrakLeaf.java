package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class NewDrakLeaf implements Command {

    private IModelBoardTile boardTile;

    public NewDrakLeaf(IModelBoardTile boardTile) {
        this.boardTile = boardTile;
    }

    @Override
    public void execute() {
        boardTile.getFolha().virarFolha();
    }

}
