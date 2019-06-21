package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.boardmovement.JuniorFirstWind;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
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

    public FirstWind() {
        super();
    }

    @Override
    public void sistemCommand() {
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
        //Tratamento de movimentacao
        // index 0 -> Informa a posicao escolhida(Point.class)
        // index 1 -> Informa a direcao escolhida(Diretion.class)

        Point folhaAMover = gson.fromJson(parametro.get(0), Point.class);
        Diretion diretion = gson.fromJson(parametro.get(1), Diretion.class);
        JuniorFirstWind juniorFirstWind = new JuniorFirstWind();
        juniorFirstWind.addPoint(folhaAMover);
        juniorFirstWind.addDiretion(diretion);

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
        fluxo.setFluxoState(new CheckBoard(NewDrackLeaf.class));
    }

}
