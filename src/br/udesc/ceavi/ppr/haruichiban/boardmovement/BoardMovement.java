package br.udesc.ceavi.ppr.haruichiban.boardmovement;

import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 13/05/2019
 *
 */
public interface BoardMovement {

    public boolean addPoint(Point positionBoard);

    public boolean addDiretion(Diretion deretion);

    public void execute();

    public boolean tableInteraction();

}
