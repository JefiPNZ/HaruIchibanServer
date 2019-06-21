package br.udesc.ceavi.ppr.haruichiban.servidor.abstractfactory;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.filhote.Filhote;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.animais.Animal;
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
