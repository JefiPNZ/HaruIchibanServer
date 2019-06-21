package br.udesc.ceavi.ppr.haruichiban.servidor.model;

/**
 *
 * @author Gustavo C
 */
public enum TitleOfGardener {
    
    SEM_TITULO(0),
    JUNIOR_TITULO(1),
    SENIOR_TITULO(2),
    SCREAM_LOSER(3),
    SCREAM_WINNER(4),
    SUPERME_GRADENER(10);
    
    private final int valor;

    private TitleOfGardener(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "TitleOfGardener{"  + getClass().getSimpleName() + " - " + getValor() + '}';
    }

}
