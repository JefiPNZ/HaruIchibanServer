package br.udesc.ceavi.ppr.haruichiban.servidor.model.folha;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.filhote.Filhote;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.PecaTabuleiro;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TipoPeca;
import com.google.gson.Gson;
import java.awt.Color;

/**
 *
 * @author GustavoSantos
 * @since 09/05/2019
 *
 */
public abstract class Folha extends PecaTabuleiro {

    protected boolean isShowDarkSide;
    protected boolean sempreEscura;
    protected PecaTabuleiro peca;
    protected Filhote filhote;

    public Folha(float rotacao) {
        super(rotacao, null);
        this.sempreEscura = false;
        this.isShowDarkSide = false;
    }

    public boolean isSempreEscura() {
        return sempreEscura;
    }

    public void setSempreEscura(boolean sempreEscura) {
        this.sempreEscura = sempreEscura;
    }

    /**
     * Retorna se a folha esta mostrando seu lado escuro
     *
     * @return true esta mostrando,false se não esta mostrando
     */
    public boolean isEscura() {
        return this.sempreEscura || isShowDarkSide;
    }

    /**
     * Retorna se tem um filhote
     *
     * @return true se há um filhote,false se não há
     */
    public boolean hasFilhote() {
        return filhote != null;
    }

    /**
     * Retorna se tem um Peca
     *
     * @return true se há um Peca,false se não há
     */
    public boolean hasPeca() {
        return this.peca != null;
    }

    /**
     * Este metodo vira a Nenufare para essa mostar seu lado escuro, lança
     * CanNotChangeSideNenufareException quando o lado escuro ja esta sendo
     * mostardo
     *
     * @return
     */
    public boolean virarFolha() {
        if (sempreEscura || isShowDarkSide) {
            return false;
        }
        isShowDarkSide = true;
        return true;
    }

    public void mostrarParteClara() {
        if (!sempreEscura) {
            isShowDarkSide = false;
        }
    }

    /**
     * Este metodo cria uma associação de nenufare com a peca, lanca
     * NenufareJaPossuiUmaPecaEmCimaException quando nenufar ja esta associado
     * com outra peca
     *
     * @param peca
     * @return 
     */
    public boolean colocarPecaNaFolha(PecaTabuleiro peca) {
        if (hasPeca()) {
            return false;
        }
        this.peca = peca;
        return true;
    }

    public boolean colocarFilhoteNaFolha(Filhote filhote) {
        if (hasFilhote()) {
            return false;
        }
        this.filhote = filhote;
        return true;
    }

    /**
     * Este metodo acaba com a associação de nenufare com a peca
     *
     * @return retorna peca removida
     */
    public PecaTabuleiro removerPecaDeFlor() {
        PecaTabuleiro pecaPegar = peca;
        peca = null;
        return pecaPegar;
    }

    public PecaTabuleiro getPeca() {
        return peca;
    }

    @Override
    public Color getCor() {
        return this.isEscura() ? new Color(10, 125, 10) : new Color(15, 205, 15);
    }

    @Override
    public TipoPeca getTipo() {
        return TipoPeca.FOLHA;
    }

    public Filhote getFilhote() {
        return filhote;
    }

    /**
     * Retorna se tem Um Animal
     *
     * @return true se tem um Animal,false se nao
     */
    public boolean hasAnimal() {
        return hasPeca() && peca.getTipo() == TipoPeca.ANIMAL;
    }

    @Override
    public String toString() {
        return "{" + "escura=" + isShowDarkSide + ", peca=" + peca + ", filhote=" + filhote + '}';
    }

}
