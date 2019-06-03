package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class NewDrakLeaf implements Command {

    private ModelBoardTile boardTile;

    public NewDrakLeaf(ModelBoardTile boardTile) {
        this.boardTile = boardTile;
    }

    @Override
    public void execute() {
        boardTile.getFolha().virarFolha();
    }

}
