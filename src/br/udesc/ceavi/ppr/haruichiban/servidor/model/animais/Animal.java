package br.udesc.ceavi.ppr.haruichiban.servidor.model.animais;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.PecaTabuleiro;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TipoPeca;
import br.udesc.ceavi.ppr.haruichiban.utils.Images;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    private static BufferedImage imagemPlataforma;
    public static BufferedImage getImagemPlataforma() throws IOException{
        if(imagemPlataforma == null){
            imagemPlataforma = Images.getImagem(Images.ANIMAL_PLATAFORMA);
        }
        return imagemPlataforma;
    }
}