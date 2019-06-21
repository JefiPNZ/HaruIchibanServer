package br.udesc.ceavi.ppr.haruichiban.servidor.model;

import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.BoardTileScoreVisitor;
import br.udesc.ceavi.ppr.haruichiban.servidor.visitor.TipoPontuacao;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma célula no tabuleiro.
 *
 * @author Jeferson Penz
 */
public class SimpleModelBoardTile implements IModelBoardTile{

    private Folha folha;
    private final int rotation;
    private List<TipoPontuacao> pontuado;

    /**
     * Cria uma nova peça para o tabuleiro.
     */
    public SimpleModelBoardTile() {
        this.rotation = GameServidorController.getInstance().getRandomizer().nextInt(360);
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

    public JsonObject toJson() {
        JsonObject modelBoardJson = new JsonObject();
        Gson gson = new Gson();

        //Folha
        if (hasFolha()) {
            modelBoardJson.add("folhaCor", gson.toJsonTree(folha.getCor()));
            modelBoardJson.addProperty("folharRotacao", folha.getRotacao());
            modelBoardJson.addProperty("folhaNome", folha.getClass().getSimpleName());
            modelBoardJson.addProperty("isEscura", folha.isEscura());
            
            //Filhote
            if (folha.hasFilhote()) {
                modelBoardJson.add("corFilhote", gson.toJsonTree(folha.getFilhote().getCor()));
                modelBoardJson.addProperty("filhoteName", folha.getFilhote().getClass().getSimpleName());
            }

            //Verifica se essa tem um animal ou uma flor
            if (folha.hasAnimal() || (folha.hasPeca() && folha.getPeca().getTipo() == TipoPeca.FLOR)) {
                JsonObject modelPecaJson = new JsonObject();
                modelPecaJson.add("pecaCor", gson.toJsonTree(folha.getPeca().getCor()));
                modelPecaJson.addProperty("pecaRotacao", folha.getPeca().getRotacao());
                modelPecaJson.addProperty("pecaName", folha.getPeca().getClass().getSimpleName());
                modelBoardJson.add("peca", modelPecaJson);
            }
        }
        
        return modelBoardJson;
    }
    
    @Override
    public void accept(BoardTileScoreVisitor visitor){
        visitor.visit(this);
    }

    @Override
    public SimpleModelBoardTile getProximoTileHorizontal() {
        return null;
    }

    @Override
    public SimpleModelBoardTile getProximoTileVertical() {
        return null;
    }

    @Override
    public SimpleModelBoardTile getProximoTileDiagonal() {
        return null;
    }

    @Override
    public void limpaPontuado() {
        this.pontuado = new ArrayList<>();
    }

    @Override
    public void setPontuado(TipoPontuacao direcao) {
        if(!this.isPontuado(direcao)){
            this.pontuado.add(direcao);
        }
    }

    @Override
    public boolean isPontuado(TipoPontuacao direcao) {
        return this.pontuado.contains(direcao);
    }
}
