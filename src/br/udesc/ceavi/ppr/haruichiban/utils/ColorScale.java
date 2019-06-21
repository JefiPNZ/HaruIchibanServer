package br.udesc.ceavi.ppr.haruichiban.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Classe responsável por alterar a escala de cores de uma imagem.
 * @author Jeferson Penz
 */
public class ColorScale {
    
    private float redValue;
    private float greenValue;
    private float blueValue;

    public ColorScale(int redValue, int greenValue, int blueValue) {
        this.redValue   = redValue / 255f;
        this.greenValue = greenValue / 255f;
        this.blueValue  = blueValue / 255f;
    }

    public ColorScale(float redValue, float greenValue, float blueValue) {
        this.redValue   = redValue;
        this.greenValue = greenValue;
        this.blueValue  = blueValue;
    }
    
    public ColorScale(Color values){
        this.redValue   = values.getRed()   / 255f;
        this.greenValue = values.getGreen() / 255f;
        this.blueValue  = values.getBlue()  / 255f;
    }

    /**
     * Aplica a escala de cor configurada na imagem e retorna o resultado.
     * @param source
     * @return A imagem convertida.
     * @throws java.io.IOException Caso não consiga ler o arquivo.
     */
    public BufferedImage convert(String source) throws IOException {
        BufferedImage img = Images.getImagem(source);
        return convert(img);
    }
    
    /**
     * Aplica a escala de cor configurada na imagem e retorna o resultado.
     * @param img
     * @return A imagem convertida.
     */
    public BufferedImage convert(BufferedImage img){
        BufferedImage res  = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        res.getGraphics().drawImage(img, 0, 0, null);
        int width  = res.getWidth();
        int height = res.getHeight();
        Color color;
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                color = new Color(res.getRGB(x, y), true);
                color = new Color(Math.min(color.getRed()   / 255f * redValue,   1)
                                 ,Math.min(color.getGreen() / 255f * greenValue, 1)
                                 ,Math.min(color.getBlue()  / 255f * blueValue,  1)
                                 ,color.getAlpha() / 255f);
                res.setRGB(x, y, color.getRGB());
            }
        }
        return res;
    }

    public float getRedValue() {
        return redValue;
    }

    public void setRedValue(float redValue) {
        this.redValue = redValue;
    }

    public float getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(float blueValue) {
        this.blueValue = blueValue;
    }

    public float getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(float greenValue) {
        this.greenValue = greenValue;
    }
    
}
