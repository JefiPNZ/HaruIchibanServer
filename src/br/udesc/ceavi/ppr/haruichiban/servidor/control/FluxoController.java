package br.udesc.ceavi.ppr.haruichiban.servidor.control;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.servidor.state.ChooseFlower;
import br.udesc.ceavi.ppr.haruichiban.servidor.state.FluxoState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo C Santos
 * @since 12/05/2019
 *
 */
public class FluxoController implements IFluxoController {

    private GameServidorController controlGame;
    private IPlayerController bottomPlayer;
    private IPlayerController topPlayer;
    private IBoardController controllerBoard;
    private FluxoState fluxoState;
    private boolean turnoExtra;
    private int rodada;
    private boolean gameOver;

    public FluxoController(GameServidorController controlGame) {
        this.controlGame = controlGame;
        this.controllerBoard = controlGame.getBoardController();
        this.bottomPlayer = controlGame.getBottomPlayer();
        this.topPlayer = controlGame.getTopPlayer();
        this.turnoExtra = false;
    }

    /**
     * Escolhe Aleatoriamete o Jogador Para Dar Inicio Ao Turno
     */
    @Override
    public void startGame() {
        while (!topPlayer.isLogadoComSucesso() || !bottomPlayer.isLogadoComSucesso()) {
            try {
                Thread.sleep(100);
            } catch(InterruptedException ex){}
        }
        try {
            Thread.sleep(2500);
            setFluxoState(new ChooseFlower());
            while (!gameOver) {
                if (fluxoState.faseEnd()) {
                    fluxoState.nextFase();
                    Thread.sleep(500);
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FluxoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void setFluxoState(FluxoState state) {
        GameServidorController.getInstance().notifyServer(state);
        this.fluxoState = state;
        fluxoState.systemCommand();
    }

    @Override
    public void playerCommandFluxoState() {
        fluxoState.playerCommandFluxoState();
    }

    @Override
    public FluxoState getFluxoState() {
        return fluxoState;
    }

    @Override
    public void turnoExtra() {
        turnoExtra = true;
        GameServidorController.getInstance().notifyServer("Turno Extra");
    }

    @Override
    public boolean isTurnoExtra() {
        return turnoExtra;
    }

    @Override
    public void novaRodada() {
        this.rodada++;
    }

    @Override
    public int getRodada() {
        return rodada;
    }

    @Override
    public void resertRodadas() {
        this.rodada = 0;
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String parametroes) {
        fluxoState.playerInformeFluxoStateEnd(player, parametroes);
    }

}
