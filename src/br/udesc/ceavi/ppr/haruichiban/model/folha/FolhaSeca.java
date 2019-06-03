package br.udesc.ceavi.ppr.haruichiban.model.folha;

import java.awt.Color;

/**
 *
 * @author Gustavo C Santos
 * @since 10/05/2019
 * 
 */
public class FolhaSeca extends Folha {

    public FolhaSeca(float rotacao) {
        super(rotacao);
    }

    @Override
    public Color getCor() {
        return this.isEscura() ? new Color(75, 128, 195) : new Color(176, 206, 255);
    }

}
