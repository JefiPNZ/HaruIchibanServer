package br.udesc.ceavi.ppr.haruichiban.servidor.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.awt.Color;

/**
 * Representa a peca do jogo
 *
 * @author GustavoSantos
 * @since 09/05/2019
 *
 */
public abstract class PecaTabuleiro {

    protected float rotacao;
    private Color cor;

    public PecaTabuleiro(float rotacao, Color cor) {
        this.rotacao = rotacao;
        this.cor = cor;
    }

    public float getRotacao() {
        return rotacao;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    public abstract TipoPeca getTipo();

    public JsonObject toJson() {
        JsonObject toObject = new JsonObject();
        Gson gson = new Gson();
        toObject.add("cor", gson.toJsonTree(getCor()));
        toObject.addProperty("rotacao", rotacao);
        toObject.addProperty("name", getClass().getSimpleName());
        return toObject;
    }

    @Override
    public String toString() {
        return "{cor=" + this.getCor() + ", tipo=" + this.getTipo() + "}";
    }
}
