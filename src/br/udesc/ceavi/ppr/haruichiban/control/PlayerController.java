package br.udesc.ceavi.ppr.haruichiban.control;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

import br.udesc.ceavi.ppr.haruichiban.command.ChooseFlowerPlayer;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarJuniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarSeniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarUntitledGardenerException;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.state.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.state.UntitledGardener;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador de Player, servirar para controlar as acoes do player no jogo
 *
 * @author Gustavo C Santos
 * @since 08/05/2019
 *
 */
public class PlayerController extends Thread implements IPlayerController {

    /**
     * Representa o jogador.
     */
    private ModelPlayer player;

    /**
     * Padrao State para guiar as açoes do player perante o estado dele
     */
    private TitleOfGardener title;

    /**
     * Este guarda a flor do turno
     */
    private Flor florEmJogo;

    private IFluxoController fluxoController;

    private Fase fase;

    private Scanner in;
    private PrintWriter out;
    private boolean jogador;

    /**
     *
     * @param cor identifica a cor das flores do jogador
     * @param tamanhoDoDeck tamanho do deck
     */
    public PlayerController(Color cor, int tamanhoDoDeck) {
        this.title = new UntitledGardener();
        this.player = new ModelPlayer(cor, tamanhoDoDeck);
        jogador = true;
    }

    @Override
    public void becomeUntitledGardener() throws PlayNaoPodeSeTornarUntitledGardenerException {
        title.becomeUntitledGardener(this);
    }

    @Override
    public void becomeJuniorGardener() throws PlayNaoPodeSeTornarJuniorException {
        title.becomeJuniorGardener(this);
    }

    @Override
    public void becomeSeniorGardener() throws PlayNaoPodeSeTornarSeniorException {
        title.becomeSeniorGardener(this);
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

    @Override
    public int getPileSize() {
        return player.getListaDeFlores().size();
    }

    public Color getColor() {
        return player.getColor();
    }

    @Override
    public synchronized List<Object> getHand() {
        return player.getListaMao().stream().map(flor -> flor.getValor()).collect(Collectors.toList());
    }

    @Override
    public void choseFlowerDeck() {
        SwingUtilities.invokeLater(() -> {
            sendResource("C,ESCOLHAUMAFLOR");
        });
    }

    @Override
    public void chooseFlowerDeckEnd(int x) {
        GameController.getInstance().executeCommand(new ChooseFlowerPlayer(this, x));
        fase = fluxoController.chooseFlowerEnd();
        fluxoController.chooseFlower();
        notifiyFlowerChoise();
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

    @Override
    public void putFlowerTable() {
        title.putFlowerTable(this);
    }

    @Override
    public void fristWint() {
        title.firstWind(this);
    }

    @Override
    public void newDarkLeaf() {
        title.newDarkLeaf(this);
    }

    @Override
    public TitleOfGardener getTitle() {
        return title;
    }

    @Override
    public void devolverFlorAoDeck() {
        player.devolverFlor(removeFlower());
    }

    @Override
    public void setFluxoController(IFluxoController aThis) {
        this.fluxoController = aThis;
    }

    public IFluxoController getFluxoController() {
        return fluxoController;
    }

    @Override
    public int getPlayerScore() {
        return this.player.getPoints();
    }

    @Override
    public void notifySemTitulo() {
        sendResource("S,YouAreSemTitulo");
    }

    @Override
    public void notifyYouAJunior() {
        sendResource("S,YouAreJunior");
    }

    @Override
    public void notifyYouASenior() {
        sendResource("S,YouASenior");
    }

    @Override
    public void notifySimples(String messagem) {
        sendResource("M," + messagem);
    }

    private void notifiyFlowerChoise() {
        sendResource("C,EscolhaUmaFlorEnd");
    }

    @Override
    public void setFase(Fase fase) {
        this.fase = fase;
    }

    @Override
    public Fase getFase() {
        return fase;
    }

    @Override
    public boolean haveFlowers() {
        return player.haveFlowers();
    }

    public void setSocket(Socket mySocket, String msg) {
        try {
            this.in = new Scanner(mySocket.getInputStream());
            this.out = new PrintWriter(mySocket.getOutputStream(), true);
            GameController.getInstance().notifyClientRequest("Jogador " + msg + " conectou.");
            sendResource("Vc e o player " + msg);
            this.start();
        } catch (IOException e) {
            System.exit(0);
        }
    }

    @Override
    public void run() {
        GameController gcInstance = GameController.getInstance();
        while (jogador) {
            if (in.hasNext()) {
                String requisicao = in.nextLine();
                gcInstance.notifyClientRequest("Recebeu uma requisição.");
                switch (requisicao.split(",")[0]) {
                    case "I":// Requisi�oes para esta classe
                        gcInstance.notifyClientRequest("Comando Interno:");
                        gcInstance.notifyClientRequest(requisicao);
                        comandoInterno(requisicao);
                        break;
                    case "E"://// Requisi�oes para GameController
                        gcInstance.notifyClientRequest("Comando Externo:");
                        gcInstance.notifyClientRequest(requisicao);
                        gcInstance.clientRequest(requisicao, this);
                        break;
                    case "END"://// Requisicao Para Fechar o Jogo
                        gcInstance.notifyClientRequest("Finalizou a execução.");
                        jogador = false;
                        break;
                    default:
                        gcInstance.notifyClientRequest("Comando Desconhecido.");
                        break;
                }
            }
            else {
//                if(!this.socket.isConnected()){
//                    this.jogador = false;
//                }
//                else {
                    try {
                        try {
                            in.next();
                        }
                        catch(NoSuchElementException ex){
                            this.jogador = false;
                        }
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {}
//                }
            }
        }
    }

    private void comandoInterno(String requisicao) {
        switch (requisicao.split(",")[1]) {
            case "MYCOLOR":
                sendResource(new Gson().toJson(getColor()));
                GameController.getInstance().notifyClientRequest("Jogador: " + getColor());
                return;
            case "HAND":
                StringBuilder sb = new StringBuilder();
                this.getHand().forEach(valor -> sb.append(valor).append(','));
                sb.substring(0, sb.length() - 2);
                sendResource(sb.toString().substring(0, sb.length() - 1));
                return;
            case "PILESIZE":
                sendResource("" + this.getPileSize());
                return;
            case "CHOSEFLOR":
                int index = Integer.parseInt(requisicao.split(",")[2]);
                chooseFlowerDeckEnd(index);
                return;
            default:
                return;
        }

    }
    
    public boolean isConnectado(){
        return this.jogador;
    }

    public void sendResource(String resource) {
        out.println(resource);
        out.flush();
    }
}
