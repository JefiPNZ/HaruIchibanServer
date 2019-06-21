package br.udesc.ceavi.ppr.haruichiban.servidor.control.channel;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import com.google.gson.Gson;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Gustavo C
 */
public class PackageServerConsumer implements Runnable {

    private BlockingQueue<CommunicationPackage> entradas;
    private IBoardController boardController;
    private IFluxoController fluxoController;
    private IPlayerController oponnet;
    private IPlayerController player;
    private Gson gson;

    /**
     * @param entradas
     * @param player
     * @param oponnet
     */
    public PackageServerConsumer(BlockingQueue<CommunicationPackage> entradas, IPlayerController player, IPlayerController oponnet) {
        this.entradas = entradas;
        this.boardController = game().getBoardController();
        this.fluxoController = game().getFluxoController();
        this.oponnet = oponnet;
        this.player = player;
        this.gson = new Gson();
    }

    private static GameServidorController game() {
        return GameServidorController.getInstance();
    }

    @Override
    public void run() {
        try {
            CommunicationPackage cPacket = null;
            //enquanto existe um novo comando, lembrando take() bloqueia
            while ((cPacket = entradas.take()) != null) {
                if (cPacket.isPost()) {
                    submitPost(cPacket.getModelPost(), cPacket.getParametro());
                }
                if (cPacket.isGet()) {
                    submitGet(cPacket.getModelGet(), cPacket.getParametro());
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitPost(ModelPost post, String parametroes) {
        switch (post) {
            case FASE_END:
                fluxoController.playerInformeFluxoStateEnd(player, parametroes);
                break;
            case LOGADO_COM_SUCESSO:
                player.setLogadorComSucesso(true);
                break;
        }
    }

    private void submitGet(ModelGet modelGet, String parametroes) {
        switch (modelGet) {
            case GAME_BOARD:
                player.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
                break;
            case GAME_FASE:
                fluxoController.playerCommandFluxoState();
                break;
            case GAME_ESTACAO:
                player.getPackageServerOutput().newGet(modelGet,
                        game().getFactoryPecas().getClass().getSimpleName().
                                contains("Inverno") ? "Inverno" : "Primavera");
                break;

            case MY_COLOR:
                player.getPackageServerOutput().newGet(modelGet, player.getColor());
                break;
            case MY_POINTS:
                player.getPackageServerOutput().newGet(modelGet, player.getPlayerScore());
                break;
            case MY_PILESIZE:
                player.getPackageServerOutput().newGet(modelGet, player.getPileSize());
                break;
            case MY_HAND:
                player.getPackageServerOutput().newGet(modelGet, player.getHand());
                break;
            case MY_POSITION:
                player.getPackageServerOutput().newGet(modelGet, game().getTopPlayer().equals(player) ? "TOP" : "BOTTOM");
                break;

            case OPONNET_COLOR:
                player.getPackageServerOutput().newGet(modelGet, oponnet.getColor());
                break;
            case OPONNET_HAND:
                player.getPackageServerOutput().newGet(modelGet, oponnet.getHand());
                break;
            case OPONNET_PILESIZE:
                player.getPackageServerOutput().newGet(modelGet, oponnet.getPileSize());
                break;
            case OPONNET_POINTS:
                player.getPackageServerOutput().newGet(modelGet, oponnet.getPlayerScore());
                break;
            case OPONNET_POSITION:
                player.getPackageServerOutput().newGet(modelGet, game().getTopPlayer().equals(oponnet) ? "TOP" : "BOTTOM");
                break;
        }
    }

}
