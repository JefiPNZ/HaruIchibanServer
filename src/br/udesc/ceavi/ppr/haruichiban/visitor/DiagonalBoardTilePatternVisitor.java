package br.udesc.ceavi.ppr.haruichiban.visitor;

import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;

/**
 *
 * @author Jeferson Penz
 */
public class DiagonalBoardTilePatternVisitor implements BoardTileScoreVisitor{

    private int pontuacao = 0;
    private ModelPlayer jogador;
    
    @Override
    public void visit(IModelBoardTile tile) {
        if((tile.hasFolha() && tile.getFolha().hasPeca() && tile.getFolha().getPeca().getTipo() == TipoPeca.FLOR) &&
           !tile.isPontuado(TipoPontuacao.DIAGONAL)){
            if(this.jogador != null){
                this.jogador = ((Flor) tile.getFolha().getPeca()).getPlayerOrigem();
            }
            else if(!this.jogador.equals(((Flor) tile.getFolha().getPeca()).getPlayerOrigem())){
                return;
            }
            tile.setPontuado(TipoPontuacao.DIAGONAL);
            this.pontuacao++;
            if(tile.getProximoTileDiagonal() != null){
                this.visit(tile.getProximoTileDiagonal());
            }
        }
    }
    
    public int getPontuacao() {
        return this.pontuacao;
    }
    
    public void limpa(){
        this.pontuacao = 0;
    }
    
}
