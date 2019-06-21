package br.udesc.ceavi.ppr.haruichiban.servidor.control.channel;

/**
 * <p>
 * Os Prefixo MY,OP,GAME são uma padronizacao feita!
 * <p>
 * Seus usos são:
 * <li>MY faz referencia um comando do player</li>
 * <li>OP faz referencia um comando do Oponente do player</li>
 * <li>GAME faz referencia a um comando do Sistema</li>
 * <p>
 * O raiz(o que se encontra depois da ,)é o comando
 *
 * @author Gustavo C
 */
public enum ModelGet {
    //Player
    MY_COLOR,
    MY_PILESIZE,
    MY_POSITION,
    MY_HAND,
    MY_POINTS,
    //Oponnet
    OPONNET_COLOR,
    OPONNET_PILESIZE,
    OPONNET_POSITION,
    OPONNET_HAND,
    OPONNET_POINTS,
    //Game
    GAME_ESTACAO,
    GAME_BOARD,
    GAME_FASE;

}
