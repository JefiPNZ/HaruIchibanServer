package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class DefineTitleEmpateCommand implements Command {

    private IPlayerController topPlayer;
    private IPlayerController bottomPlayer;

    public DefineTitleEmpateCommand(IPlayerController topPlayer, IPlayerController bottomPlayer) {
        this.topPlayer = topPlayer;
        this.bottomPlayer = bottomPlayer;
    }
    
    @Override
    public void execute() {
        bottomPlayer.devolverFlorAoDeck();
        topPlayer.devolverFlorAoDeck();
    }

}
