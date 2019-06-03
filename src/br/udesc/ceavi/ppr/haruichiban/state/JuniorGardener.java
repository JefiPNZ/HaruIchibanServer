package br.udesc.ceavi.ppr.haruichiban.state;

import br.udesc.ceavi.ppr.haruichiban.boardmovement.JuniorFirstWind;
import br.udesc.ceavi.ppr.haruichiban.boardmovement.JuniorFlowerBoard;
import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.control.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarSeniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarJuniorException;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;

/**
 *
 * @author GustavoSantos
 * @since 03/05/2019
 *
 */
public class JuniorGardener implements TitleOfGardener {

    @Override
    public void becomeUntitledGardener(IPlayerController aThis) {
        aThis.setTitle(new UntitledGardener());
        aThis.notifySemTitulo();
    }

    @Override
    public void becomeJuniorGardener(IPlayerController aThis) throws PlayNaoPodeSeTornarJuniorException {
        throw new PlayNaoPodeSeTornarJuniorException("Este usuario já é um Junior");
    }

    @Override
    public void becomeSeniorGardener(IPlayerController aThis) throws PlayNaoPodeSeTornarSeniorException {
        throw new PlayNaoPodeSeTornarSeniorException("Este usuario é um Junior, e não pode se tornar um senior");
    }

    @Override
    public void putFlowerTable(IPlayerController aThis) {
        IBoardController boardController = GameController.getInstance().getBoardController();
        IFluxoController fluxoController = GameController.getInstance().getFluxoController();

        JuniorFlowerBoard juniorFlowerBoard = new JuniorFlowerBoard(aThis, boardController, fluxoController);
        juniorFlowerBoard.addPoint(boardController.getFolhaEscura());
        juniorFlowerBoard.execute();
    }

    @Override
    public void firstWind(IPlayerController aThis) {
        aThis.notifySimples("Escolha Qual Folha Deseja Mover");
        IBoardController boardController = GameController.getInstance().getBoardController();
        IFluxoController fluxoController = GameController.getInstance().getFluxoController();
        JuniorFirstWind juniorFirstWind = new JuniorFirstWind(aThis, boardController, fluxoController);
        boardController.initBoardMovement(juniorFirstWind);
    }

    @Override
    public void newDarkLeaf(IPlayerController aThis) {
        IFluxoController fluxoController = GameController.getInstance().getFluxoController();
        aThis.setFase(fluxoController.newDarkLeafEnd());
        fluxoController.newDarkLeaf();
    }

}
