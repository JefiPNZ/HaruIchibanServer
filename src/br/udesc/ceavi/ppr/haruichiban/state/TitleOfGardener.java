package br.udesc.ceavi.ppr.haruichiban.state;

import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarJuniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarSeniorException;
import br.udesc.ceavi.ppr.haruichiban.exceptions.PlayNaoPodeSeTornarUntitledGardenerException;

/**
 *
 * @author GustavoSantos
 * @since 03/05/2019
 *
 */
public interface TitleOfGardener {

    public void becomeUntitledGardener(IPlayerController aThis) throws PlayNaoPodeSeTornarUntitledGardenerException;

    public void becomeJuniorGardener(IPlayerController aThis) throws PlayNaoPodeSeTornarJuniorException;

    public void becomeSeniorGardener(IPlayerController aThis) throws PlayNaoPodeSeTornarSeniorException;

    public void putFlowerTable(IPlayerController aThis);

    public void firstWind(IPlayerController aThis);

    public void newDarkLeaf(IPlayerController aThis);
}
