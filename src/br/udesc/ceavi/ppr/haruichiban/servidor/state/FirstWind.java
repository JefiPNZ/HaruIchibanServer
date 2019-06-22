package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.command.FirstWindCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.MacroCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo C
 */
public class FirstWind extends FluxoState {

    private IPlayerController jardineiroJunior;
    private IPlayerController jardineiroSenior;
    private Point fim;

    public FirstWind() {
        super();
    }

    @Override
    public void systemCommand() {
        jardineiroJunior = game.getJardineiroJunior(TitleOfGardener.JUNIOR_TITULO);
        jardineiroSenior = game.getJardineiroJunior(TitleOfGardener.SENIOR_TITULO);
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {
        jardineiroJunior.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Primeiro Vento da Primavera");
        jardineiroSenior.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Primeiro Vento da Primavera");
        //Senior
        jardineiroSenior.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Aguarde o Jardineiro Junior Mover o Tabuleiro");
        //junior
        jardineiroJunior.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Primeiro Vento da Primaveria Chegou");
        jardineiroJunior.getPackageServerOutput().newGet(ModelGet.GAME_FASE, FIST_WIND);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String parametros) {
        List<String> parametro = gson.fromJson(parametros, ArrayList.class);

        Point folhaAMover = gson.fromJson(parametro.get(0), Point.class);
        Diretion diretion = gson.fromJson(parametro.get(1), Diretion.class);

        calcularPontoFinal(folhaAMover, diretion);
        MacroCommand macroCommand = new MacroCommand();

        calcularMovimento(folhaAMover, fim, diretion, boardController.getBoardTile(folhaAMover).removeFolha(), macroCommand);
        game.executeCommand(macroCommand);

        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            Logger.getLogger(FirstWind.class.getName()).log(Level.SEVERE, null, ex);
        }
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "O Primeiro Vento da Primavera Chegou Ao Fim");
        topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "O Primeiro Vento da Primavera Chegou Ao Fim");
        nextFase();
    }

    @Override
    public void nextFase() {
        fluxo.setFluxoState(new CheckBoard(NewDarkLeaf.class));
    }

    @Override
    public String getDescricaoLog() {
        return "Primeiro vento da primavera";
    }

    /**
     * Calcula o Ponto Final do Movimento
     * @param pontoIncial Ponto de Partida (Index 0)
     * @param direcao Direcao do Movimento (Index 1)
     * @return
     */
    private boolean calcularPontoFinal(Point pontoIncial, Diretion direcao) {
        switch (direcao) {
            case NORTE:
                if (0 <= pontoIncial.y) {
                    if (boardController.getBoardTile(pontoIncial).hasFolha()) {
                        return calcularPontoFinal(new Point(pontoIncial.x, pontoIncial.y - 1), direcao);
                    } else {
                        fim = pontoIncial;
                        return true;
                    }
                } else {
                    return false;
                }
            case SUL:
                if (boardController.getAlturaTabuleiro() > pontoIncial.y) {
                    if (boardController.getBoardTile(pontoIncial).hasFolha()) {
                        return calcularPontoFinal(new Point(pontoIncial.x, pontoIncial.y + 1), direcao);
                    } else {
                        fim = pontoIncial;
                        return true;
                    }
                } else {
                    return false;
                }
            case LEST:
                if (boardController.getLarguraTabuleiro() > pontoIncial.x) {
                    if (boardController.getBoardTile(pontoIncial).hasFolha()) {
                        return calcularPontoFinal(new Point(pontoIncial.x + 1, pontoIncial.y), direcao);
                    } else {
                        fim = pontoIncial;
                        return true;
                    }
                } else {
                    return false;
                }
            case OESTE:
                if (0 <= pontoIncial.x) {
                    if (boardController.getBoardTile(pontoIncial).hasFolha()) {
                        return calcularPontoFinal(new Point(pontoIncial.x - 1, pontoIncial.y), direcao);
                    } else {
                        fim = pontoIncial;
                        return true;
                    }
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    private void calcularMovimento(Point atual, Point fim,
            Diretion p,
            Folha peca, MacroCommand macroCommand) {
        if (!atual.equals(this.fim)) {
            Point pProxima = null;
            switch (p) {
                case NORTE:
                    pProxima = new Point(atual.x, atual.y - 1);
                    break;
                case SUL:
                    pProxima = new Point(atual.x, atual.y + 1);
                    break;
                case LEST:
                    pProxima = new Point(atual.x + 1, atual.y);
                    break;
                case OESTE:
                    pProxima = new Point(atual.x - 1, atual.y);
                    break;
                default:
                    break;
            }
            macroCommand.addCommand(new FirstWindCommand(boardController.getBoardTile(pProxima), peca));
            Folha folhaDaFrente = boardController.getBoardTile(pProxima).removeFolha();

            calcularMovimento(pProxima, fim, p, folhaDaFrente, macroCommand);
        }
    }
}
