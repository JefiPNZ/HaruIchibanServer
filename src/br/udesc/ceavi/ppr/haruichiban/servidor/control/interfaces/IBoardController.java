package br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import java.awt.Point;

/**
 * Interface para classes que realizam o controle do tabuleiro
 *
 * @author Jeferson Penz
 */
public interface IBoardController {

    public int getAlturaTabuleiro();

    public int getLarguraTabuleiro();

    public Point getFolhaEscura();

    public void setFolhaEscura(Point newFolhaEscura);

    public IModelBoardTile getBoardTile(Point localLerf);

    public IModelBoardTile[][] getTabuleiro();

    public boolean removeAnimal();

    public String[][] getTabuleiroProxy();

    public void limparTabuleiro();

    public Point[] getAnimalsPoint();

    void initTabuleiro();

}
