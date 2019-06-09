package br.udesc.ceavi.ppr.haruichiban.control;

import br.udesc.ceavi.ppr.haruichiban.HarulchibanServidor;
import br.udesc.ceavi.ppr.haruichiban.abstractfactory.*;
import br.udesc.ceavi.ppr.haruichiban.builder.*;
import br.udesc.ceavi.ppr.haruichiban.command.Command;
import br.udesc.ceavi.ppr.haruichiban.command.CommandInvoker;
import br.udesc.ceavi.ppr.haruichiban.control.observers.ServerStatusObserver;
import br.udesc.ceavi.ppr.haruichiban.model.GameConfig;

import java.awt.Color;
import java.util.Random;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Classe Principal para inicializasao e controle do estado da Aplicacao.
 *
 * @author Jeferson e Gustavo
 *
 * AVISO - N�O USAR INSTANCEOF, COMPARAR USANDO GETCLASS() == .CLASS. - N�O PODE HAVER //JOptionPane NO CONTROLLER. -
 * Quando adicionar um componente transparente (como overlay por ex.), lembrar do setOpaque(false).
 */
public class GameController {

    /**
     * Instancia de referencia do Singleton.
     */
    private static GameController instance;

    /**
     * Retorna a instancia existente do Singleton.
     *
     * @return A instancia existente ou uma nova instencia do jogo.
     */
    public synchronized static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    /**
     * Constante para o nome do jogo.
     */
    public static final String GAME_NAME = "Haru Ichiban";

    /**
     * Responsavel por gerar dados aleatorios.
     */
    private Random randomizer;

    /**
     * Semente fixa para geracao estatica de dados aleatarios.
     */
    private long fixedSeed;

    private HarulchibanServidor servidor;

    private BoardBuilder builderTabuleiro;

//	private List<GameStateObserver> gameStateObserver;
    /**
     * Representa o jogador do topo da tela.
     */
    private PlayerController topPlayer;

    /**
     * Representa o jogador da base da tela.
     */
    private PlayerController bottomPlayer;

    private FactoryPecas factoryPecas;

    private BoardController controllerBoard;

    private IFluxoController controlDeFluxo;

    private CommandInvoker commandInvoker;

    private GameConfig gameConfig;

    private List<ServerStatusObserver> statusObservers;

    /**
     * Classe para criação da instância do Singleton.
     */
    private GameController() {
        this.randomizer = new Random();
        this.fixedSeed = this.randomizer.nextLong();
        //		this.gameStateObserver = new ArrayList<>();
        this.statusObservers = new ArrayList<>();
    }

    /**
     * Para a execução da lógica do jogo.
     *
     * @param mensagem
     */
    public void stop(String mensagem) {
//		this.gameStateObserver.forEach((observer) -> {
//			observer.notificaFimJogo(mensagem);
//		});
//		this.gameStateObserver = new ArrayList<>();
    }

    /**
     * Retorna o gerador de dados aleatórios.
     *
     * @return
     */
    public Random getRandomizer() {
        return randomizer;
    }

    /**
     * Retorna o gerador de dados aleatórios fixo. Este gerador apresenta sempre os mesmo valores quando é criado.
     *
     * @return
     */
    public Random getFixedRandomizer() {
        return new Random(this.fixedSeed);
    }

    public PlayerController getBottomPlayer() {
        return bottomPlayer;
    }

    public PlayerController getTopPlayer() {
        return topPlayer;
    }

    public void setTopPlayer(PlayerController topPlayer) {
        this.topPlayer = topPlayer;
    }

    public void setBottomPlayer(PlayerController bottomPlayer) {
        this.bottomPlayer = bottomPlayer;
    }

    public FactoryPecas getFactoryPecas() {
        return factoryPecas;
    }

    public BoardBuilder getBuilder() {
        return builderTabuleiro;
    }

    public IBoardController getBoardController() {
        return controllerBoard;
    }

    public void startGame() {
        this.controllerBoard = new BoardController();
        this.commandInvoker = new CommandInvoker();
        this.controlDeFluxo = new FluxoController(this);
        this.controlDeFluxo.startGame();
    }

//
    public IFluxoController getFluxoController() {
        return controlDeFluxo;
    }
//
//	public void notificaMudancaEstado(String mensagem) {
//		SwingUtilities.invokeLater(() -> {
//			this.gameStateObserver.forEach((observer) -> {
//				observer.notificaMudancaEstado(mensagem);
//			});
//		});
//	}
//
//	public void addGameStateObserver(GameStateObserver obs) {
//		this.gameStateObserver.add(obs);
//	}
//
//	public void removeGameStateObserver(GameStateObserver obs) {
//		this.gameStateObserver.remove(obs);
//	}

    public void addServerStatusObserver(ServerStatusObserver obs) {
        this.statusObservers.add(obs);
    }

    public void removeServerStatusObserver(ServerStatusObserver obs) {
        this.statusObservers.remove(obs);
    }

    public void executeCommand(Command command) {
        this.commandInvoker.executeCommand(command);
    }

    public void beginServet(String estacaoTabuleiro, String tamanhoTabuleiro, Color corJogadorTopo,
            Color corJogadorBase) {
        this.gameConfig = new GameConfig();

        gameConfig.setEstacao(estacaoTabuleiro);
        switch (estacaoTabuleiro) {
            default:
                gameConfig.setEstacao("Primavera");
            case "Primavera":
                this.factoryPecas = new FactoryPecasPrimavera();
                break;
            case "Inverno":
                this.factoryPecas = new FactoryPecasInverno();
                break;
        }

        int tamanhoDeck;
        switch (tamanhoTabuleiro) {
            default:
            case "Giga":
                this.builderTabuleiro = new BoardGigaBuilder();
                tamanhoDeck = 13;
                break;
            case "Normal":
                this.builderTabuleiro = new BoardNormalBuilder();
                tamanhoDeck = 9;
                break;
        }
        gameConfig.setTamanho(tamanhoDeck);
        gameConfig.setBottonColor(corJogadorBase);
        gameConfig.setTopColor(corJogadorTopo);
        this.servidor.startServidor(gameConfig);
    }

    public void setServidor(HarulchibanServidor servidor) {
        this.servidor = servidor;
    }

    public void clientRequest(String request, PlayerController playerRequest) {
        switch (request.split(",")[1]) {
            case "GAMECONFIG":
                this.notifyClientRequest("Game Controller : " + request);
                String resposta = new Gson().toJson(this.gameConfig);
                playerRequest.sendResource(resposta);
                this.notifyClientRequest("Requisicao Respondida");
                break;
            default:
                break;
        }
    }

    public void notifyClientRequest(String message) {
        SwingUtilities.invokeLater(() -> {
            this.statusObservers.forEach((statusObserver) -> {
                statusObserver.onClientRequest(message);
            });
        });
    }

    public void notifyError(String message) {
        SwingUtilities.invokeLater(() -> {
            this.statusObservers.forEach((statusObserver) -> {
                statusObserver.onError(message);
            });
        });
    }

    public void notifyResourceResponse(String message, PlayerController target) {
        SwingUtilities.invokeLater(() -> {
            this.statusObservers.forEach((statusObserver) -> {
                statusObserver.onResourceResponse(message, target);
            });
        });
    }

}
