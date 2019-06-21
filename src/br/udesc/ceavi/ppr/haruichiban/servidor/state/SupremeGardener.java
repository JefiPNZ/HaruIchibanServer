package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo C
 */
public class SupremeGardener extends FluxoState {

    private IPlayerController supremeGardener;
    private IPlayerController perdedorGardener;
    private static final String RECOMECAR = "RECOMECAR";

    @Override

    public void sistemCommand() {
        perdedorGardener = game.getJardineiroJunior(TitleOfGardener.SEM_TITULO);
        supremeGardener = game.getJardineiroJunior(TitleOfGardener.SUPERME_GRADENER);
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {
        String parametro = gson.toJson(supremeGardener.getColor());
        parametro += "/" + supremeGardener.getPlayerScore() + "/" + perdedorGardener.getPlayerScore();
        
        topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_WINEW, parametro);
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_WINEW, parametro);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String communicationPcackageParamentro) {
        List<String> fromJson = gson.fromJson(communicationPcackageParamentro, ArrayList.class);
        if (fromJson.get(0).equals("SIM")) {
            player.setPlayerStage(RECOMECAR);
        } else {
            player.setPlayerStage(END);
            topPlayer.getPackageServerOutput().newPost(ModelPost.CLOSE_GAME, "");
            bottomPlayer.getPackageServerOutput().newPost(ModelPost.CLOSE_GAME, "");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SupremeGardener.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        }

        if (recomecar()) {
            resetarJoga();
            nextFase();
        }

    }

    @Override
    public void nextFase() {
        fluxo.setFluxoState(new ChooseFlower());
    }

    public boolean recomecar() {
        return !topPlayer.getPlayerStage().equals(END) && topPlayer.getPlayerStage().equals(RECOMECAR)
                && !bottomPlayer.getPlayerStage().equals(END) && bottomPlayer.getPlayerStage().equals(RECOMECAR);
    }

    public void resetarJoga() {
        topPlayer.clearDeck();
        bottomPlayer.clearDeck();
        topPlayer.initDeck();
        bottomPlayer.initDeck();
        boardController.initTabuleiro();
        fluxo.resertRodadas();

        //Notificar Os Jogadores

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
    }
}
