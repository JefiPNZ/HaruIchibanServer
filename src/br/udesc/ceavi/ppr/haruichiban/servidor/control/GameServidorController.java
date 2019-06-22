package br.udesc.ceavi.ppr.haruichiban.servidor.control;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.servidor.abstractfactory.FactoryPecas;
import br.udesc.ceavi.ppr.haruichiban.servidor.abstractfactory.FactoryPecasInverno;
import br.udesc.ceavi.ppr.haruichiban.servidor.abstractfactory.FactoryPecasPrimavera;
import br.udesc.ceavi.ppr.haruichiban.servidor.builder.BoardBuilder;
import br.udesc.ceavi.ppr.haruichiban.servidor.builder.BoardGigaBuilder;
import br.udesc.ceavi.ppr.haruichiban.servidor.builder.BoardNormalBuilder;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.Command;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.CommandInvoker;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.observers.ServerStatusObserver;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.utils.Images;
import java.awt.Color;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Classe Principal para inicialização e controle do estado da Aplicação.
 *
 * @author Jerfeson e Gustavo
 *
 * AVISO - NÃO USAR INSTANCEOF, COMPARAR USANDO GETCLASS() == .CLASS. - NÃO PODE
 * HAVER //JOptionPane NO CONTROLLER. - Quando adicionar um componente
 * transparente (como overlay por ex.), lembrar do setOpaque(false).
 */
public class GameServidorController {

    /**
     * Instância de referência do Singleton.
     */
    private static GameServidorController instance;

    /**
     * Retorna a instância existente do Singleton.
     *
     * @return A instância existente ou uma nova instância do jogo.
     */
    public synchronized static GameServidorController getInstance() {
        if (instance == null) {
            instance = new GameServidorController();
        }
        return instance;
    }
    /**
     * Constante para o nome do jogo.
     */
    public static final String GAME_NAME = "Haru Ichiban";

    /**
     * Responsável por gerar dados aleatórios.
     */
    private Random randomizer;

    /**
     * Semente fixa para geração estática de dados aleatórios.
     */
    private long fixedSeed;

    private BoardBuilder builderTabuleiro;

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

    private ExecutorService threadPool;

    /**
     * Mapa Ira Guardar A Referencia de Titulo para com os player
     */
    private Map<Integer, IPlayerController> tituloJardineiro = new HashMap<>();

    private List<ServerStatusObserver> statusObservers;

    /**
     * Classe para criação da instância do Singleton.
     */
    private GameServidorController() {
        this.randomizer = new Random();
        this.fixedSeed = this.randomizer.nextLong();
        this.threadPool = Executors.newCachedThreadPool();
        this.statusObservers = new ArrayList<>();
    }

    /**
     * Para a execução da lógica do jogo.
     *
     * @param varianteTabuleiro
     * @param tamanhoTabuleiro
     * @param topPlayerColor
     * @param bottomPlayerColor
     */
    public void begin(String varianteTabuleiro, String tamanhoTabuleiro, Color topPlayerColor, Color bottomPlayerColor) {
        this.notifyServer("Utilizando Mapa de " + varianteTabuleiro);
        switch (varianteTabuleiro) {
            default:
            case "Primavera":
                this.factoryPecas = new FactoryPecasPrimavera();
                Images.mapImagemPrimaveira();
                break;
            case "Inverno":
                this.factoryPecas = new FactoryPecasInverno();
                Images.mapImagemInverno();
                break;
        }
        this.notifyServer("Utilizando tabuleiro " + tamanhoTabuleiro);
        int tamanhoDeckInicial;
        switch (tamanhoTabuleiro) {
            default:
            case "Giga":
                this.builderTabuleiro = new BoardGigaBuilder();
                tamanhoDeckInicial = 13;
                break;
            case "Normal":
                this.builderTabuleiro = new BoardNormalBuilder();
                tamanhoDeckInicial = 9;
                break;
        }
        this.topPlayer = new PlayerController(tamanhoDeckInicial, topPlayerColor);
        this.notifyServer("Jogador do topo: " + this.topPlayer);
        this.bottomPlayer = new PlayerController(tamanhoDeckInicial, bottomPlayerColor);
        this.notifyServer("Jogador de baixo: " + this.bottomPlayer);
        this.controllerBoard = new BoardController();
        this.commandInvoker = new CommandInvoker();
        this.controlDeFluxo = new FluxoController(this);
    }

    /**
     * Para a execução da lógica do jogo.
     *
     * @param mensagem
     */
    public void stop(String mensagem) {
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
     * Retorna o gerador de dados aleatórios fixo. Este gerador apresenta sempre
     * os mesmo valores quando é criado.
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
        openChannel();
    }

    public IFluxoController getFluxoController() {
        return controlDeFluxo;
    }

    public void executeCommand(Command command) {
        this.commandInvoker.executeCommand(command);
    }

    private void openChannel() {
        threadPoolExecute(() -> {
            try (ServerSocket listener = new ServerSocket(60000)) {
                notifyServer("Aguardando Jogadores");
                
                Socket topPlayerSocket = listener.accept();
                notifyServer("Jogador Superior Entrou Com Endere\u00E7o: " + topPlayerSocket.getRemoteSocketAddress());
                topPlayer.setSocket(topPlayerSocket, bottomPlayer, "TOPPlayer");
               
                Socket bottomPlayerSocket = listener.accept();
                notifyServer("Jogador Inferior Entrou Com Endere\u00E7o: " + bottomPlayerSocket.getRemoteSocketAddress());
                bottomPlayer.setSocket(bottomPlayerSocket, topPlayer, "BOTTOMPlayer");

                controlDeFluxo.startGame();
                while (topPlayer.isConnectado() && bottomPlayer.isConnectado()) {
                    Thread.sleep(5000);
                }

                if (!bottomPlayer.isConnectado()) {
                    notifyServer("Jogador inferior desconectou...");
                } else {
                    notifyServer("Jogador superior desconectou...");
                }
                listener.close();
            } catch (Exception ex) {
                Logger.getLogger(GameServidorController.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    public void limparListaJardineiro() {
        tituloJardineiro.clear();
    }

    public void setJardineiro(TitleOfGardener titulo, IPlayerController jardineiro) {
        tituloJardineiro.put(titulo.getValor(), jardineiro);
    }

    public IPlayerController getJardineiroJunior(TitleOfGardener titulo) {
        return tituloJardineiro.get(titulo.getValor());
    }

    public String getHoraSimples() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public void threadPoolExecute(Runnable command) {
        threadPool.execute(command);
    }

    public void addServerStatusObserver(ServerStatusObserver obs) {
        this.statusObservers.add(obs);
    }
    
    public void notifyServer(Loggable message) {
        this.notifyServer("Execução de " + message.getTipoLog() + ": " + message.getDescricaoLog());
    }

    public void notifyServer(String message) {
        SwingUtilities.invokeLater(() -> {
            this.statusObservers.forEach((statusObserver) -> {
                statusObserver.onClientRequest(getHoraSimples() + " - " + message);
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
}
