package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.decorator.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.animais.Animal;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class MoveAnimalCommand implements Command {

    private Animal animal;
    private IModelBoardTile boardTile;

    public MoveAnimalCommand(Animal animal, IModelBoardTile boardTile) {
        this.animal = animal;
        this.boardTile = boardTile;
    }

    @Override
    public void execute() {
        boardTile.getFolha().colocarPecaNaFolha(animal);
    }

}
