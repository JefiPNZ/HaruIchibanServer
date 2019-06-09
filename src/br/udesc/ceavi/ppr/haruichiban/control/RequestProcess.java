package br.udesc.ceavi.ppr.haruichiban.control;

import br.udesc.ceavi.ppr.haruichiban.model.GameConfig;
import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import java.awt.Point;

public class RequestProcess {

    private Scanner clienteRequest;
    private PrintWriter clienteAnswer;
    private IPlayerController player;
    private Gson gson;

    public RequestProcess(Scanner clienteRequest, PrintWriter clienteAnswer, IPlayerController player) {
        super();
        this.clienteRequest = clienteRequest;
        this.clienteAnswer = clienteAnswer;
        this.player = player;
        this.gson = new Gson();
    }

    public void processar() {
        String requisicao = clienteRequest.nextLine();
        gameController().notifyClientRequest(player.getSocket().getPort() + " - " + requisicao);
        switch (requisicao.split(",")[0]) {
            case "MY":// Requisicoes para player
                comandoJogador(requisicao);
                break;
            case "OP"://// Requisicoes para DadosOponnets
                comandoOponnet(requisicao);
                break;
            case "GAME"://// Requisicao para Game
                comandoGame(requisicao);
                break;
            default:
                sendResource("Request-Perdida", requisicao.split(",")[1]);
                gameController().notifyError(player.getSocket().getPort() + " - " + "Comando Perdido : " + requisicao);
                break;
        }
    }

    private void comandoJogador(String comando) {
        String requisicao = comando.split(",")[1];
        switch (requisicao) {
            case "Color":
                sendResource(new Gson().toJson(player.getColor()), requisicao);
                return;

            case "Position":
                sendResource(isTopPlayer() ? "TOP" : "BOTTON", requisicao);
                return;

            case "Hand":
                StringBuilder sb = new StringBuilder();
                String resposta = "";

                if (!player.getHand().isEmpty()) {
                    player.getHand().forEach(valor -> sb.append(valor).append(','));
                    resposta = sb.toString().substring(0, sb.length() - 1);
                } else {
                    resposta = "";
                }
                sendResource(resposta, requisicao);
                return;

            case "PileSize":
                sendResource(player.getPileSize(), requisicao);
                return;

            case "ProduceDeck":
                player.initDeck();
                sendResource("DeckProduzido", requisicao);
                return;

            default:
                sendResource("Request-Perdida", requisicao);
                gameController().notifyError(player.getSocket().getPort() + " - " + "Comando Jogador Perdido : " + comando);
        }

    }

    private void comandoOponnet(String comando) {
        String requisicao = comando.split(",")[1];
        switch (requisicao) {
            case "Color":
                sendResource(new Gson().toJson(oponnet().getColor()), requisicao);
                return;

            case "Position":
                sendResource(isTopPlayer() ? "TOP" : "BOTTON", requisicao);
                return;

            case "Hand":
                StringBuilder sb = new StringBuilder();
                String resposta = "";

                if (!oponnet().getHand().isEmpty()) {
                    oponnet().getHand().forEach(valor -> sb.append("").append(','));
                    resposta = sb.toString().substring(0, sb.length() - 1);
                } else {
                    resposta = "";
                }
                sendResource(resposta, requisicao);
                return;

            case "PileSize":
                sendResource(oponnet().getPileSize(), requisicao);
                return;

            default:
                sendResource("Request-Perdida", requisicao);
                gameController().notifyError(player.getSocket().getPort() + " - " + "Comando Oponnet Perdido : " + comando);
        }
    }

    private void comandoGame(String comando) {
        String requisicao = comando.split(",")[1];
        switch (requisicao) {

            case "HaveOponent":
                if (isTopPlayer() && gameController().getBottomPlayer() == null
                        || !isTopPlayer() && gameController().getTopPlayer() == null) {
                    sendResource("false", requisicao);
                } else {
                    sendResource("true", comando);
                }
                return;

            case "GameConfig":
                GameConfig gameConfig = gameController().getGameConfig();
                String resouse = gson.toJson(gameConfig);
                sendResource(resouse, requisicao);
                return;

            case "JogoContinua":
                sendResource("true", requisicao);
                return;

            case "BordHeight":
                sendResource(gameController().getBoardController().getAlturaTabuleiro(), comando);
                return;

            case "BordWidth":
                sendResource(gameController().getBoardController().getLarguraTabuleiro(), comando);
                return;

            case "Estacao":
                sendResource(gameController().getGameConfig().getEstacao(), comando);
                return;
            case "BoardTile":
                String pontos = comando.split(":")[1];

                String x = pontos.split(",")[0];
                String y = pontos.split(",")[1];
                sendResource(gameController()
                        .getBoardController()
                        .getBoardTile(
                                new Point(
                                        Integer.parseInt(x),
                                        Integer.parseInt(y)
                                ))
                        .toJson(), comando);
                return;
            case "ENDGAME":
                sendResource("Oi", comando);
                return;
            default:
                sendResource("Request-Perdida", requisicao);
                gameController().notifyError(player.getSocket().getPort() + " - " + "Comando Game Perdido : " + comando);
        }

    }

    private IPlayerController player() {
        return player;
    }

    private IPlayerController oponnet() {
        return isTopPlayer() ? gameController().getBottomPlayer() : gameController().getTopPlayer();
    }

    private boolean isTopPlayer() {
        return player.isTop();
    }

    private GameController gameController() {
        return GameController.getInstance();
    }

    public void sendResource(String resposta, String requisicao) {
        clienteAnswer.println(resposta);
        clienteAnswer.flush();
        gameController().notifyServerRespond(player.getSocket().getPort() + " - " + "Requisicao Respondida : " + requisicao);

    }

    public void sendResource(int resposta, String requisicao) {
        clienteAnswer.println(resposta);
        clienteAnswer.flush();
        gameController().notifyServerRespond(player.getSocket().getPort() + " - " + "Requisicao Respondida : " + requisicao);
    }

}
