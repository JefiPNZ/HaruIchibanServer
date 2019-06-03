package br.udesc.ceavi.ppr.haruichiban.builder;

import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;
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
    protected ModelBoardTile[][] board;

    public ModelBoardTile[][] getProduto() {
        return board;
    }

    public abstract Point getBlack();

    public abstract void reset();

    public abstract void constroiPartes();

}
