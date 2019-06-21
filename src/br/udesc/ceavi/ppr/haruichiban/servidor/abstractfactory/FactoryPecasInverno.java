package br.udesc.ceavi.ppr.haruichiban.servidor.abstractfactory;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.FolhaSeca;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.animais.Pinguim;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.filhote.OvoPinguim;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.animais.Animal;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.filhote.Filhote;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.ModelPlayer;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.FlorInverno;
import java.awt.Color;

/**
 *
 * @author Jeferson Penz
 */
public class FactoryPecasInverno extends FactoryPecas{

    @Override
    public Flor createFlor(Color cor, int valor, ModelPlayer playerOrigem) {
        return new FlorInverno(GameServidorController.getInstance().getRandomizer().nextFloat() * 20, cor, valor, playerOrigem);
    }

    @Override
    public Folha createFolha() {
        return new FolhaSeca(GameServidorController.getInstance().getRandomizer().nextFloat() * 360);
    }

    @Override
    public Animal createAnimal(Color cor) {
        return new Pinguim(GameServidorController.getInstance().getRandomizer().nextFloat() * 360, cor);
    }

    @Override
    public Filhote createFilhote(Color cor) {
        return new OvoPinguim(GameServidorController.getInstance().getRandomizer().nextFloat() * 360, cor);
    }
    
}
