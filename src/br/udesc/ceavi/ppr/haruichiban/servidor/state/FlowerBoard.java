package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.command.FlowerBoardCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.MoveAnimalCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
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
public class FlowerBoard extends FluxoState {

    public FlowerBoard() {
        super();
    }

    @Override
    public void systemCommand() {
        IPlayerController junior = game.getJardineiroJunior(TitleOfGardener.JUNIOR_TITULO);
        IPlayerController senior = game.getJardineiroJunior(TitleOfGardener.SENIOR_TITULO);

        Flor florJunior = junior.removeFlower();
        IModelBoardTile boardTile = boardController.getBoardTile(boardController.getFolhaEscura());

        GameServidorController.getInstance().executeCommand(
                new FlowerBoardCommand(
                        florJunior,
                        boardTile));

        junior.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        senior.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        junior.setPlayerStage(UPDATEDATA);
        junior.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Flor Colocada Automaticamente");

        junior.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Flores No Tabuleiro");
        senior.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Flores No Tabuleiro");
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {
        IPlayerController senior = game.getJardineiroJunior(TitleOfGardener.SENIOR_TITULO);
        senior.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Escolha o Local Para Sua Flor");
        senior.setPlayerStage(START_BEING_MADE);
        senior.getPackageServerOutput().newGet(ModelGet.GAME_FASE, PUT_FLOWER_SENIOR);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String parametro) {
        //Tratamento de jogada
        // index 0 -> Informa se tem ou não animal
        // index 1 -> Informa a posicao escolhida(Point.class)
        // index 2 -> Informa a nova posicao do animal(Point.class)
        List<String> lista = gson.fromJson(parametro, ArrayList.class);

        Point pontoEscolhido = gson.fromJson(lista.get(1), Point.class);
        Flor florPlayer = player.removeFlower();
        IModelBoardTile boardTile = game.getBoardController().getBoardTile(pontoEscolhido);

        if (lista.get(0).contains("Animal")) {
            Animal animal = (Animal) game.getBoardController().getBoardTile(pontoEscolhido).getFolha().removerPecaDeFlor();
            Point newPositionAnimal = gson.fromJson(lista.get(2), Point.class);
            game.executeCommand(new MoveAnimalCommand(animal, game.getBoardController().getBoardTile(newPositionAnimal)));
        }

        game.executeCommand(new FlowerBoardCommand(florPlayer, boardTile));

        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        bottomPlayer.setPlayerStage(END);
        topPlayer.setPlayerStage(END);
        nextFase();
    }

    @Override
    public void nextFase() {
        fluxo.setFluxoState(new CheckBoard(FirstWind.class));
    }

    @Override

    public boolean faseEnd() {
        return super.faseEnd();
    }

    @Override
    public String getDescricaoLog() {
        return "Flora\u00e7\u00e3o";
    }

}
