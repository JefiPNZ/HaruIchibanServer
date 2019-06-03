package br.udesc.ceavi.ppr.haruichiban.control.observers;

/**
 *
 * @author Jeferson Penz
 */
public interface GameStateObserver {
    
    public void notificaMudancaEstado(String mensagem);
    
    public void notificaFimJogo(String mensagem);
    
}
