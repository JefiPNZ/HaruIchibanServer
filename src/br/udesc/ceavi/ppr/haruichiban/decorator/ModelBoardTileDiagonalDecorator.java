package br.udesc.ceavi.ppr.haruichiban.decorator;

/**
 *
 * @author Jeferson Penz
 */
public class ModelBoardTileDiagonalDecorator extends ModelBoardTileDecorator{
    
    private IModelBoardTile tileAdjacente;
    
    public ModelBoardTileDiagonalDecorator(IModelBoardTile modelBoardTile, IModelBoardTile tileAdjacente) {
        super(modelBoardTile);
        this.tileAdjacente = tileAdjacente;
    }

    @Override
    public IModelBoardTile getProximoTileDiagonal() {
        IModelBoardTile tileAnterior = super.getProximoTileDiagonal();
        if(tileAnterior != null){
            return tileAnterior;
        }
        return this.tileAdjacente;
    }
    
}
