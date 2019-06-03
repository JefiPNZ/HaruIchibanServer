package br.udesc.ceavi.ppr.haruichiban.command;

import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarJuniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarSeniorException;

/**
 *
 * @author Gustavo C Santos
 * @since 14/05/2019
 *
 */
public class DefineTitleCommand implements Command {

    private IPlayerController junior;
    private IPlayerController senior;

    public DefineTitleCommand(IPlayerController junior, IPlayerController senior) {
        this.junior = junior;
        this.senior = senior;
    }

    @Override
    public void execute() {
        try {
            junior.becomeJuniorGardener();
            senior.becomeSeniorGardener();
        } catch (PlayNaoPodeSeTornarJuniorException | PlayNaoPodeSeTornarSeniorException ex) {
        }
    }

}
