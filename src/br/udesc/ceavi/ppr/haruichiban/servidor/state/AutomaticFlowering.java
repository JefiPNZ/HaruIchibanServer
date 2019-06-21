package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.command.FlowerBoardCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.MoveAnimalCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.animais.Animal;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.flores.Flor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gustavo C
 */
public class AutomaticFlowering extends FluxoState {

    private Animal topPlayerAnimal;
    private Animal bottomPlayerAnimal;
    private boolean hasAnimal;
    private IPlayerController perdedor;
    private IPlayerController vencedor;

    public AutomaticFlowering() {
        super();
        perdedor = game.getJardineiroJunior(TitleOfGardener.SCREAM_LOSER);
        vencedor = game.getJardineiroJunior(TitleOfGardener.SCREAM_WINNER);
    }

    @Override
    public void systemCommand() {

        Flor florBottom = bottomPlayer.removeFlower();
        Flor florTop = topPlayer.removeFlower();

        Point[] animals = boardController.getAnimalsPoint();

        this.topPlayerAnimal = (Animal) boardController.getBoardTile(animals[0]).getFolha().removerPecaDeFlor();
        this.bottomPlayerAnimal = (Animal) boardController.getBoardTile(animals[1]).getFolha().removerPecaDeFlor();

        //Corrige um eventual erro de referenicia
        if (topPlayerAnimal.getCor().
                equals(bottomPlayer.getColor())) {
            Animal a = bottomPlayerAnimal;
            bottomPlayerAnimal = topPlayerAnimal;
            topPlayerAnimal = a;

            Point p = animals[1];
            animals[1] = animals[0];
            animals[0] = p;
        }

        game.executeCommand(new FlowerBoardCommand(florTop, game.getBoardController().getBoardTile(animals[0])));
        game.executeCommand(new FlowerBoardCommand(florBottom, game.getBoardController().getBoardTile(animals[1])));

        this.hasAnimal = boardController.removeAnimal();
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {
        //Perdedor
        perdedor.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Aguarde O Movimento do Vencedor Da Batalha");
        //Vencedor
        vencedor.getPackageServerOutput().newGet(ModelGet.GAME_FASE, AUTOMATIC_FLOWERING);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String fase) {
        List<String> lista = gson.fromJson(fase, ArrayList.class);
        //Tratamento do Movimento
        //Index 0 : animal TOP newPosicao(Point.class)
        //Index 1 : animal BOTTOM newPosicao(Point.class)

        //Movendo Animais -->>>
        //Animal A
        Point newAnimalTopPoint = gson.fromJson(lista.get(0), Point.class);
        Point newAnimalBottomPoint = gson.fromJson(lista.get(1), Point.class);

        game.executeCommand(new MoveAnimalCommand(topPlayerAnimal, game.getBoardController().getBoardTile(newAnimalTopPoint)));
        game.executeCommand(new MoveAnimalCommand(bottomPlayerAnimal, game.getBoardController().getBoardTile(newAnimalBottomPoint)));
        //Movendo Animais <<<<--
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        nextFase();
    }

    @Override
    public void nextFase() {
        game.getFluxoController().setFluxoState(new CheckBoard(ChooseFlower.class));
    }

    @Override
    public String getDescricaoLog() {
        return "Flora\u00e7\u00e3o autom\u00e1tica";
    }

}
