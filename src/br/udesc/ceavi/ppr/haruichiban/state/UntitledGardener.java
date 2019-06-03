package br.udesc.ceavi.ppr.haruichiban.state;

import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarUntitledGardenerException;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;

/**
 *
 * @author GustavoSantos
 * @since 03/05/2019
 *
 */
public class UntitledGardener implements TitleOfGardener {

    @Override
    public void becomeUntitledGardener(IPlayerController aThis) throws PlayNaoPodeSeTornarUntitledGardenerException {
        throw new PlayNaoPodeSeTornarUntitledGardenerException("Este usuario já é um UntitledGardener");
    }

    @Override
    public void becomeJuniorGardener(IPlayerController aThis) {
        aThis.setTitle(new JuniorGardener());
        aThis.notifyYouAJunior();
    }

    @Override
    public void becomeSeniorGardener(IPlayerController aThis) {
        aThis.setTitle(new SeniorGardener());
        aThis.notifyYouASenior();
    }

    @Override
    public void putFlowerTable(IPlayerController aThis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void firstWind(IPlayerController aThis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void newDarkLeaf(IPlayerController aThis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
