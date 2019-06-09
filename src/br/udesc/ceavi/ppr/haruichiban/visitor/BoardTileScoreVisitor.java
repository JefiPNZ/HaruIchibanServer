package br.udesc.ceavi.ppr.haruichiban.visitor;

import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;

/**
 *
 * @author Jeferson Penz
 */
public interface BoardTileScoreVisitor {
    
    public void visit(IModelBoardTile tile);
    
}
