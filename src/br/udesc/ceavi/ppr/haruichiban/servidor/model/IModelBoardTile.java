package br.udesc.ceavi.ppr.haruichiban.servidor.model;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.BoardTileScoreVisitor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.TipoPontuacao;
import com.google.gson.JsonObject;

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
    
    public IModelBoardTile getProximoTileDiagonalInverso();
    
    public void setPontuado(TipoPontuacao pontuado);
    
    public void limpaPontuado();
    
    public boolean isPontuado(TipoPontuacao direcao);
    
    public JsonObject toJson();
    
}
