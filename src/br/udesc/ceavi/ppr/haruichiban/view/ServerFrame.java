package br.udesc.ceavi.ppr.haruichiban.view;

import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.PlayerController;
import br.udesc.ceavi.ppr.haruichiban.control.observers.ServerStatusObserver;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringJoiner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Jeferson Penz
 */
public class ServerFrame extends JFrame implements ServerStatusObserver {

    private List<String> mensagens;
    private JLabel logMensagens;
    private JScrollPane pane;

    public ServerFrame() {
        super("Haru Ichiban (Server)");
        this.mensagens = new ArrayList<>();
        this.initFrame();
        GameController.getInstance().addServerStatusObserver(this);
    }

    private void initFrame() {
        this.setVisible(false);
        this.setSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.logMensagens = new JLabel();
        this.logMensagens.setVerticalTextPosition(JLabel.TOP);
        this.pane = new JScrollPane(this.logMensagens);
        this.pane.getViewport().setBackground(Color.white);
        this.pane.setBorder(null);
        this.pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(pane, BorderLayout.CENTER);
    }

    @Override
    public void onClientRequest(String message) {
        imprime("<p color='#2E8B57'>" + message + "</p>");
    }
    
    private void trataMensagens(){
        StringJoiner joiner = new StringJoiner("<br/>");
        for (ListIterator<String> iterador = this.mensagens.listIterator(0); iterador.hasNext();) {
            joiner.add(iterador.next());
        }

        this.logMensagens.setText("<html>" + joiner.toString() + "</html>");
        this.pane.getVerticalScrollBar().setValue(this.pane.getVerticalScrollBar().getMaximum());
    }

    private void imprime(String message) {
        this.mensagens.add(message);
        this.trataMensagens();
    }

    @Override
    public void onError(String message) {
        imprime("<p color='#CD3333'>" + message + "</p>");
    }

    @Override
    public void onServerRespond(String resource) {
        imprime("<p color='#1E90FF'>" + resource + "</p>");
    }

}
