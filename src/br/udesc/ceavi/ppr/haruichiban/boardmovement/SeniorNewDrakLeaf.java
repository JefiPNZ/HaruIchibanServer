package br.udesc.ceavi.ppr.haruichiban.boardmovement;

import br.udesc.ceavi.ppr.haruichiban.command.NewDrakLeafCommand;
import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.control.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 13/05/2019
 *
 */
public class SeniorNewDrakLeaf implements BoardMovement {

    protected IPlayerController player;
    protected IBoardController boardController;
    protected Point localLerf;
    protected IFluxoController fluxoController;

    public SeniorNewDrakLeaf(IPlayerController player, IBoardController boardController, IFluxoController fluxoController) {
        this.player = player;
        this.boardController = boardController;
        this.fluxoController = fluxoController;
    }

    @Override
    public boolean addPoint(Point positionBoard) {
        if (localLerf == null) {

            if (validandoPosicao(positionBoard)) {
                return false;
            }

            localLerf = positionBoard;
            if (isReady()) {
                execute();
            }
            return true;
        }
        return false;
    }

    private boolean validandoPosicao(Point positionBoard) {
        if(positionBoard == null){
            return false;
        }
        ModelBoardTile boardTile = boardController.getBoardTile(positionBoard);
        if (!boardTile.hasFolha()) {
            player.notifySimples("A Posicao Escolhida Não Tem Folha");
            return true;
        }
        if (boardTile.getFolha().hasAnimal()) {
            player.notifySimples("Folha Escura, Escolha A Nova Posição Do Animal");
            boardController.removeBoardMovement();
            SeniorNewDrakLeafAnimal juniorFlowerBoard
                    = new SeniorNewDrakLeafAnimal(player, boardController, fluxoController, positionBoard);
            boardController.initBoardMovement(juniorFlowerBoard);
            juniorFlowerBoard.executePutFlower();

            return true;
        }
        if (boardTile.getFolha().hasPeca()) {
            player.notifySimples("A Posicao Escolhida Ja Tem Flor");
            return true;
        }
        return false;
    }

    public boolean isReady() {
        return localLerf != null;
    }

    @Override
    public synchronized void execute() {
        GameController.getInstance().executeCommand(
                new NewDrakLeafCommand(boardController, localLerf));
        boardController.renderBoard();
        boardController.removeBoardMovement();
        player.setFase(fluxoController.newDarkLeafEnd());
        fluxoController.newDarkLeaf();
        player.notifySimples("");
    }

    @Override
    public boolean tableInteraction() {
        return true;
    }

    @Override
    public boolean addDiretion(Diretion deretion) {
        return false;
    }

}
