package br.udesc.ceavi.ppr.haruichiban.decorator;

/**
 *
 * @author Jeferson Penz
 */
public class ModelBoardTileHorizontalDecorator extends ModelBoardTileDecorator{
    
    private IModelBoardTile tileAdjacente;
    
    public ModelBoardTileHorizontalDecorator(IModelBoardTile modelBoardTile, IModelBoardTile tileAdjacente) {
        super(modelBoardTile);
        this.tileAdjacente = tileAdjacente;
    }

    @Override
    public IModelBoardTile getProximoTileHorizontal() {
        IModelBoardTile tileAnterior = super.getProximoTileHorizontal();
        if(tileAnterior != null){
            return tileAnterior;
        }
        return this.tileAdjacente;
    }
    
}
