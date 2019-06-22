package br.udesc.ceavi.ppr.haruichiban.servidor.builder;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.decorator.ModelBoardTileDiagonalDecorator;
import br.udesc.ceavi.ppr.haruichiban.servidor.decorator.ModelBoardTileDiagonalInversoDecorator;
import br.udesc.ceavi.ppr.haruichiban.servidor.decorator.ModelBoardTileHorizontalDecorator;
import br.udesc.ceavi.ppr.haruichiban.servidor.decorator.ModelBoardTileVerticalDecorator;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.SimpleModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import java.awt.Point;

/**
 * Controi um ModelBoardTile vazio
 *
 * @author Gustavo C Santos
 * @since 10/05/2019
 *
 */
public class BoardNormalBuilder extends BoardBuilder {

    private final Point POSICAO_ANIMAL_TOPO = new Point(2, 1);
    private final Point POSICAO_ANIMAL_BASE = new Point(3, 3);
    private final Point POSICAO_FOLHA_PRETA = new Point(3, 1);
    private static final boolean[][] TABULEIRO = {
        {true, false, true, false, true},
        {false, true, true, true, false},
        {true, true, false, true, true},
        {false, true, true, true, false},
        {true, false, true, false, true}
    };

    @Override
    public void reset() {
        this.board = new IModelBoardTile[5][];
        for (int i = 0; i < 5; i++) {
            this.board[i] = new IModelBoardTile[5];
            for (int j = 0; j < 5; j++) {
                this.board[i][j] = new SimpleModelBoardTile();
            }
        }
        for (int row = 4; row >= 0; row--) {
            for (int column = 4; column >= 0; column--) {
                if(row + 1 < this.board.length){
                    this.board[row][column] = new ModelBoardTileVerticalDecorator(this.board[row][column], this.board[row + 1][column]);
                    if(column + 1 < this.board[row].length){
                        this.board[row][column] = new ModelBoardTileDiagonalDecorator(this.board[row][column], this.board[row + 1][column + 1]);
                    }
                }
                if(column + 1 < this.board[row].length){
                    this.board[row][column] = new ModelBoardTileHorizontalDecorator(this.board[row][column], this.board[row][column + 1]);
                }
            }
            for (int column = 1; column < 5; column++) {
                if(row + 1 < this.board.length){
                    this.board[row][column] = new ModelBoardTileDiagonalInversoDecorator(this.board[row][column], this.board[row + 1][column - 1]);
                }
            }
        }
    }

    @Override
    public void constroiPartes() {
        GameServidorController gcInstance = GameServidorController.getInstance();
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board[row].length; column++) {
                if (TABULEIRO[row][column]) {
                    Folha folha = gcInstance.getFactoryPecas().createFolha();
                    this.board[row][column].addFolha(folha);
                    Point pos = new Point(column, row);
                    if (POSICAO_FOLHA_PRETA.equals(pos)) {
                        folha.virarFolha();
                        folha.setSempreEscura(true);
                    }
                    if (POSICAO_ANIMAL_TOPO.equals(pos)) {
                        folha.colocarFilhoteNaFolha(gcInstance.getFactoryPecas().createFilhote(gcInstance.getTopPlayer().getColor()));
                        folha.colocarPecaNaFolha(gcInstance.getFactoryPecas().createAnimal(gcInstance.getTopPlayer().getColor()));
                    } else if (POSICAO_ANIMAL_BASE.equals(pos)) {
                        folha.colocarFilhoteNaFolha(gcInstance.getFactoryPecas().createFilhote(gcInstance.getBottomPlayer().getColor()));
                        folha.colocarPecaNaFolha(gcInstance.getFactoryPecas().createAnimal(gcInstance.getBottomPlayer().getColor()));
                    }
                }
            }
        }
    }

    @Override
    public Point getBlack() {
        return POSICAO_FOLHA_PRETA;
    }

}
