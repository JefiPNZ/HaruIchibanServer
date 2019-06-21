package br.udesc.ceavi.ppr.haruichiban.servidor.visitor;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;

/**
 *
 * @author Jeferson Penz
 */
public class QuadradoBoardTilePatternVisitor implements BoardTileScoreVisitor{

    private int pontuacao = 0;
    private ModelPlayer jogador;
    
    @Override
    public void visit(IModelBoardTile tile) {
        if((tile.hasFolha() && tile.getFolha().hasPeca() && tile.getFolha().getPeca().getTipo() == TipoPeca.FLOR) &&
           !tile.isPontuado(TipoPontuacao.QUADRADO)){
            this.jogador = ((Flor) tile.getFolha().getPeca()).getPlayerOrigem();
            tile.setPontuado(TipoPontuacao.QUADRADO);
            this.pontuacao++;
            SingularBoardTilePatternVisitor visitor = new SingularBoardTilePatternVisitor();
            visitor.setJogador(this.jogador);
            if(tile.getProximoTileHorizontal() != null && tile.getProximoTileDiagonal() != null &&
               tile.getProximoTileVertical()   != null){
                visitor.visit(tile.getProximoTileHorizontal());
                visitor.visit(tile.getProximoTileDiagonal());
                visitor.visit(tile.getProximoTileVertical());
            }
            this.pontuacao += visitor.getPontuacao();
        }
    }
    
    public int getPontuacao() {
        return this.pontuacao;
    }
    
    public void limpa(){
        this.pontuacao = 0;
    }
    
}
