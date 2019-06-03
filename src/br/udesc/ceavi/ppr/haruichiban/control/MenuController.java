package br.udesc.ceavi.ppr.haruichiban.control;

/**
 *
 * @author Jeferson Penz
 */
public class MenuController implements IMenuController{

    @Override
    public void reiniciar() {
        GameController.getInstance().stop(null);
    }

    @Override
    public void finalizar() {
        System.exit(0);
    }
    
}
