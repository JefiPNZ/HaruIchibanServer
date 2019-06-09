package br.udesc.ceavi.ppr.haruichiban.model;

import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.awt.Color;

/**
 * Representa uma célula no tabuleiro.
 *
 * @author Jeferson Penz
 */
public class ModelBoardTile {

    private Folha folha;

    /**
     * Cria uma nova peça para o tabuleiro.
     */
    public ModelBoardTile() {
        this.folha = null;
    }

    /**
     * Adiciona uma folha para a célula.
     *
     * @param folha
     */
    public void addFolha(Folha folha) {
        this.folha = folha;
    }

    /**
     * Remove a folha da peça.
     */
    public Folha removeFolha() {
        Folha retono = folha;
        this.folha = null;
        return retono;
    }

    /**
     * Retorna se tem ou não uma folha
     *
     * @return true tem, false não tem
     */
    public boolean hasFolha() {
        return this.folha != null;
    }

    public Folha getFolha() {
        return this.folha;
    }

    Gson gson = new Gson();
    
    public String toJson() {
        JsonObject criando = new JsonObject();
        
        if (hasFolha()) {
            criando.add("corDaFolha", gson.toJsonTree(folha.getCor()));
            criando.addProperty("folharRotacao", folha.getRotacao());
            criando.addProperty("folhaNome", folha.getClass().getSimpleName());
        
            if (folha.hasFilhote()) {
                criando.add("corDoFilhote", gson.toJsonTree(folha.getFilhote().getCor()));
                criando.addProperty("filhoteName", folha.getFilhote().getClass().getSimpleName());
            }

            //Verifica se essa tem uma peca
            if (folha.hasPeca()) {
                criando.add("corDaPeca", gson.toJsonTree(folha.getPeca().getCor()));
                criando.addProperty("pecaRotacao", folha.getPeca().getRotacao());
                criando.addProperty("pecaName", folha.getPeca().getClass().getSimpleName());
            }
        }
        return criando.toString();
    }
}
