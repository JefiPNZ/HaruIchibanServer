package br.udesc.ceavi.ppr.haruichiban.abstractfactory;

import br.udesc.ceavi.ppr.haruichiban.model.animais.Sapo;
import br.udesc.ceavi.ppr.haruichiban.model.animais.Animal;
import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.model.filhote.Filhote;
import br.udesc.ceavi.ppr.haruichiban.model.flores.FlorPrimavera;
import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.model.folha.VitoriaRegia;
import br.udesc.ceavi.ppr.haruichiban.model.filhote.OvoSapo;
import java.awt.Color;

/**
 *
 * @author Jeferson Penz
 */
public class FactoryPecasPrimavera extends FactoryPecas{

    
    @Override
    public Flor createFlor(Color cor, int valor, ModelPlayer playerOrigem) {
        return new FlorPrimavera(GameController.getInstance().getRandomizer().nextFloat() * 20, cor, valor, playerOrigem);
    }

    @Override
    public Folha createFolha() {
        return new VitoriaRegia(GameController.getInstance().getRandomizer().nextFloat() * 360);
    }

    @Override
    public Animal createAnimal(Color cor) {
        return new Sapo(GameController.getInstance().getRandomizer().nextFloat() * 360, cor);
    }

    @Override
    public Filhote createFilhote(Color cor) {
        return new OvoSapo(GameController.getInstance().getRandomizer().nextFloat() * 360, cor);
    }
    
}
