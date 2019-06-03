package br.udesc.ceavi.ppr.haruichiban.control;

import br.udesc.ceavi.ppr.haruichiban.boardmovement.BoardMovement;
import br.udesc.ceavi.ppr.haruichiban.control.observers.BoardObserverProxy;
import br.udesc.ceavi.ppr.haruichiban.builder.BoardBuilder;
import br.udesc.ceavi.ppr.haruichiban.builder.BuilderDirector;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeferson Penz
 */
public class BoardController implements IBoardController {

    private List<BoardObserverProxy> observers;
    private ModelBoardTile[][] tabuleiro;
    private Point folhaEscura;
    private BoardMovement boardMovement;

    public BoardController() {
        this.observers = new ArrayList<>();
        initTabuleiro();
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public void addObserver(BoardObserverProxy observer) {
        this.observers.add(observer);
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public synchronized void renderBoard() {
        observers.forEach(observer -> {
            for (int row = 0; row < tabuleiro.length; row++) {
                for (int column = 0; column < tabuleiro[row].length; column++) {

                    //Apaga
                    observer.clearTile(row, column);

                    //Desenha a Desenha Folha
                    if (tabuleiro[row][column].hasFolha()) {
                        Folha folha = tabuleiro[row][column].getFolha();
                        observer.drawImage(row, column, folha.getCor(), folha.getRotacao(), folha.getClass().getSimpleName());

                        //Verifica se esta tem filhotes 
                        if (folha.hasFilhote()) {
                            observer.drawImage(row, column, folha.getFilhote().getCor(), null, folha.getFilhote().getClass().getSimpleName());
                        }

                        //Verifica se essa tem uma peca
                        if (folha.hasPeca()) {
                            observer.drawImage(row, column, folha.getPeca().getCor(), folha.getPeca().getRotacao(), folha.getPeca().getClass().getSimpleName());
                        }
                    }
                }
            }
            observer.repaintTela();
        });
    }

    private void initTabuleiro() {
        BoardBuilder builder = GameController.getInstance().getBuilder();
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
    public void eventoDeSelecao(Point newSelection) {
        if (boardMovement != null) {
            boardMovement.addPoint(newSelection);
        }
    }

    @Override
    public void botaoClick(Diretion diretion) {
        if (boardMovement != null) {
            boardMovement.addDiretion(diretion);
        }
    }

    /**
     * Valida se um dos jogadores pontuou no tabuleiro e adiciona os respectivos
     * pontos, retornando se algum ou ambos pontuou(aram).
     *
     * @return
     */
    @Override
    public synchronized boolean validaPontuacao() {
        int pontuacaoPrimeiroPontuador = 0;
        int pontuacaoSegundoPontuador = 0;
        ModelPlayer primeiroPontuador = null;
        ModelPlayer segundoPontuador = null;
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                if (tabuleiro[row][column].hasFolha() && tabuleiro[row][column].getFolha().hasPeca()
                        && tabuleiro[row][column].getFolha().getPeca().getTipo() == TipoPeca.FLOR) {
                    int maiorPontuacao = 0;
                    ModelPlayer origem = ((Flor) tabuleiro[row][column].getFolha().getPeca()).getPlayerOrigem();
                    int linear = this.validaFloresLineares(row, column, origem);
                    if (linear > maiorPontuacao) {
                        maiorPontuacao = linear;
                    }
                    int diagonal = this.validaFloresDiagonais(row, column, origem);
                    if (diagonal > maiorPontuacao) {
                        maiorPontuacao = diagonal;
                    }
                    int bloco = this.validaFloresBloco(row, column, origem);
                    if (bloco > maiorPontuacao) {
                        maiorPontuacao = bloco;
                    }
                    if (maiorPontuacao > 0) {
                        if (primeiroPontuador == null) {
                            primeiroPontuador = origem;
                        }
                        if (primeiroPontuador.equals(origem)) {
                            pontuacaoPrimeiroPontuador += maiorPontuacao;
                        } else {
                            if (segundoPontuador == null) {
                                segundoPontuador = origem;
                            }
                            pontuacaoSegundoPontuador += maiorPontuacao;
                        }
                    }
                }
            }
        }
        if (primeiroPontuador != null) {
            primeiroPontuador.addPontos(pontuacaoPrimeiroPontuador);
            if (segundoPontuador != null) {
                segundoPontuador.addPontos(pontuacaoSegundoPontuador);
            }
        }
        return primeiroPontuador != null;
    }

    /**
     * Valida se há uma sequencia de flores em linha (horizontal ou vertical) e
     * retorna a pontuação desta.
     *
     * @param row
     * @param column
     * @param origem
     * @return
     */
    private int validaFloresLineares(int row, int column, ModelPlayer origem) {
        int horizontal = 1 + validaFloresDirecao(row, column, -1, 0, origem) + validaFloresDirecao(row, column, 1, 0, origem);
        int vertical = 1 + validaFloresDirecao(row, column, 0, -1, origem) + validaFloresDirecao(row, column, 0, 1, origem);
        if (horizontal >= 4 || vertical >= 4) {
            int maior = (horizontal > vertical ? horizontal : vertical);
            if (maior >= 5) {
                return 5;
            }
            return 2;
        }
        return 0;
    }

    /**
     * Valida se há uma sequencia de flores em linha (diagonal) e retorna a
     * pontuação desta.
     *
     * @param row
     * @param column
     * @param origem
     * @return
     */
    private int validaFloresDiagonais(int row, int column, ModelPlayer origem) {
        int decendente = 1 + validaFloresDirecao(row, column, -1, 1, origem) + validaFloresDirecao(row, column, 1, -1, origem);
        int ascendente = 1 + validaFloresDirecao(row, column, -1, -1, origem) + validaFloresDirecao(row, column, 1, 1, origem);
        if (decendente >= 4 || ascendente >= 4) {
            int maior = (decendente > ascendente ? decendente : ascendente);
            if (maior >= 5) {
                return 5;
            }
            return 3;
        }
        return 0;
    }

    /**
     * Valida se há uma sequencia de flores em bloco (qualquer direção) e
     * retorna a pontuação desta.
     *
     * @param row
     * @param column
     * @param origem
     * @return
     */
    private int validaFloresBloco(int row, int column, ModelPlayer origem) {
        if (this.validaFloresQuadrado(row, column, origem) || this.validaFloresQuadrado(row - 1, column, origem)
                || this.validaFloresQuadrado(row, column - 1, origem) || this.validaFloresQuadrado(row - 1, column - 1, origem)) {
            return 1;
        }
        return 0;
    }

    private int validaFloresDirecao(int row, int column, int deltaX, int deltaY, ModelPlayer origem) {
        int count = 0;
        ModelBoardTile atual = tabuleiro[row][column];
        while (atual != null && isPosicaoValida(column + deltaX, row + deltaY)) {
            row += deltaY;
            column += deltaX;
            atual = tabuleiro[row][column];
            if (tilePertenceJogador(atual, origem)) {
                count++;
            } else {
                atual = null;
            }
        }
        return count;
    }

    private boolean validaFloresQuadrado(int xEsquerda, int yInferior, ModelPlayer origem) {
        if (isPosicaoValida(yInferior + 1, xEsquerda) && isPosicaoValida(yInferior, xEsquerda + 1) && isPosicaoValida(yInferior + 1, xEsquerda + 1)) {
            if (tilePertenceJogador(tabuleiro[yInferior + 1][xEsquerda], origem) && tilePertenceJogador(tabuleiro[yInferior][xEsquerda + 1], origem)
                    && tilePertenceJogador(tabuleiro[yInferior + 1][xEsquerda + 1], origem)) {
                return true;
            }
        }
        return false;
    }

    private boolean tilePertenceJogador(ModelBoardTile tile, ModelPlayer origem) {
        return tile.hasFolha() && tile.getFolha().hasPeca() && tile.getFolha().getPeca().getTipo() == TipoPeca.FLOR
                && ((Flor) tile.getFolha().getPeca()).getPlayerOrigem().equals(origem);
    }

    @Override
    public boolean isPosicaoValida(int x, int y) {
        return !(y < 0 || y >= tabuleiro.length || x < 0 || x >= tabuleiro[0].length);
    }

    @Override
    public ModelBoardTile getBoardTile(Point point) {
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
    public void initBoardMovement(BoardMovement boardMovement) {
        this.boardMovement = boardMovement;
        observers.forEach(obs -> obs.notifyAtivarTabela());
    }

    @Override
    public void removeBoardMovement() {
        observers.forEach(obs -> obs.notifyDesativarTabela());
        observers.forEach(obs -> obs.notifyDesativarDirection());
        this.boardMovement = null;
    }

    @Override
    public ModelBoardTile[][] getTabuleiro() {
        return this.tabuleiro;
    }

    @Override
    public void removeAnimal() {
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
        if ((numeroDeFolha - numeroDeFores) <= 2) {
            for (int row = 0; row < tabuleiro.length; row++) {
                for (int column = 0; column < tabuleiro[row].length; column++) {
                    if (tabuleiro[row][column].hasFolha()
                            && tabuleiro[row][column].getFolha().hasPeca()
                            && tabuleiro[row][column].getFolha().getPeca().getTipo() == TipoPeca.ANIMAL) {
                        tabuleiro[row][column].getFolha().removerPecaDeFlor();
                    }
                }
            }
        }
    }

    @Override
    public void notifyDirectionAtivar() {
        observers.forEach(obs -> obs.notifyAtivarDirection());
    }
}
