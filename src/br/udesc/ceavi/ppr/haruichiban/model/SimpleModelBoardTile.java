package br.udesc.ceavi.ppr.haruichiban.model;

import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.visitor.BoardTileScoreVisitor;
import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.visitor.TipoPontuacao;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma célula no tabuleiro.
 *
 * @author Jeferson Penz
 */
public class SimpleModelBoardTile implements IModelBoardTile{

    private Folha folha;
    private List<TipoPontuacao> pontuado;

    /**
     * Cria uma nova peça para o tabuleiro.
     */
    public SimpleModelBoardTile() {
        this.folha = null;
        this.pontuado = new ArrayList<>();
    }

    /**
     * Adiciona uma folha para a célula.
     *
     * @param folha
     */
    @Override
    public void addFolha(Folha folha) {
        this.folha = folha;
    }

    /**
     * Remove a folha da peça.
     * @return 
     */
    @Override
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
    @Override
    public boolean hasFolha() {
        return this.folha != null;
    }

    @Override
    public Folha getFolha() {
        return this.folha;
    }
    
    @Override
    public void accept(BoardTileScoreVisitor visitor){
        visitor.visit(this);
    }

    @Override
    public IModelBoardTile getProximoTileHorizontal() {
        return null;
    }

    @Override
    public IModelBoardTile getProximoTileVertical() {
        return null;
    }

    @Override
    public IModelBoardTile getProximoTileDiagonal() {
        return null;
    }

    @Override
    public void limpaPontuado() {
        this.pontuado = new ArrayList<>();
    }

    @Override
    public void setPontuado(TipoPontuacao direcao) {
        if(!this.isPontuado(direcao)){
            this.pontuado.add(direcao);
        }
    }

    @Override
    public boolean isPontuado(TipoPontuacao direcao) {
        return this.pontuado.contains(direcao);
    }

}
