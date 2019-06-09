package br.udesc.ceavi.ppr.haruichiban.control.observers;

import br.udesc.ceavi.ppr.haruichiban.control.PlayerController;

/**
 *
 * @author Jeferson Penz
 */
public interface ServerStatusObserver {
    
    public void onClientRequest(String message);

    public void onError(String message);

    public void onServerRespond(String resource);
    
}
