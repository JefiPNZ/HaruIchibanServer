package br.udesc.ceavi.ppr.haruichiban.model.animais;

import br.udesc.ceavi.ppr.haruichiban.model.PecaTabuleiro;
import br.udesc.ceavi.ppr.haruichiban.model.TipoPeca;
import java.awt.Color;
/**
 *
 * @author GustavoSantos
 * @since 09/05/2019
 *
 */
public abstract class Animal extends PecaTabuleiro {

    public Animal(float rotacao, Color cor) {
        super(rotacao, cor);
    }

    @Override
    public TipoPeca getTipo() {
        return TipoPeca.ANIMAL;
    }
}