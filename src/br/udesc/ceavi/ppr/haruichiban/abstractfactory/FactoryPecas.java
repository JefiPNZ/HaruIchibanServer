package br.udesc.ceavi.ppr.haruichiban.abstractfactory;

import br.udesc.ceavi.ppr.haruichiban.model.filhote.Filhote;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.animais.Animal;
import java.awt.Color;

/**
 *
 * @author Jeferson Penz
 */
public abstract class FactoryPecas {
    
    public abstract Flor createFlor(Color cor, int valor, ModelPlayer playerOrigem);
    public abstract Folha createFolha();
    public abstract Animal createAnimal(Color cor);
    public abstract Filhote createFilhote(Color cor);
    
}
