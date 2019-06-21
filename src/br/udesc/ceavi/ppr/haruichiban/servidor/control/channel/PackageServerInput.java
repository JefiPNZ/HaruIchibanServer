package br.udesc.ceavi.ppr.haruichiban.servidor.control.channel;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IPlayerController;
import com.google.gson.Gson;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entrada
 *
 * @author Gustavo C
 */
public class PackageServerInput implements Runnable {

    private BlockingQueue<CommunicationPackage> entradas;
    private Scanner entrada;
    private PackageServerConsumer packetConsumer;
    private Gson gson;
    private String nome;

    public PackageServerInput(Scanner scanner, IPlayerController player, IPlayerController oponnet, String nome) {
        this.entradas = new LinkedBlockingDeque<>();
        this.entrada = scanner;
        this.gson = new Gson();
        this.packetConsumer = new PackageServerConsumer(entradas, player, oponnet);
        GameServidorController.getInstance().threadPoolExecute(packetConsumer);
        this.nome = nome;
    }

    public synchronized void add(String cPackage) {
        try {
            CommunicationPackage newPackege = gson.fromJson(cPackage, CommunicationPackage.class);
            entradas.put(newPackege);
        } catch (InterruptedException ex) {
            Logger.getLogger(PackageServerInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (entrada.hasNextLine()) {
            add(entrada.nextLine());
        }
    }

}
