package br.udesc.ceavi.ppr.haruichiban.servidor.decorator;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;

/**
 *
 * @author Jeferson Penz
 */
public class ModelBoardTileVerticalDecorator extends ModelBoardTileDecorator{
    
    private IModelBoardTile tileAdjacente;
    
    public ModelBoardTileVerticalDecorator(IModelBoardTile modelBoardTile, IModelBoardTile tileAdjacente) {
        super(modelBoardTile);
        this.tileAdjacente = tileAdjacente;
    }

    @Override
    public IModelBoardTile getProximoTileVertical() {
        IModelBoardTile tileAnterior = super.getProximoTileVertical();
        if(tileAnterior != null){
            return tileAnterior;
        }
        return this.tileAdjacente;
    }
    
}