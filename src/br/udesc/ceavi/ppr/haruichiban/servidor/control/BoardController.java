package br.udesc.ceavi.ppr.haruichiban.servidor.control;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.boardmovement.BoardMovement;
import br.udesc.ceavi.ppr.haruichiban.servidor.builder.BoardBuilder;
import br.udesc.ceavi.ppr.haruichiban.servidor.builder.BuilderDirector;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TipoPeca;
import java.awt.Point;

/**
 *
 * @author Jeferson Penz
 */
public class BoardController implements IBoardController {

    private IModelBoardTile[][] tabuleiro;
    private Point folhaEscura;
    private BoardMovement boardMovement;

    public BoardController() {
        initTabuleiro();
    }

    public void initTabuleiro() {
        BoardBuilder builder = GameServidorController.getInstance().getBuilder();
        BuilderDirector director = new BuilderDirector(builder);
        director.contruir();
        this.tabuleiro = builder.getProduto();
        this.folhaEscura = builder.getBlack();
    }

    @Override
    public int getAlturaTabuleiro() {
        return this.tabuleiro[0].length;
    }

    @Override
    public int getLarguraTabuleiro() {
        return this.tabuleiro.length;
    }

    @Override
    public IModelBoardTile getBoardTile(Point point) {
        return this.tabuleiro[point.y][point.x];
    }

    @Override
    public Point getFolhaEscura() {
        return folhaEscura;
    }

    @Override
    public void setFolhaEscura(Point folhaEscura) {
        this.folhaEscura = folhaEscura;
    }

    @Override
    public IModelBoardTile[][] getTabuleiro() {
        return this.tabuleiro;
    }

    @Override
    public boolean removeAnimal() {
        int numeroDeFores = 0;
        int numeroDeFolha = 0;
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                if (tabuleiro[row][column].hasFolha()) {
                    numeroDeFolha++;
                    if (tabuleiro[row][column].getFolha().hasPeca()
                            && !tabuleiro[row][column].getFolha().hasAnimal()) {
                        numeroDeFores++;
                    }
                }
            }
        }
        if ((numeroDeFolha - numeroDeFores) <= 2) { // Remover os animais
            for (int row = 0; row < tabuleiro.length; row++) {
                for (int column = 0; column < tabuleiro[row].length; column++) {
                    if (tabuleiro[row][column].hasFolha()
                            && tabuleiro[row][column].getFolha().hasPeca()
                            && tabuleiro[row][column].getFolha().getPeca().getTipo() == TipoPeca.ANIMAL) {
                        tabuleiro[row][column].getFolha().removerPecaDeFlor();
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public String[][] getTabuleiroProxy() {
        String[][] tabuleiroProxy = new String[getAlturaTabuleiro()][getLarguraTabuleiro()];
        for (int row = 0; row < tabuleiroProxy.length; row++) {
            for (int column = 0; column < tabuleiroProxy[row].length; column++) {
                tabuleiroProxy[row][column] = tabuleiro[row][column].toJson().toString();
            }
        }
        return tabuleiroProxy;
    }

    @Override
    public void limparTabuleiro() {
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                IModelBoardTile tile = tabuleiro[row][column];
                if (tile.hasFolha()) {
                    if (tile.getFolha().hasPeca()) {
                        tile.getFolha().removerPecaDeFlor();
                    }
                    if (tile.getFolha().isEscura()) {
                        tile.getFolha().mostrarParteClara();
                    }
                }
            }
        }
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                IModelBoardTile tile = tabuleiro[row][column];
                if (tile.hasFolha() && tile.getFolha().isEscura()) {
                    setFolhaEscura(new Point(row, column));
                }
                if (tile.hasFolha() && tile.getFolha().hasFilhote()) {
                    tile.getFolha().colocarPecaNaFolha(GameServidorController.getInstance().getFactoryPecas().createAnimal(tile.getFolha().getFilhote().getCor()));
                }
            }
        }
    }

    @Override
    public Point[] getAnimalsPoint() {
        Point[] animais = new Point[2];
        int i = 0;
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                IModelBoardTile tile = tabuleiro[row][column];
                if (tile.hasFolha() && tile.getFolha().hasAnimal()) {
                    animais[i] = new Point(column, row);
                    i++;
                }
            }
        }
        return animais;
    }

}
