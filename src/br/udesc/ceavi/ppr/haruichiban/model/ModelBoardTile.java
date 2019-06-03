package br.udesc.ceavi.ppr.haruichiban.model;

import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;

/**
 * Representa uma célula no tabuleiro.
 *
 * @author Jeferson Penz
 */
public class ModelBoardTile {

    private Folha folha;

    /**
     * Cria uma nova peça para o tabuleiro.
     */
    public ModelBoardTile() {
        this.folha = null;
    }

    /**
     * Adiciona uma folha para a célula.
     *
     * @param folha
     */
    public void addFolha(Folha folha) {
        this.folha = folha;
    }

    /**
     * Remove a folha da peça.
     */
    public Folha removeFolha() {
        Folha retono = folha;
        this.folha = null;
        return retono;
    }

    /**
     * Retorna se tem ou não uma folha
     *
     * @return true tem, false não tem
     */
    public boolean hasFolha() {
        return this.folha != null;
    }

    public Folha getFolha() {
        return this.folha;
    }

}
