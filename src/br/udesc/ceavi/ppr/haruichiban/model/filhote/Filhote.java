package br.udesc.ceavi.ppr.haruichiban.model.filhote;

import br.udesc.ceavi.ppr.haruichiban.model.PecaTabuleiro;
import br.udesc.ceavi.ppr.haruichiban.model.TipoPeca;
import java.awt.Color;

/**
 * Esta Classe Representa O Filhote Do Animal
 * 
 * @author Gustavo C Santos
 * @since 10/05/2019
 * 
 */

public abstract class Filhote extends PecaTabuleiro{

    public Filhote(float rotacao, Color cor) {
        super(rotacao, cor);
    }

    @Override
    public TipoPeca getTipo() {
        return TipoPeca.FILHOTE;
    }

}
