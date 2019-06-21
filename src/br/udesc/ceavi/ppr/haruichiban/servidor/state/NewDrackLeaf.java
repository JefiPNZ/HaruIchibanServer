package br.udesc.ceavi.ppr.haruichiban.servidor.state;

import br.udesc.ceavi.ppr.haruichiban.servidor.command.MoveAnimalCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.NewDrakLeafCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelGet;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.channel.ModelPost;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.TitleOfGardener;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.animais.Animal;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gustavo C
 */
public class NewDrackLeaf extends FluxoState {

    private IPlayerController jardineiroJunior;
    private IPlayerController jardineiroSenior;

    public NewDrackLeaf() {
        super();
    }

    @Override
    public void sistemCommand() {
        jardineiroJunior = game.getJardineiroJunior(TitleOfGardener.JUNIOR_TITULO);
        jardineiroSenior = game.getJardineiroJunior(TitleOfGardener.SENIOR_TITULO);
        playerCommandFluxoState();
    }

    @Override
    public void playerCommandFluxoState() {
        //Para Os Dois
        topPlayer.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "");
        topPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Novo Folha Escura");
        bottomPlayer.getPackageServerOutput().newPost(ModelPost.GAME_MUDANCA_ESTADO, "Novo Folha Escura");

        //Junior
        jardineiroJunior.getPackageServerOutput().newPost(ModelPost.PLAYER_NOTIFICAO_SIMPLER, "Aguarde o Jogador Senior Definir a Nova Folha Escura");
        //Senior
        jardineiroSenior.getPackageServerOutput().newGet(ModelGet.GAME_FASE, NEW_DRACK_LEFT);
    }

    @Override
    public void playerInformeFluxoStateEnd(IPlayerController player, String parametroFase) {
        List<String> parametros = gson.fromJson(parametroFase, ArrayList.class);
        //Tratamento de jogada
        // index 0 -> Informa se tem ou não animal
        // index 1 -> Informa a posicao escolhida(Point.class)
        // index 2 -> Informa a nova posicao do animal(Point.class)
        Point newDarkLeaf = gson.fromJson(parametros.get(1), Point.class);

        if (parametros.get(0).contains("Animal")) {
            Animal animal = (Animal) boardController.getBoardTile(newDarkLeaf).getFolha().removerPecaDeFlor();
            Point newPositionAnimal = gson.fromJson(parametros.get(2), Point.class);
            game.executeCommand(new MoveAnimalCommand(animal, boardController.getBoardTile(newPositionAnimal)));
        }

        game.executeCommand(new NewDrakLeafCommand(newDarkLeaf));
        topPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        bottomPlayer.getPackageServerOutput().newGet(ModelGet.GAME_BOARD, boardController.getTabuleiroProxy());
        nextFase();
    }

    @Override
    public void nextFase() {
        fluxo.setFluxoState(new ChooseFlower());
    }

    @Override
    public boolean faseEnd() {
        return super.faseEnd();
    }

}
