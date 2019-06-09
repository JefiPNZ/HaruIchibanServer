package br.udesc.ceavi.ppr.haruichiban.control;

import br.udesc.ceavi.ppr.haruichiban.model.GameConfig;
import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;

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
        gameController().notifyClientRequest(requisicao);
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
                gameController().notifyError("Comando Perdido : " + requisicao);
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
                sendResource("" + player.getPileSize(), requisicao);
                return;
            case "ProduceDeck":
                player.initDeck();
                sendResource("DeckProduzido", requisicao);
                return;
            default:
                sendResource("Request-Perdida", requisicao);
                gameController().notifyError("Comando Jogador Perdido : " + comando);
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
                int pileSize = oponnet().getPileSize();
                System.out.println("PileSize OP" + pileSize);
                sendResource("" + pileSize, requisicao);
                return;
            default:
                sendResource("Request-Perdida", requisicao);
                gameController().notifyError("Comando Oponnet Perdido : " + comando);
        }
    }

    private void comandoGame(String comando) {
        String requisicao = comando.split(",")[1];
        switch (requisicao) {
            case "HaveOponent":
                if (isTopPlayer() && gameController().getBottomPlayer() == null
                        || isBottonPlayer() && gameController().getTopPlayer() == null) {
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

            default:
                sendResource("Request-Perdida", requisicao);
                gameController().notifyError("Comando Game Perdido : " + comando);
        }

    }

    private IPlayerController player() {
        return player;
    }

    private IPlayerController oponnet() {
        return isTopPlayer() ? gameController().getBottomPlayer() : gameController().getTopPlayer();
    }

    private boolean isTopPlayer() {
        return gameController().getTopPlayer() != null && gameController().getTopPlayer().equals(player);
    }

    private boolean isBottonPlayer() {
        return gameController().getBottomPlayer() != null && gameController().getBottomPlayer().equals(player);
    }

    private GameController gameController() {
        return GameController.getInstance();
    }

    public void sendResource(String resposta, String requisicao) {
        if (resposta.contains("Request-Perdida")) {
            gameController().notifyError("Request-Perdida : " + requisicao);
        } else {
            clienteAnswer.println(resposta);
            clienteAnswer.flush();
            gameController().notifyServerRespond("Requisicao Respondida : " + requisicao);
        }
    }

}
