package br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.PackageServerOutput;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import java.awt.Color;
import java.net.Socket;
import java.util.List;

/**
 * Interface para classes que realizam o controle da mão do jogador.
 *
 * @author Jeferson Penz
 */
public interface IPlayerController {

    /**
     * Retorna o tamanho da pilha do jogador.
     *
     * @return
     */
    public int getPileSize();

    /**
     * Retorna a mão do usuário.
     *
     * @return
     */
    public List<Object> getHand();

    /**
     * Retorna os pontos do player
     *
     * @return pontos do player
     */
    public int getPlayerScore();

    /**
     * Modifica o estado do jogador perante o State Atual
     *
     * @param playerStage novo Estado
     */
    public void setPlayerStage(String playerStage);

    /**
     * Retorna o estado do jogador perante o State Atual
     *
     * @return State Atual
     */
    public String getPlayerStage();

    public void setTitle(TitleOfGardener title);

    public Flor removeFlower();

    public boolean haveFlowers();

    public boolean isConnectado();

    public void initDeck();

    public Socket getSocket();

    public Color getColor();


    public TitleOfGardener getTitulo();

    /**
     * Reinicia o Deck
     */
    public void clearDeck();


    public PackageServerOutput getPackageServerOutput();

    boolean isLogadoComSucesso();

    public void setLogadorComSucesso(boolean b);

    public void addPontos(int parseInt);

    public void zerarPontos();

    void getFlorFromHand(int x);

    Flor getFlower();
}
