package br.udesc.ceavi.ppr.haruichiban.control.observers;

/**
 *
 * @author Jeferson Penz
 */
public interface ServerStatusObserver {
    
    public void onClientRequest(String message);

    public void onError(String message);
    
}
