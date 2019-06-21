package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;

/**
 *
 * @author Gustavo C
 */
public class DefineTitles extends FluxoState {

    private boolean empate;

    public DefineTitles() {
        super();
    }

    @Override
    public void systemCommand() {
        Flor florTopo = topPlayer.getFlower();
        Flor florBottom = bottomPlayer.getFlower();
        topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Verificando Flores");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Verificando Flores");

        if (florBottom.getValor() == florTopo.getValor()) {
            empate = true;

            topPlayer.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Voce e O Seu Oponnet Escolheram Flores do Mesmo Valor");
            topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Flores Com Mesmo Valor");

            bottomPlayer.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Voce e O Seu Oponnet Escolheram Flores do Mesmo Valor");
            bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Flores Com Mesmo Valor");
        } else {
            if (florTopo.getValor() > florBottom.getValor()) {
                //Top Senior
                //Bottonm Junior
                topPlayer.setTitle(TitleOfGardener.SENIOR_TITULO);
                bottomPlayer.setTitle(TitleOfGardener.JUNIOR_TITULO);

                topPlayer.getPackageServerOutput().newPost(ModelPost.BECAME_SENIOR, "");
                bottomPlayer.getPackageServerOutput().newPost(ModelPost.BECAME_JUNIOR, "");

                game.limparListaJardineiro();
                game.setJardineiro(TitleOfGardener.SENIOR_TITULO, game.getTopPlayer());
                game.setJardineiro(TitleOfGardener.JUNIOR_TITULO, game.getBottomPlayer());
            } else {
                //Top Junior
                //Bottonm Senior
                topPlayer.setTitle(TitleOfGardener.JUNIOR_TITULO);
                bottomPlayer.setTitle(TitleOfGardener.SENIOR_TITULO);

                topPlayer.getPackageServerOutput().newPost(ModelPost.BECAME_JUNIOR, "");
                bottomPlayer.getPackageServerOutput().newPost(ModelPost.BECAME_SENIOR, "");

                game.limparListaJardineiro();
                game.setJardineiro(TitleOfGardener.JUNIOR_TITULO, game.getTopPlayer());
                game.setJardineiro(TitleOfGardener.SENIOR_TITULO, game.getBottomPlayer());
            }
            empate = false;
        }
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {

        bottomPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_HAND, topPlayer.getHand());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_PILESIZE, topPlayer.getPileSize());

        topPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_HAND, bottomPlayer.getHand());
        topPlayer.getPackageServerOutput().newGet(ModelGet.OPONNET_PILESIZE, bottomPlayer.getPileSize());

        bottomPlayer.setPlayerStage(END);
        topPlayer.setPlayerStage(END);
        nextFase();
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String fase) {
    }

    @Override
    public void nextFase() {
        game.getFluxoController().setFluxoState(empate ? new MakeSound() : new FlowerBoard());
    }

    @Override
    public boolean faseEnd() {
        return super.faseEnd();
    }

    @Override
    public String getDescricaoLog() {
        return "Defini\u00e7\u00e3o dos T\u00edtulos";
    }

}
