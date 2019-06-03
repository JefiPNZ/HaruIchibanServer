package br.udesc.ceavi.ppr.haruichiban.control;

/**
 *
 * @author Jeferson Penz
 */
public class ScoreController implements IScoreController{

    @Override
    public int getTopPlayerScore() {
        return GameController.getInstance().getTopPlayer().getPlayerScore();
    }

    @Override
    public int getBotPlayerScore() {
        return GameController.getInstance().getBottomPlayer().getPlayerScore();
    }
    
}
