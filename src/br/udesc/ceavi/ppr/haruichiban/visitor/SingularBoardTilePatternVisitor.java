package br.udesc.ceavi.ppr.haruichiban.visitor;

import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;

/**
 *
 * @author Jeferson Penz
 */
public class SingularBoardTilePatternVisitor implements BoardTileScoreVisitor{

    private int pontuacao = 0;
    private ModelPlayer jogador;
    
    @Override
    public void visit(IModelBoardTile tile) {
        if((tile.hasFolha() && tile.getFolha().hasPeca() && tile.getFolha().getPeca().getTipo() == TipoPeca.FLOR) &&
           !tile.isPontuado(TipoPontuacao.QUADRADO)){
            if(this.jogador == null || !this.jogador.equals(((Flor) tile.getFolha().getPeca()).getPlayerOrigem())){
                return;
            }
            tile.setPontuado(TipoPontuacao.QUADRADO);
            this.pontuacao++;
        }
    }

    public void setJogador(ModelPlayer jogador) {
        this.jogador = jogador;
    }
    
    public int getPontuacao() {
        return this.pontuacao;
    }
    
    public void limpa(){
        this.pontuacao = 0;
    }
    
}
