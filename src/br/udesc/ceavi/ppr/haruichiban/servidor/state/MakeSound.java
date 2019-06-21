package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import java.util.Vector;

/**
 *
 * @author Gustavo C
 */
public class MakeSound extends FluxoState {

    private final Vector<TitleOfGardener> titleOfGardeners;

    public MakeSound() {
        super();
        titleOfGardeners = new Vector<>();
    }

    @Override
    public synchronized void systemCommand() {
        titleOfGardeners.add(TitleOfGardener.SCREAM_WINNER);
        titleOfGardeners.add(TitleOfGardener.SCREAM_LOSER);
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_FASE, MAKE_SOUND);
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_FASE, MAKE_SOUND);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String fase) {
        synchronized (titleOfGardeners) {
            player.setTitle(titleOfGardeners.remove(0));
            player.setPlayerStage(END);
            game.setJardineiro(player.getTitulo(), player);
        }
        if (player.getTitulo() == TitleOfGardener.SCREAM_WINNER) {
            player.getPackageServerOutput().newPost(ModelPost.SCREAM_WINNER, "");
        } else {
            player.getPackageServerOutput().newPost(ModelPost.SCREAM_LOSER, "");
        }
        if(faseEnd()){
            nextFase();
        }
    }

    @Override
    public void nextFase() {
        fluxo.setFluxoState(new AutomaticFlowering());
    }

    @Override
    public String getDescricaoLog() {
        return "A\u00e7\u00e3o de Coaxar";
    }

}
