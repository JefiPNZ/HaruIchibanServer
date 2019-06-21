package br.udesc.ceavi.ppr.haruichiban.servidor.control;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.PackageServerInput;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.PackageServerOutput;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.ModelPlayer;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controlador de Player, servirar para controlar as acoes do player no jogo
 *
 * @author Gustavo C Santos
 * @since 08/05/2019
 *
 */
public class PlayerController implements IPlayerController {

    /**
     * Representa o jogador.
     */
    private ModelPlayer player;

    /**
     * Atributo que defini o titulo do jogador
     */
    private TitleOfGardener title;

    /**
     * Este guarda a flor do turno
     */
    private Flor florEmJogo;


    /**
     * Controller do fluxo
     */
    private IFluxoController fluxoController;

    /**
     * Usado Para controller de fase
     */
    private String playerStage;

    /**
     * Socket e seus recursos
     */
    private Socket socket;
    private PackageServerInput packageServerInput;
    private PackageServerOutput packageServerOutput;

    /**
     * Atributo usado para definir se o jogador esta conectado
     */
    private boolean conectado;

    private boolean logadoComSucesso;

    /**
     *
     * @param tamanhoDeckInicial tamano do deck
     * @param color cor do jogador
     *
     */
    public PlayerController(int tamanhoDeckInicial, Color color) {
        this.player = new ModelPlayer(tamanhoDeckInicial, color);
    }

    public void setSocket(Socket socket, IPlayerController oponent, String nome) {
        try {
            this.socket = socket;
            packageServerInput = new PackageServerInput(new Scanner(socket.getInputStream()), this, oponent, nome + " Input");
            packageServerOutput = new PackageServerOutput(new PrintWriter(socket.getOutputStream()), nome + " Output");
            conectado = true;
            GameServidorController.getInstance().threadPoolExecute(packageServerInput);
            GameServidorController.getInstance().threadPoolExecute(packageServerOutput);
        } catch (IOException ex) {
            Logger.getLogger(PlayerController.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    @Override
    public void setTitle(TitleOfGardener title) {
        this.title = title;
    }

    public void addPontos(int pontos) {
        player.addPontos(pontos);
    }

    public ModelPlayer getPlayer() {
        return player;
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public int getPileSize() {
        return player.getListaDeFlores().size();
    }

    @Override
    public Color getColor() {
        return player.getColor();
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public synchronized List<Object> getHand() {
        return player.getListaMao().stream().map(flor -> flor.getValor()).collect(Collectors.toList());
    }

    @Override
    public Flor getFlower() {
        return florEmJogo;
    }

    @Override
    public void getFlorFromHand(int x) {
        florEmJogo = player.getFlorFromHand(x);
    }

    @Override
    public Flor removeFlower() {
        Flor flor = florEmJogo;
        florEmJogo = null;
        return flor;
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public int getPlayerScore() {
        return this.player.getPoints();
    }


    /**
     * {@inheritdoc}
     */
    @Override
    public void setPlayerStage(String playerStage) {
        this.playerStage = playerStage;
    }

    @Override
    public boolean isLogadoComSucesso() {
        return logadoComSucesso;
    }

    @Override
    public void setLogadorComSucesso(boolean logadoComSucesso) {
        this.logadoComSucesso = logadoComSucesso;
    }

    /**
     * {@inheritdoc}
     */
    @Override
    public String getPlayerStage() {
        return playerStage;
    }

    @Override
    public boolean haveFlowers() {
        return player.haveFlowers();
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

    /**
     * Retorna se o jogador ainda esta na partida
     *
     * @return
     */
    @Override
    public boolean isConnectado() {
        return this.conectado;
    }

    @Override
    public void initDeck() {
        player.initDeck();
    }

    @Override
    public TitleOfGardener getTitulo() {
        return title;
    }

    @Override
    public void zerarPontos() {
        player.addPontos(-player.getPoints());
    }

    @Override
    public void clearDeck() {
        player.clearDeck();
    }

    @Override
    public PackageServerOutput getPackageServerOutput() {
        return packageServerOutput;
    }

}
