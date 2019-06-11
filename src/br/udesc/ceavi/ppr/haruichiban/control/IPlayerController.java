package br.udesc.ceavi.ppr.haruichiban.control;

//import br.udesc.ceavi.ppr.haruichiban.control.observers.PlayerPanelObserver;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarJuniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarSeniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarUntitledGardenerException;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.state.TitleOfGardener;
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

    public int getPlayerScore();

    public void setFase(Fase fase);

    public Fase getFase();

    public void choseFlowerDeck();

    public void devolverFlorAoDeck();

    public void chooseFlowerDeckEnd(int x);

    public void getFlorFromHand(int x);

//    public void addObserver(PlayerPanelObserver obs);
//
//    public void setFluxoController(IFluxoController aThis);
    public void becomeSeniorGardener() throws PlayNaoPodeSeTornarSeniorException;

    public void becomeJuniorGardener() throws PlayNaoPodeSeTornarJuniorException;

    public void becomeUntitledGardener() throws PlayNaoPodeSeTornarUntitledGardenerException;

    public void putFlowerTable();

    public void fristWint();

    public void newDarkLeaf();

    public Flor getFlower();

    public TitleOfGardener getTitle();

    public void notifySimples(String messagem);

    public void setTitle(TitleOfGardener untitledGardener);

    public void notifySemTitulo();

    public void notifyYouAJunior();

    public void notifyYouASenior();

    public Flor removeFlower();

    public boolean haveFlowers();

    public void setFluxoController(IFluxoController fluxoController);

    public Color getColor();
    
    public RequestProcess getCanal();

    public void initDeck();

    public boolean isConnectado();

    public boolean isTop();

    public Socket getSocket();
    
    public boolean aguardaPronto();

}
