package br.udesc.ceavi.ppr.haruichiban.servidor.visitor;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;

/**
 *
 * @author Jeferson Penz
 */
public interface BoardTileScoreVisitor {
    
    public void visit(IModelBoardTile tile);
    
}
