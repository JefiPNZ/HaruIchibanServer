package br.udesc.ceavi.ppr.haruichiban.boardmovement;

import br.udesc.ceavi.ppr.haruichiban.command.FlowerBoardCommand;
import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import java.awt.Point;
import br.udesc.ceavi.ppr.haruichiban.control.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.utils.Direction;

/**
 *
 * @author Gustavo C Santos
 * @since 13/05/2019
 *
 */
public class JuniorFlowerBoard implements BoardMovement {

    private IPlayerController player;
    private IBoardController boardController;
    private Point localLerf;
    private final IFluxoController fluxoController;

    public JuniorFlowerBoard(IPlayerController player, IBoardController boardController,
            IFluxoController fluxoController) {
        this.player = player;
        this.boardController = boardController;
        this.fluxoController = fluxoController;
    }

    @Override
    public boolean addPoint(Point positionBoard) {
        this.localLerf = positionBoard;
        if (isReady()) {
            execute();
        }
        return true;
    }

    public boolean isReady() {
        return localLerf != null;
    }

    @Override
    public synchronized void execute() {
        GameController.getInstance().executeCommand(
                new FlowerBoardCommand(
                        player.removeFlower(),
                        boardController.getBoardTile(localLerf)));
        boardController.renderBoard();
        player.setFase(fluxoController.putFlowerTableEnd());
        fluxoController.putFlowerTable();
        player.notifySimples("");
    }

    @Override
    public boolean tableInteraction() {
        return false;
    }

    @Override
    public boolean addDiretion(Direction deretion) {
        return false;
    }

}
