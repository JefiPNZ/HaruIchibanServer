package br.udesc.ceavi.ppr.haruichiban.servidor.decorator;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.BoardTileScoreVisitor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.TipoPontuacao;
import com.google.gson.JsonObject;

/**
 *
 * @author Jeferson Penz
 */
public class ModelBoardTileDecorator implements IModelBoardTile{
    
    protected IModelBoardTile modelBoardTile;

    public ModelBoardTileDecorator(IModelBoardTile modelBoardTile) {
        this.modelBoardTile = modelBoardTile;
    }

    @Override
    public void addFolha(Folha folha) {
        this.modelBoardTile.addFolha(folha);
    }

    @Override
    public Folha removeFolha() {
        return this.modelBoardTile.removeFolha();
    }

    @Override
    public boolean hasFolha() {
        return this.modelBoardTile.hasFolha();
    }

    @Override
    public Folha getFolha() {
        return this.modelBoardTile.getFolha();
    }

    @Override
    public void accept(BoardTileScoreVisitor visitor) {
        this.modelBoardTile.accept(visitor);
    }

    @Override
    public IModelBoardTile getProximoTileHorizontal() {
        return this.modelBoardTile.getProximoTileHorizontal();
    }

    @Override
    public IModelBoardTile getProximoTileVertical() {
        return this.modelBoardTile.getProximoTileVertical();
    }

    @Override
    public IModelBoardTile getProximoTileDiagonal() {
        return this.modelBoardTile.getProximoTileDiagonal();
    }

    @Override
    public IModelBoardTile getProximoTileDiagonalInverso() {
        return this.modelBoardTile.getProximoTileDiagonalInverso();
    }

    @Override
    public void limpaPontuado() {
        this.modelBoardTile.limpaPontuado();
    }

    @Override
    public void setPontuado(TipoPontuacao direcao) {
        this.modelBoardTile.setPontuado(direcao);
    }

    @Override
    public boolean isPontuado(TipoPontuacao direcao) {
        return this.modelBoardTile.isPontuado(direcao);
    }

    @Override
    public JsonObject toJson() {
        return this.modelBoardTile.toJson();
    }

    @Override
    public String toString() {
        return this.modelBoardTile.toString();
    }
    
}
