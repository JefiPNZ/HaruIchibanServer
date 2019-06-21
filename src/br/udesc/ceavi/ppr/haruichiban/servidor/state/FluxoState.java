package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.Loggable;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import com.google.gson.Gson;

/**
 *
 * @author Gustavo C
 */
public abstract class FluxoState implements Loggable{

    //Fase
    public static final String CHOOSE_FLOWER = "ChooseFlowerState";
    public static final String MAKE_SOUND = "MakeSoundState";
    public static final String PUT_FLOWER_JUNIOR = "PutFlowerJuniorState";
    public static final String PUT_FLOWER_SENIOR = "PutFlowerSeniorState";
    public static final String NEW_DRACK_LEFT = "NewDrakLeftState";
    public static final String AUTOMATIC_FLOWERING = "AutomaticFloweringtState";
    public static final String FIST_WIND = "FistWindState";
    public static final String SUOERME_GARDENER = "SupremeGardenerState";

    protected Gson gson = new Gson();
    protected final String START = "START";
    protected final String START_BEING_MADE = "STARTBEINGMADE";

    protected final String UPDATEDATA = "UPDATEDATA";
    protected final String UPDATE_DATA_BEING_MADE = "UPDATEDATABEINGMADE";
    protected final String END = "END";
    protected IBoardController boardController;
    protected IPlayerController topPlayer;
    protected IPlayerController bottomPlayer;
    protected GameServidorController game;
    protected IFluxoController fluxo;
    
    public FluxoState() {
        game = GameServidorController.getInstance();
        boardController = game.getBoardController();
        topPlayer = GameServidorController.getInstance().getTopPlayer();
        bottomPlayer = GameServidorController.getInstance().getBottomPlayer();
        topPlayer.setPlayerStage(START);
        bottomPlayer.setPlayerStage(START);
        fluxo = game.getFluxoController();
    }

    public abstract void systemCommand();

    public abstract void playerCommandFluxoState();

    public abstract void playerInformeFluxoStateEnd(IPlayerController player, String communicationPcackageParamentro);

    public abstract void nextFase();

    @Override
    public String getTipoLog() {
        return "Estado";
    }

    /**
     * Verifica se ambos dos player tem Estatos de END
     * <p>
     * END (Estato que indica que todas as operacoes da fase foram feitas
     *
     * @return true se ambos tem o estado END
     */
    public boolean faseEnd() {
        return bottomPlayer.getPlayerStage().endsWith(END)
                && topPlayer.getPlayerStage().endsWith(END);
    }

}
