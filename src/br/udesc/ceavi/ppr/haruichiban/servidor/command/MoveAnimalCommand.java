package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.animais.Animal;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class MoveAnimalCommand implements Command {

    private Animal animal;
    private IModelBoardTile boardTile;
    private IBoardController boardController;
    /**
     * 
     * @param animal animal
     * @param boardTile nova Posicao do animal
     */
    public MoveAnimalCommand(Animal animal, IModelBoardTile boardTile) {
        this.animal = animal;
        this.boardTile = boardTile;
        this.boardController = GameServidorController.getInstance().getBoardController();
        
    }

    @Override
    public void execute() {
        boardTile.getFolha().colocarPecaNaFolha(animal);
    }

    @Override
    public String getDescricaoLog() {
        return "Movimentado animal " + this.animal + " para o tile: " + this.boardTile;
    }

    @Override
    public String getTipoLog() {
        return "Comando";
    }

}
