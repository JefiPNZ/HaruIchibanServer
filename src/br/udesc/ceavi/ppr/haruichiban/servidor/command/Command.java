package br.udesc.ceavi.ppr.haruichiban.servidor.command;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.Loggable;

/**
 *
 * @author GustavoSantos
 * @since 03/05/2019
 * 
 */
public interface Command extends Loggable{

    public void execute();
}
