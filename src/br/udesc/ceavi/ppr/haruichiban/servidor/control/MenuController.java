package br.udesc.ceavi.ppr.haruichiban.servidor.control;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IMenuController;

/**
 *
 * @author Jeferson Penz
 */
public class MenuController implements IMenuController{

    @Override
    public void reiniciar() {
        GameServidorController.getInstance().stop(null);
    }

    @Override
    public void finalizar() {
        System.exit(0);
    }
    
}
