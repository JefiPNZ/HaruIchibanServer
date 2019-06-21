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
public enum ModelPost {
    MY_CHOOSEFLOWER,
    GAME_ENDGAME,
    FASE_END,
    GAME_MUDANCA_ESTADO,
    PLAYER_NOTIFICAO_SIMPLER,
    PLAYER_NOTIFICAO_TEMPO,
    OPPONENT_DISCOUNTED,
    GAME_WINEW,
    BECAME_SENIOR,
    BECAME_JUNIOR,
    BECAME_NOT_TITLE,
    SCREAM_WINNER,
    SCREAM_LOSER,
    LOGADO_COM_SUCESSO,
    CLOSE_GAME,
    DESCONTANDO;

}
