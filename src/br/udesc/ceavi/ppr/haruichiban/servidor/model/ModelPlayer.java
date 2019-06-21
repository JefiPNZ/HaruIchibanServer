package br.udesc.ceavi.ppr.haruichiban.servidor.model;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Jeferson Penz, Gustavo C. Santos
 */
public class ModelPlayer {

    private int points;
    private List<Flor> listaDeFlores;
    private List<Flor> listaMao;
    private Color myColor;
    private int tamanhoDeckInicial = 0;

    public ModelPlayer(int tamanhoDeckInicial, Color myColor) {
        this.points = 0;
        listaDeFlores = new ArrayList<>();
        listaMao = new ArrayList<>();
        this.myColor = myColor;
        this.tamanhoDeckInicial = tamanhoDeckInicial;
        initDeck();
    }

    public void initDeck() {
        for (int i = 1; i < tamanhoDeckInicial; i++) {
            listaDeFlores.add(GameServidorController.getInstance().getFactoryPecas().createFlor(myColor, i, this));
        }
        controlHand();
    }

    public void addPontos(int newPoints) {
        this.points += newPoints;
    }

    public int getPoints() {
        return points;
    }

    public void controlHand() {
        while (listaMao.size() < 3 && listaDeFlores.size() > 0) {
            listaMao.add(listaDeFlores.remove((int) (Math.random() * listaDeFlores.size())));
        }
    }

    public List<Flor> getListaDeFlores() {
        return listaDeFlores;
    }

    public List<Flor> getListaMao() {
        return listaMao;
    }

    public Flor getFlorFromHand(int i) {
        Flor florARemover = listaMao.remove(i);
        controlHand();
        return florARemover;
    }

    public Color getColor() {
        return myColor;
    }

    public void devolverFlor(Flor florEmJogo) {
        this.listaDeFlores.add(florEmJogo);
        controlHand();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelPlayer other = (ModelPlayer) obj;
        return Objects.equals(this.myColor, other.myColor);
    }

    public boolean haveFlowers() {
        return !listaMao.isEmpty();
    }

    public void setMyColor(Color myColor) {
        this.myColor = myColor;
    }

    public void clearDeck() {
        this.listaDeFlores.clear();
        this.listaMao.clear();
    }

}
