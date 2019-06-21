package br.udesc.ceavi.ppr.haruichiban.servidor.builder;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import java.awt.Point;

/**
 *
 * @author Jeferson Penz
 */
public abstract class BoardBuilder {

    protected Point posicaoAnimalTopo = null;
    protected Point posicaoAnimalBaixo = null;
    protected Point posicaoVitoriaPreta = null;
    protected boolean[][] tabuleiro = null;
    protected IModelBoardTile[][] board;

    public IModelBoardTile[][] getProduto() {
        return board;
    }

    public abstract Point getBlack();

    public abstract void reset();

    public abstract void constroiPartes();

}
