package br.udesc.ceavi.ppr.haruichiban.decorator;

import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.visitor.BoardTileScoreVisitor;
import br.udesc.ceavi.ppr.haruichiban.visitor.TipoPontuacao;

/**
 *
 * @author Jeferson Penz
 */
public interface IModelBoardTile {
    
    public void addFolha(Folha folha);
    
    public Folha removeFolha();
    
    public boolean hasFolha();
    
    public Folha getFolha();
    
    public void accept(BoardTileScoreVisitor visitor);
    
    public IModelBoardTile getProximoTileHorizontal();
    
    public IModelBoardTile getProximoTileVertical();
    
    public IModelBoardTile getProximoTileDiagonal();
    
    public void setPontuado(TipoPontuacao pontuado);
    
    public void limpaPontuado();
    
    public boolean isPontuado(TipoPontuacao direcao);
    
    public String toJson();
    
}
