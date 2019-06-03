package br.udesc.ceavi.ppr.haruichiban.control;

/**
 *
 * @author Gustavo C Santos
 * @since 12/05/2019
 *
 */
public interface IFluxoController {

    public void startGame();

    public void chooseFlower();

    public void putFlowerTable();

    public void firstWind();

    public void newDarkLeaf();

    public void getPlayerPoints();

    /**
     * Retorna ao controller o estado da fase em que este se encontra
     *
     * @return Fase.PUT_FLOWER_TABLE
     */
    public Fase chooseFlowerEnd();

    /**
     * Retorna ao controller o estado da fase em que este se encontra
     *
     * @return Fase.FRIST_WINT
     */
    public Fase putFlowerTableEnd();

    /**
     * Retorna ao controller o estado da fase em que este se encontra
     *
     * @return NEW_DARK_LEAF
     */
    public Fase firstWindEnd();

    /**
     * Retorna ao controller o estado da fase em que este se encontra
     *
     * @return GET_PONTS
     */
    public Fase newDarkLeafEnd();

	public boolean hasPlayer();

}
