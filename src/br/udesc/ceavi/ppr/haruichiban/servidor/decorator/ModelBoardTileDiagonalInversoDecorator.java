package br.udesc.ceavi.ppr.haruichiban.servidor.decorator;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;

/**
 *
 * @author Jeferson Penz
 */
public class ModelBoardTileDiagonalInversoDecorator extends ModelBoardTileDecorator{
    
    private IModelBoardTile tileAdjacente;
    
    public ModelBoardTileDiagonalInversoDecorator(IModelBoardTile modelBoardTile, IModelBoardTile tileAdjacente) {
        super(modelBoardTile);
        this.tileAdjacente = tileAdjacente;
    }

    @Override
    public IModelBoardTile getProximoTileDiagonalInverso() {
        IModelBoardTile tileAnterior = super.getProximoTileDiagonalInverso();
        if(tileAnterior != null){
            return tileAnterior;
        }
        return this.tileAdjacente;
    }
    
}
