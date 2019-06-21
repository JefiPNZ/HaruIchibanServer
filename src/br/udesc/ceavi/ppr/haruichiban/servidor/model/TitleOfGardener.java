package br.udesc.ceavi.ppr.haruichiban.servidor.model;

/**
 *
 * @author Gustavo C
 */
public enum TitleOfGardener {
    
    SEM_TITULO(0, "Sem Titulo"),
    JUNIOR_TITULO(1, "Junior"),
    SENIOR_TITULO(2, "Senior"),
    SCREAM_LOSER(3, "Perdedor"),
    SCREAM_WINNER(4, "Vencedor"),
    SUPERME_GRADENER(10, "Jardineiro Supremo");
    
    private final int valor;
    private final String descricao;
    private TitleOfGardener(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return this.descricao;
    }

}
