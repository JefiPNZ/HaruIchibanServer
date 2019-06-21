package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.command.ChooseFlowerPlayer;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import java.util.ArrayList;

/**
 *
 * @author Gustavo C
 */
public class ChooseFlower extends FluxoState {

    private static final String CONECTADO = "CONECTADO";

    public ChooseFlower() {
        super();
    }

    @Override
    public void sistemCommand() {
        game.limparListaJardineiro();
        playerCommandFluxoState();
    }

    @Override
    public synchronized void playerCommandFluxoState() {
        topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Nova Rodada");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Nova Rodada");
        topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Escolha de Flores");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Escolha de Flores");

        topPlayer.getPackageServerOutput().newPost(ModelPost.BECAME_NOT_TITLE, "");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.BECAME_NOT_TITLE, "");

        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_FASE, CHOOSE_FLOWER);
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_FASE, CHOOSE_FLOWER);

        bottomPlayer.setPlayerStage(START_BEING_MADE);
        topPlayer.setPlayerStage(START_BEING_MADE);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String parametro) {
        ArrayList<String> carta = gson.fromJson(parametro, ArrayList.class);
        int escolha = Integer.parseInt(carta.get(0));
        GameServidorController.getInstance().executeCommand(
                new ChooseFlowerPlayer(player, escolha));
        //Mando Atualizar
        player.getPackageServerOutput().newGet(ModelGet.MY_HAND, player.getHand());
        player.getPackageServerOutput().newGet(ModelGet.MY_PILESIZE, player.getPileSize());

        player.setPlayerStage(END);
        if (faseEnd()) {
            nextFase();
        }
    }

    @Override
    public void nextFase() {
        fluxo.setFluxoState(new DifineTitles());
    }

}
