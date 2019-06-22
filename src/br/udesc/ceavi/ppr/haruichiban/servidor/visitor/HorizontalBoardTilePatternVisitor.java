package br.udesc.ceavi.ppr.haruichiban.servidor.visitor;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;

/**
 *
 * @author Jeferson Penz
 */
public class HorizontalBoardTilePatternVisitor implements BoardTileScoreVisitor{

    private int pontuacao = 0;
    private ModelPlayer jogador;
    
    @Override
    public void visit(IModelBoardTile tile) {
        if((tile.hasFolha() && tile.getFolha().hasPeca() && tile.getFolha().getPeca().getTipo() == TipoPeca.FLOR) &&
           !tile.isPontuado(TipoPontuacao.HORIZONTAL)){
            if(this.jogador == null){
                this.jogador = ((Flor) tile.getFolha().getPeca()).getPlayerOrigem();
            }
            else if(!this.jogador.equals(((Flor) tile.getFolha().getPeca()).getPlayerOrigem())){
                return;
            }
            tile.setPontuado(TipoPontuacao.HORIZONTAL);
            this.pontuacao++;
            if(tile.getProximoTileHorizontal()!= null){
                tile.getProximoTileHorizontal().accept(this);
            }
        }
    }
    
    public int getPontuacao() {
        return this.pontuacao;
    }
    
    public void limpa(){
        this.pontuacao = 0;
        this.jogador = null;
    }
    
}
