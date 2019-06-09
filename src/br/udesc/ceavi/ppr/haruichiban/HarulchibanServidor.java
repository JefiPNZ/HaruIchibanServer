package br.udesc.ceavi.ppr.haruichiban;

import java.net.ServerSocket;

import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.PlayerController;
import br.udesc.ceavi.ppr.haruichiban.control.TopPlayerVerificationRunnable;
import br.udesc.ceavi.ppr.haruichiban.model.GameConfig;
import br.udesc.ceavi.ppr.haruichiban.view.FrameConfig;
import br.udesc.ceavi.ppr.haruichiban.view.ServerFrame;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class HarulchibanServidor implements Runnable{
    
    private GameConfig config = null;

    public static void main(String[] args) {
        Thread mainThread = new Thread(new HarulchibanServidor());
        mainThread.start();
        FrameConfig tela = new FrameConfig();
        tela.setVisible(true);
    }

    @Override
    public void run() {
        GameController.getInstance();
        GameController.getInstance().setServidor(this);
        while(this.config == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(HarulchibanServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ServerFrame frame = new ServerFrame();
        frame.setVisible(true);
        try {
            this.iniciaSocket(this.config);
        } catch (IOException ex) {
            GameController.getInstance().notifyError("Erro na criação dos sockets.");
            System.exit(0);
        }
    }

    public synchronized void startServidor(GameConfig gameConfig) {
        this.config = gameConfig;
    }
    
    private void iniciaSocket(GameConfig gameConfig) throws IOException{
        GameController gcInstance = GameController.getInstance();
        try (ServerSocket listener = new ServerSocket(60000)) {
            boolean b = true;
            gcInstance.notifyClientRequest("Aguardando Jogadores.");
            PlayerController pTopo = new PlayerController(gameConfig.getColorTop(), gameConfig.getTamanho());
            gcInstance.setTopPlayer(pTopo);
            Socket topPlayerSocket = listener.accept();
            pTopo.setSocket(topPlayerSocket, "TOP");
            gcInstance.notifyClientRequest("Jogador superior entrou com endereço: " + topPlayerSocket.getRemoteSocketAddress());
            
            TopPlayerVerificationRunnable aguardando = new TopPlayerVerificationRunnable(pTopo);
            new Thread(aguardando).start();
            PlayerController pBase = new PlayerController(gameConfig.getColorBotton(), gameConfig.getTamanho());
            gcInstance.setBottomPlayer(pBase);
            Socket bottomPlayerSocket = listener.accept();
            pBase.setSocket(bottomPlayerSocket, "BOTTON");
            gcInstance.notifyClientRequest("Jogador superior entrou com endereço: " + bottomPlayerSocket.getRemoteSocketAddress());
            aguardando.termina();
            
            if(pTopo.isConnectado() && pBase.isConnectado()){
                pTopo.sendResource("STRT");
                pBase.sendResource("STRT");
                if(pTopo.aguardaPronto() && pBase.aguardaPronto()){
                    gcInstance.startGame();
                }
                while (pBase.isConnectado() && pTopo.isConnectado() && this.config != null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        this.config = null;
                        gcInstance.notifyClientRequest("");
                    }
                }
            }
            if(!pBase.isConnectado()){
                gcInstance.notifyClientRequest("Jogador inferior desconectou...");
            }
            else {
                gcInstance.notifyClientRequest("Jogador superior desconectou...");
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Erro de Socket: " + ex.toString());
        }
    }
}
