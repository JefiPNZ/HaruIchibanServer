package br.udesc.ceavi.ppr.haruichiban.control;

/**
 *
 * @author Jeferson Penz
 */
public class TopPlayerVerificationRunnable implements Runnable{
    private boolean bOk;
    private PlayerController pTopo;

    public TopPlayerVerificationRunnable(PlayerController pTopo) {
        bOk = false;
        this.pTopo = pTopo;
    }
    
    @Override
    public void run() {
        while(!bOk){
            if(!pTopo.isConnectado()){
                GameController.getInstance().notifyClientRequest("Jogador superior desconectou...");
                this.termina();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
    }
    public void termina(){
        bOk = true;
    }
}
