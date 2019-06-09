package br.udesc.ceavi.ppr.haruichiban.control;

import br.udesc.ceavi.ppr.haruichiban.boardmovement.BoardMovement;
import br.udesc.ceavi.ppr.haruichiban.control.observers.BoardObserverProxy;
import br.udesc.ceavi.ppr.haruichiban.builder.BoardBuilder;
import br.udesc.ceavi.ppr.haruichiban.builder.BuilderDirector;
import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.utils.Direction;
import br.udesc.ceavi.ppr.haruichiban.visitor.DiagonalBoardTilePatternVisitor;
import br.udesc.ceavi.ppr.haruichiban.visitor.HorizontalBoardTilePatternVisitor;
import br.udesc.ceavi.ppr.haruichiban.visitor.QuadradoBoardTilePatternVisitor;
import br.udesc.ceavi.ppr.haruichiban.visitor.VerticalBoardTilePatternVisitor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeferson Penz
 */
public class BoardController implements IBoardController {

    private List<BoardObserverProxy> observers;
    private IModelBoardTile[][] tabuleiro;
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
    public void botaoClick(Direction diretion) {
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
        DiagonalBoardTilePatternVisitor   diagonalVisitor = new DiagonalBoardTilePatternVisitor();
        HorizontalBoardTilePatternVisitor horizontalVisitor = new HorizontalBoardTilePatternVisitor();
        VerticalBoardTilePatternVisitor   verticalVisitor = new VerticalBoardTilePatternVisitor();
        QuadradoBoardTilePatternVisitor   quadradoVisitor = new QuadradoBoardTilePatternVisitor();
        limpaPontuados();
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                int pontuacaoPeca = 0;
                ModelPlayer origem = ((Flor) tabuleiro[row][column].getFolha().getPeca()).getPlayerOrigem();
                pontuacaoPeca += visitaDiagonal(diagonalVisitor, row, column);
                pontuacaoPeca += visitaHorizontal(horizontalVisitor, row, column);
                pontuacaoPeca += visitaVertical(verticalVisitor, row, column);
                pontuacaoPeca += visitaQuadrado(quadradoVisitor, row, column);
                if (pontuacaoPeca > 0) {
                    if (primeiroPontuador == null) {
                        primeiroPontuador = origem;
                    }
                    if (primeiroPontuador.equals(origem)) {
                        pontuacaoPrimeiroPontuador += pontuacaoPeca;
                    } else {
                        if (segundoPontuador == null) {
                            segundoPontuador = origem;
                        }
                        pontuacaoSegundoPontuador += pontuacaoPeca;
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
    
    private int visitaDiagonal(DiagonalBoardTilePatternVisitor visitor, int row, int column){
        if(row > tabuleiro.length - 4 || column > tabuleiro.length - 4){
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if(visitor.getPontuacao() >= 4){
            if(visitor.getPontuacao() >= 5){
                return 5;
            }
            return 3;
        }
        return 0;
    }
    
    private int visitaHorizontal(HorizontalBoardTilePatternVisitor visitor, int row, int column){
        if(column > tabuleiro[row].length - 4){
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if(visitor.getPontuacao() >= 4){
            if(visitor.getPontuacao() >= 5){
                return 5;
            }
            return 2;
        }
        return 0;
    }
    
    private int visitaVertical(VerticalBoardTilePatternVisitor visitor, int row, int column){
        if(row > tabuleiro.length - 4){
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if(visitor.getPontuacao() >= 4){
            if(visitor.getPontuacao() >= 5){
                return 5;
            }
            return 2;
        }
        return 0;
    }
    
    private int visitaQuadrado(QuadradoBoardTilePatternVisitor visitor, int row, int column){
        if(row > tabuleiro.length - 2 || column > tabuleiro.length - 2){
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if(visitor.getPontuacao() >= 4){
            return 1;
        }
        return 0;
    }
    
    private void limpaPontuados (){
        for (IModelBoardTile[] tabuleiroLinha : tabuleiro) {
            for (IModelBoardTile tabuleiroColuna : tabuleiroLinha) {
                tabuleiroColuna.limpaPontuado();
            }
        }
    }

    @Override
    public boolean isPosicaoValida(int x, int y) {
        return !(y < 0 || y >= tabuleiro.length || x < 0 || x >= tabuleiro[0].length);
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
    public IModelBoardTile[][] getTabuleiro() {
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
