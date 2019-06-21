package br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces;

import br.udesc.ceavi.ppr.haruichiban.servidor.state.FluxoState;

/**
 *
 * @author Gustavo C Santos
 * @since 12/05/2019
 *
 */
public interface IFluxoController {

    public void startGame();

    public void setFluxoState(FluxoState state);

    public void playerCommandFluxoState();
    
    public FluxoState getFluxoState();

    public void turnoExtra();

    public boolean isTurnoExtra();

    public void novaRodada();

    public int getRodada();

    public void resertRodadas();

    public void playerInformeFluxoStateEnd(IPlayerController player, String parametroes);
}
