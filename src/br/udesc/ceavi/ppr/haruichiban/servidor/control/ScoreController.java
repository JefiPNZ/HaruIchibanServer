package br.udesc.ceavi.ppr.haruichiban.servidor.control;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IScoreController;

/**
 *
 * @author Jeferson Penz
 */
public class ScoreController implements IScoreController{

    @Override
    public int getTopPlayerScore() {
        return GameServidorController.getInstance().getTopPlayer().getPlayerScore();
    }

    @Override
    public int getBotPlayerScore() {
        return GameServidorController.getInstance().getBottomPlayer().getPlayerScore();
    }
    
}
