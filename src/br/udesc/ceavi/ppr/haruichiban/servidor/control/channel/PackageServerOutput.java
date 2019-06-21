package br.udesc.ceavi.ppr.haruichiban.servidor.control.channel;

import java.io.PrintWriter;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Saida
 *
 * @author Gustavo C
 */
public class PackageServerOutput implements Runnable {

    private final BlockingDeque<String> saidas;
    private final PrintWriter saida;
    private final CommunicationPackage cPackage;
    private String nome;

    public PackageServerOutput(PrintWriter saida, String nome) {
        this.saida = saida;
        saidas = new LinkedBlockingDeque<>();
        cPackage = new CommunicationPackage();
        this.nome = nome;
    }

    public synchronized void newGet(ModelGet get, String parametro) {
        cPackage.addGet(get, parametro);
        saidas.add(cPackage.toJson());
    }

    public synchronized void newPost(ModelPost post, String parametro) {
        cPackage.addPost(post, parametro);
        saidas.add(cPackage.toJson());
    }

    public synchronized void newGet(ModelGet get, Object parametro) {
        cPackage.addGet(get, parametro);
        saidas.add(cPackage.toJson());
    }

    public synchronized void newPost(ModelPost post, Object parametro) {
        cPackage.addPost(post, parametro);
        saidas.add(cPackage.toJson());
    }

    @Override
    public void run() {
        try {
            String cPacket = null;
            while ((cPacket = saidas.take()) != null) {
                saida.println(cPacket);
                saida.flush();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
