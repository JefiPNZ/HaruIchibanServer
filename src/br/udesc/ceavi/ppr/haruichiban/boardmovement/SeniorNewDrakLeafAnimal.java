package br.udesc.ceavi.ppr.haruichiban.boardmovement;

import br.udesc.ceavi.ppr.haruichiban.command.MoveAnimalCommand;
import br.udesc.ceavi.ppr.haruichiban.command.NewDrakLeafCommand;
import br.udesc.ceavi.ppr.haruichiban.control.Fase;
import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.control.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.animais.Animal;
import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 13/05/2019
 *
 */
public class SeniorNewDrakLeafAnimal extends SeniorNewDrakLeaf implements BoardMovement {

    private Point animalLocal;
    private Animal animal;

    public SeniorNewDrakLeafAnimal(IPlayerController player,
            IBoardController boardController, IFluxoController fluxoController, Point animalLocal) {
        super(player, boardController, fluxoController);
        this.animalLocal = animalLocal;
        super.localLerf = null;

    }

    @Override
    public boolean addPoint(Point positionBoard) {
        if (animal != null && localLerf == null) {
            if (validar(positionBoard)) {
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

    private boolean validar(Point positionBoard) {
        ModelBoardTile boardTile = boardController.getBoardTile(positionBoard);
        if (!boardTile.hasFolha()) {
            player.notifySimples("Animal Apenas Pode Ser Colocado Na Folha");
            return true;
        }
        if (boardTile.getFolha().hasPeca()) {
            player.notifySimples("Animal Apenas Pode Ser Colocado Folha Vazia");
            this.localLerf = null;
            return true;
        }
        if (boardTile.getFolha().isEscura()) {
            player.notifySimples("Animal Apenas Pode Ser Colocado Folha Clara");
            this.localLerf = null;
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
                new MoveAnimalCommand(
                        animal,
                        boardController.getBoardTile(localLerf)));
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

    public void executePutFlower() {
        ModelBoardTile boardTile = boardController.getBoardTile(animalLocal);
        GameController gameController = GameController.getInstance();

        this.animal = (Animal) boardTile.getFolha().removerPecaDeFlor();
        gameController.executeCommand(
                new NewDrakLeafCommand(boardController, animalLocal));
        boardController.renderBoard();
        player.setFase(Fase.MOVE_ANIMAL);
        boardController.initBoardMovement(this);
//        gameController.notificaMudancaEstado("Mover Animal");
    }

    @Override
    public boolean addDiretion(Diretion deretion) {
        return false;
    }

}
