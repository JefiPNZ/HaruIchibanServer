package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class ChooseFlowerPlayer implements Command {

    private IPlayerController player;
    private int escolhida;

    public ChooseFlowerPlayer(IPlayerController player, int escolhida) {
        this.player = player;
        this.escolhida = escolhida;
    }

    @Override
    public void execute() {
        player.getFlorFromHand(escolhida);
    }
}
