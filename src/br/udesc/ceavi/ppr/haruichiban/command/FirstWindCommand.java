package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.command.Command;
import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;

/**
 *
 * @author Gustavo C Santos
 * @since 15/05/2019
 *
 */
public class FirstWindCommand implements Command {

    private final ModelBoardTile boardTile;
    private final Folha peca;

    public FirstWindCommand(ModelBoardTile boardTile, Folha peca) {
        this.boardTile = boardTile;
        this.peca = peca;
    }

    @Override
    public void execute() {
          boardTile.addFolha(peca);
    }

}
