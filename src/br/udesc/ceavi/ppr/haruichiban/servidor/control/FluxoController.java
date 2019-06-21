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
            System.out.println();
            /*VOCE AI! SIM VC QUE ESTA LENDO ISSO!
            SABE POR QUE EU ESTOU AQUI! BOM E MUITO SIMPLES, ME REMOVA E O FLUXO DO JOGO NAO EXECUTA!
            VC ESTA ESTRANHANDO AGORA.
             -QUE ESCREVEU ISSO? - VC PENSANDO!
            BOM EU TOU A 5 LINHA A CIMA SIM O System.out.println();
            UM TANTO QUANTO BIZZARO EU SEI.
            -O QUE O CARA QUE ESCREVEU ESSE COMANDO FUMOU PROVAVELMENTE? - É O QUE VC ESTA PENSANDO AGORA NAO É.
            MAS!
            NAO É BRINCADEIRA VA EM FRENTE TENTE APAGUE ME COMENTE, E VERAR QUE TUDO PARA! EM BAIXO AS PALAVRA DO AUTOR DA LINHA 44 DESSA CLASSE
            
            system.out.printiln,
            TODO DIA TE USO PARA FAZER DEBUGAR UMA PROJETO,
            EM TI ESCREVRO PALAVORES E COISA TOSCAS,
            NOME DE METODOS,HORA, CONDIÇOES,SAIDA DE FUNCOES QUE JUGO ESTAREM ERRADA!
            println QUANTAS VEZES TE TRAI COM printf,
            ELE É MAIS FACIL DE CONCATENAR String MAS ISSO NÃO JUSTIFICA A TRAICAO
            OBRIGADO POR GARANTIR QUE ESSE PROJETO FUNCIONE
            
            Gustavo C. Santos.
             */
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
        this.fluxoState = state;
        fluxoState.sistemCommand();
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
