package br.udesc.ceavi.ppr.haruichiban.view;

import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 *
 * @author Jeferson Penz
 */
public class Principal extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private JComboBox<String> variante;
    private JComboBox<String> tamanho;
    private JColorChooser corSuperior;
    private JColorChooser corInferior;
    ServerFrame serverFrame;
    
    public static void main(String[] args) {
        new Principal();
    }
    
    public Principal() {
        super(GameServidorController.GAME_NAME);
        this.serverFrame = new ServerFrame();
        this.initComponentes();
        initializeFrameProperties();
    }
    
    public final void initializeFrameProperties() {
        this.setVisible(false);
        this.setSize(new Dimension(450, 400));
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    
    private void initComponentes() {
        JPanel content = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        content.setLayout(gbl);
        GridBagConstraints labelConstraints = new java.awt.GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.insets = new Insets(5, 0, 5, 5);
        GridBagConstraints componentConstraints = new java.awt.GridBagConstraints();
        componentConstraints.gridx = 1;
        componentConstraints.fill = GridBagConstraints.BOTH;
        componentConstraints.insets = new Insets(5, 5, 5, 0);
        GridBagConstraints fullConstraints = new java.awt.GridBagConstraints();
        fullConstraints.gridx = 0;
        fullConstraints.gridwidth = 2;
        content.add(new JLabel("Variante"), labelConstraints);
        this.variante = new JComboBox<>(new String[]{"Primavera", "Inverno"});
        content.add(this.variante, componentConstraints);
        content.add(new JLabel("Tamanho"), labelConstraints);
        this.tamanho = new JComboBox<>(new String[]{"Normal", "Giga"});
        content.add(this.tamanho, componentConstraints);
        content.add(new JLabel("Cor jogador superior."), fullConstraints);
        this.corSuperior = new JColorChooser(new Color(255, 210, 65));
        this.trataConfigColorChooser(this.corSuperior);
        content.add(this.corSuperior, fullConstraints);
        content.add(new JLabel("Cor jogador inferior."), fullConstraints);
        this.corInferior = new JColorChooser(new Color(255, 15, 35));
        this.trataConfigColorChooser(this.corInferior);
        content.add(this.corInferior, fullConstraints);
        JButton confirma = new JButton("Iniciar Servidor");
        confirma.addActionListener((ActionEvent e) -> {
            if (corSuperior.getColor().equals(corInferior.getColor())) {
                JOptionPane.showMessageDialog(this, "Os jogadores n\u00E3o podem ter a mesma cor.");
            } else {
                this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                GameServidorController.getInstance().begin((String) variante.getSelectedItem(), (String) tamanho.getSelectedItem(),
                        corSuperior.getColor(), corInferior.getColor());
                serverFrame.setVisible(true);
                GameServidorController.getInstance().startGame();
            }
        });
        content.add(confirma, fullConstraints);
        this.add(content);
    }
    
    private void trataConfigColorChooser(JColorChooser colorChooser) {
        colorChooser.setPreviewPanel(new JPanel());
        colorChooser.setPreferredSize(new Dimension(450, 130));
        AbstractColorChooserPanel[] Panels = colorChooser.getChooserPanels();
        for (AbstractColorChooserPanel panel : Panels) {
            if (!"DefaultSwatchChooserPanel".equals(panel.getClass().getSimpleName())) {
                colorChooser.removeChooserPanel(panel);
            }
        }
    }
    
}
