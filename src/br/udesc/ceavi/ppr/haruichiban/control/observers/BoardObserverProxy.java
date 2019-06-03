package br.udesc.ceavi.ppr.haruichiban.control.observers;

import java.awt.Color;

/**
 *
 * @author Jeferson Penz
 */
public interface BoardObserverProxy {
    
    /**
     * Limpa uma célula do tabuleiro para permitir desenhos futuros.
     * @param row
     * @param col 
     */
    public void clearTile(int row, int col);
    
    /**
     * Realiza o desenho de uma vitoria régia no tabuleiro.
     * @param row
     * @param col
     * @param lilypadColor
     * @param rotation
     * @param classeName
     */
    public void drawImage(int row, int col, Color lilypadColor, Float rotation, String classeName);

    public void repaintTela();

    public void notifyAtivarTabela();

    public void notifyDesativarTabela();

    public void notifyAtivarDirection();

    public void notifyDesativarDirection();
    
}
