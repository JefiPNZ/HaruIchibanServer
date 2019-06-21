package br.udesc.ceavi.ppr.haruichiban.servidor.model;

/**
 *
 * @author Jeferson Penz
 */
public enum TipoPeca {
    FOLHA("Folha"), FLOR("Flor"), ANIMAL("Animal"), FILHOTE("Filhote");
    
    private String tipo;
    private TipoPeca(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return this.tipo;
    }
}
