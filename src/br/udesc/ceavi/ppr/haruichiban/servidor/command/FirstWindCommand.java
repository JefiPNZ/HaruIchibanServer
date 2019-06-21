package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;

/**
 *
 * @author Gustavo C Santos
 * @since 15/05/2019
 *
 */
public class FirstWindCommand implements Command {

    private final IModelBoardTile boardTile;
    private final Folha peca;

    public FirstWindCommand(IModelBoardTile boardTile, Folha peca) {
        this.boardTile = boardTile;
        this.peca = peca;
    }

    @Override
    public void execute() {
          boardTile.addFolha(peca);
    }

}