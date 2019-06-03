package br.udesc.ceavi.ppr.haruichiban.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Classe com a Lista de Imagens (por conveniência).
 *
 * @author Jeferson Penz
 */
public class Images {

    public static Map<String, BufferedImage> imagems = new HashMap<String, BufferedImage>();
    //Geral
    public static final String TABULEIRO_AGUA = "img/Agua.jpg";             // https://www.deviantart.com/berserkitty/art/Seamless-Cartoon-styled-Water-Texture-743787929
    public static final String ANIMAL_PLATAFORMA = "img/AnimalPlataforma.png"; // Extraida do PDF
    public static final String JOGADOR_BASE = "img/Base.png";             // Extraida do PDF
    public static final String PECA_TABULEIRO = "img/BlocoTabuleiro.png";   // Extraida do PDF
    public static final String PONTUACAO_FUNDO = "img/Pontos.png";           // Extraida do PDF
    public static final String JOGADOR_ROSTO = "img/Rosto.png";            // Extraida do PDF
    public static final String JOGADOR_ROUPA = "img/Roupa.png";            // Extraida do PDF
    public static final String JOGADOR_TABUA = "img/Tabua.png";            // Extraida do PDF
    //Primavera
    public static final String JOGADOR_FLOR_PRIM = "img/FlorPrimavera.png";    // Extraida do PDF
    public static final String OVO_SAPO = "img/OvoSapo.png";          // Manual
    public static final String ANIMAL_SAPO = "img/Sapo.png";             // Extraida do PDF
    public static final String VITORIA_REGIA = "img/VitoriaRegia.png";     // Extraida do PDF
    //Inverno
    public static final String JOGADOR_FLOR_INV = "img/FlorInverno.png";      // Manual
    public static final String FOLHA_SECA = "img/FolhaSeca.png";        // Manual
    public static final String OVO_PINGUIM = "img/OvoPinguim.png";       // Manual
    public static final String ANIMAL_PINGUIM = "img/Pinguim.png";          // Manual

    public static void mapImagemPrimaveira() {
        try {
            initGeral();
            imagems.put(JOGADOR_FLOR_PRIM, ImageIO.read(new File(Images.JOGADOR_FLOR_PRIM)));
            imagems.put(OVO_SAPO, ImageIO.read(new File(Images.OVO_SAPO)));
            imagems.put(ANIMAL_SAPO, ImageIO.read(new File(Images.ANIMAL_SAPO)));
            imagems.put(VITORIA_REGIA, ImageIO.read(new File(Images.VITORIA_REGIA)));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    public static void mapImagemInverno() {
        try {
            initGeral();
            imagems.put(JOGADOR_FLOR_INV, ImageIO.read(new File(Images.JOGADOR_FLOR_INV)));
            imagems.put(FOLHA_SECA, ImageIO.read(new File(Images.FOLHA_SECA)));
            imagems.put(OVO_PINGUIM, ImageIO.read(new File(Images.OVO_PINGUIM)));
            imagems.put(ANIMAL_PINGUIM, ImageIO.read(new File(Images.ANIMAL_PINGUIM)));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private static void initGeral() throws IOException {
        imagems.put(TABULEIRO_AGUA, ImageIO.read(new File(Images.TABULEIRO_AGUA)));
        imagems.put(ANIMAL_PLATAFORMA, ImageIO.read(new File(Images.ANIMAL_PLATAFORMA)));
        imagems.put(JOGADOR_BASE, ImageIO.read(new File(Images.JOGADOR_BASE)));
        imagems.put(PECA_TABULEIRO, ImageIO.read(new File(Images.PECA_TABULEIRO)));
        imagems.put(PONTUACAO_FUNDO, ImageIO.read(new File(Images.PONTUACAO_FUNDO)));
        imagems.put(JOGADOR_ROSTO, ImageIO.read(new File(Images.JOGADOR_ROSTO)));
        imagems.put(JOGADOR_ROUPA, ImageIO.read(new File(Images.JOGADOR_ROUPA)));
        imagems.put(JOGADOR_TABUA, ImageIO.read(new File(Images.JOGADOR_TABUA)));
    }

    public static BufferedImage getImagem(String imagem) {
        return imagems.get(imagem);
    }
}
