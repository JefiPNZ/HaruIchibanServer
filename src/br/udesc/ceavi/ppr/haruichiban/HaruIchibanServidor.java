package br.udesc.ceavi.ppr.haruichiban;

import java.net.ServerSocket;

import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.control.PlayerController;
import br.udesc.ceavi.ppr.haruichiban.control.TopPlayerVerificationRunnable;
import br.udesc.ceavi.ppr.haruichiban.model.GameConfig;
import br.udesc.ceavi.ppr.haruichiban.view.FrameConfig;
import br.udesc.ceavi.ppr.haruichiban.view.ServerFrame;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HaruIchibanServidor implements Runnable {

    private GameConfig config = null;

    public static void main(String[] args) {
        Thread mainThread = new Thread(new HaruIchibanServidor());
        mainThread.start();
        FrameConfig tela = new FrameConfig();
        tela.setVisible(true);
    }

    @Override
    public void run() {
        GameController.getInstance();
        GameController.getInstance().setServidor(this);
        while (this.config == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(HaruIchibanServidor.class.getName()).log(Level.SEVERE, null, ex);
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

    private void iniciaSocket(GameConfig gameConfig) throws IOException {
        GameController gcInstance = GameController.getInstance();
        try (ServerSocket listener = new ServerSocket(60000)) {
            gcInstance.notifyClientRequest("Aguardando Jogadores.");

            IPlayerController pTopo = new PlayerController(gameConfig.getColorTop(), gameConfig.getTamanhoDeck(), listener.accept(), "TOP");
            gcInstance.setTopPlayer(pTopo);
            Socket topPlayerSocket = gcInstance.getTopPlayer().getSocket();
            gcInstance.notifyClientRequest("Jogador superior entrou com endereço: " + topPlayerSocket.getRemoteSocketAddress());

            TopPlayerVerificationRunnable aguardando = new TopPlayerVerificationRunnable(pTopo);
            new Thread(aguardando).start();

            IPlayerController pBase = new PlayerController(gameConfig.getColorBotton(), gameConfig.getTamanhoDeck(), listener.accept(), "BOTTON");
            gcInstance.setBottomPlayer(pBase);
            Socket bottomPlayerSocket = gcInstance.getBottomPlayer().getSocket();
            gcInstance.notifyClientRequest("Jogador inferior entrou com endereço: " + bottomPlayerSocket.getRemoteSocketAddress());

            aguardando.termina();

            if (pTopo.isConnectado() && pBase.isConnectado()) {
                pBase.getCanal().sendResource("STRT", "Iniciar");
                pTopo.getCanal().sendResource("STRT", "Iniciar");
//                if(pTopo.aguardaPronto() && pBase.aguardaPronto()){
//                    System.out.println("Oiii");
                gcInstance.startGame();
//                    System.out.println("Ola");
//                }
                while (pBase.isConnectado() && pTopo.isConnectado() && this.config != null) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        this.config = null;
                        gcInstance.notifyClientRequest("");
                    }
                }
                if (!pBase.isConnectado()) {
                    gcInstance.notifyClientRequest("Jogador inferior desconectou...");
                } else {
                    gcInstance.notifyClientRequest("Jogador superior desconectou...");
                }
            }
        }
    }

}
