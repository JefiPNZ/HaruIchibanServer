package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.DiagonalBoardTilePatternVisitor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.HorizontalBoardTilePatternVisitor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.QuadradoBoardTilePatternVisitor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.VerticalBoardTilePatternVisitor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo C
 */
public class CheckBoard extends FluxoState {

    private Class proximaFase;
    private boolean vencedor = false;
    private boolean pontuou = false;
    private IModelBoardTile[][] tabuleiro;

    public CheckBoard(Class proximaFase) {
        super();
        this.tabuleiro = boardController.getTabuleiro();
        this.proximaFase = proximaFase;
    }

    public boolean isPosicaoValida(int x, int y) {
        return !(y < 0 || y >= this.tabuleiro.length || x < 0 || x >= tabuleiro[0].length);
    }

    @Override
    public void systemCommand() {
        boardController.removeAnimal();
        topPlayer.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Verificando Pontuacao");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Verificando Pontuacao");
        pontuou = this.validaPontuacao();
        int pontosJogadorSuperior = topPlayer.getPlayerScore();
        int pontosJogadorInferior = bottomPlayer.getPlayerScore();

        if (fluxo.isTurnoExtra()) {
            if (pontosJogadorSuperior > pontosJogadorInferior) {
                topVenceu();
                vencedor = true;

            } else if (pontosJogadorInferior > pontosJogadorSuperior) {
                bootomVenceu();
                vencedor = true;
            }
        }
        if (!vencedor) {
            if (pontosJogadorSuperior == pontosJogadorInferior && pontosJogadorSuperior >= 5) {
                fluxo.turnoExtra();
                bottomPlayer.zerarPontos();
                topPlayer.zerarPontos();
            } else if (pontosJogadorSuperior >= 5) {
                //TOP É O VENCEDOR
                topVenceu();
                vencedor = true;
            } else if (pontosJogadorInferior >= 5) {
                //BOTTOM É O VENCEDOR
                bootomVenceu();
                vencedor = true;
            }
        }
        fluxo.novaRodada();
        if (pontuou) {
            if (!vencedor || fluxo.getRodada() == 8) {
                topPlayer.clearDeck();
                bottomPlayer.clearDeck();
                topPlayer.initDeck();
                bottomPlayer.initDeck();
                boardController.limparTabuleiro();
                fluxo.resertRodadas();
                proximaFase = ChooseFlower.class;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CheckBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        playerCommandFluxoState();
    }

    private void bootomVenceu() {
        game.limparListaJardineiro();
        game.setJardineiro(TitleOfGardener.SUPERME_GRADENER, bottomPlayer);
        game.setJardineiro(TitleOfGardener.SEM_TITULO, topPlayer);
        proximaFase = SupremeGardener.class;
        game.notifyServer("Bottom é O vencedor");
        nextFase();
    }

    private void topVenceu() {
        game.limparListaJardineiro();
        game.setJardineiro(TitleOfGardener.SUPERME_GRADENER, topPlayer);
        game.setJardineiro(TitleOfGardener.SEM_TITULO, bottomPlayer);
        proximaFase = SupremeGardener.class;
        game.notifyServer("Top é O vencedor");
        nextFase();
    }

    /**
     * Valida se um dos jogadores pontuou no tabuleiro e adiciona os respectivos
     * pontos, retornando se algum ou ambos pontuou(aram).
     *
     * @return
     */
    public synchronized boolean validaPontuacao() {
        int pontuacaoPrimeiroPontuador = 0;
        int pontuacaoSegundoPontuador = 0;
        ModelPlayer primeiroPontuador = null;
        ModelPlayer segundoPontuador = null;
        DiagonalBoardTilePatternVisitor diagonalVisitor = new DiagonalBoardTilePatternVisitor();
        HorizontalBoardTilePatternVisitor horizontalVisitor = new HorizontalBoardTilePatternVisitor();
        VerticalBoardTilePatternVisitor verticalVisitor = new VerticalBoardTilePatternVisitor();
        QuadradoBoardTilePatternVisitor quadradoVisitor = new QuadradoBoardTilePatternVisitor();
        limpaPontuados();
        for (int row = 0; row < tabuleiro.length; row++) {
            for (int column = 0; column < tabuleiro[row].length; column++) {
                IModelBoardTile tile = tabuleiro[row][column];
                if (!(tile.hasFolha() && tile.getFolha().hasPeca() && tile.getFolha().getPeca().getTipo() == TipoPeca.FLOR)) {
                    continue;
                }
                int pontuacaoPeca = 0;
                ModelPlayer origem = ((Flor) tile.getFolha().getPeca()).getPlayerOrigem();
                pontuacaoPeca += visitaDiagonal(diagonalVisitor, row, column);
                diagonalVisitor.limpa();
                pontuacaoPeca += visitaHorizontal(horizontalVisitor, row, column);
                horizontalVisitor.limpa();
                pontuacaoPeca += visitaVertical(verticalVisitor, row, column);
                verticalVisitor.limpa();
                pontuacaoPeca += visitaQuadrado(quadradoVisitor, row, column);
                quadradoVisitor.limpa();
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

    private int visitaDiagonal(DiagonalBoardTilePatternVisitor visitor, int row, int column) {
        if (row > tabuleiro.length - 4 || column > tabuleiro.length - 4) {
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if (visitor.getPontuacao() >= 4) {
            if (visitor.getPontuacao() >= 5) {
                return 5;
            }
            return 3;
        }
        return 0;
    }

    private int visitaHorizontal(HorizontalBoardTilePatternVisitor visitor, int row, int column) {
        if (column > tabuleiro[row].length - 4) {
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if (visitor.getPontuacao() >= 4) {
            if (visitor.getPontuacao() >= 5) {
                return 5;
            }
            return 2;
        }
        return 0;
    }

    private int visitaVertical(VerticalBoardTilePatternVisitor visitor, int row, int column) {
        if (row > tabuleiro.length - 4) {
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if (visitor.getPontuacao() >= 4) {
            if (visitor.getPontuacao() >= 5) {
                return 5;
            }
            return 2;
        }
        return 0;
    }

    private int visitaQuadrado(QuadradoBoardTilePatternVisitor visitor, int row, int column) {
        if (row > tabuleiro.length - 2 || column > tabuleiro.length - 2) {
            return 0;
        }
        visitor.visit(tabuleiro[row][column]);
        if (visitor.getPontuacao() >= 4) {
            return 1;
        }
        return 0;
    }

    private void limpaPontuados() {
        for (IModelBoardTile[] tabuleiroLinha : tabuleiro) {
            for (IModelBoardTile tabuleiroColuna : tabuleiroLinha) {
                tabuleiroColuna.limpaPontuado();
            }
        }
    }

    @Override
    public void playerCommandFluxoState() {
        topPlayer.getPackageServerOutput().newGet(ModelGet.MY_HAND, topPlayer.getHand());
        topPlayer.getPackageServerOutput().newGet(ModelGet.MY_PILESIZE, topPlayer.getPileSize());
        topPlayer.getPackageServerOutput().newGet(ModelGet.MY_POINTS, topPlayer.getPlayerScore());
        topPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_HAND, bottomPlayer.getHand());
        topPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_PILESIZE, bottomPlayer.getPileSize());
        topPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_POINTS, bottomPlayer.getPlayerScore());
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());

        bottomPlayer.getPackageServerOutput().newGet(ModelGet.MY_HAND, bottomPlayer.getHand());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.MY_PILESIZE, bottomPlayer.getPileSize());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.MY_POINTS, bottomPlayer.getPlayerScore());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_HAND, topPlayer.getHand());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_PILESIZE, topPlayer.getPileSize());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_POINTS, topPlayer.getPlayerScore());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        nextFase();
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String fase) {
    }

    @Override
    public void nextFase() {
        try {
            fluxo.setFluxoState((FluxoState) proximaFase.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CheckBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getDescricaoLog() {
        return "Validando estado do tabuleiro";
    }

}
