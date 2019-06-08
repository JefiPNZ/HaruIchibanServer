package br.udesc.ceavi.ppr.haruichiban.control;

import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;

public class RequestProcess {

    private Scanner clienteRequest;
    private PrintWriter clienteAnswer;
    private IPlayerController player;

    public RequestProcess(Scanner clienteRequest, PrintWriter clienteAnswer, IPlayerController player) {
        super();
        this.clienteRequest = clienteRequest;
        this.clienteAnswer = clienteAnswer;
        this.player = player;
    }

    public void processar() {
        String requisicao = clienteRequest.nextLine();
        switch (requisicao.split(",")[0]) {
            case "I":// Requisi�oes para player
                comandoInterno(requisicao);
                break;
            case "E"://// Requisi�oes para Servidor
                comandoExterno(requisicao);
                break;
            case "END"://// Requisicao Para Fechar o Jogo
                // jogador = false;
                break;
            default:
                System.out.println("Comando Perdido : " + requisicao);
                break;
        }
    }

    private void comandoInterno(String requisicao) {
        switch (requisicao.split(",")[1]) {
            case "ColorRequest":
                sendResource(new Gson().toJson(player.getColor()));
                return;
            case "PositionRequest":
                sendResource(isTopPlayer() ? "TOP" : "BOTTON");
                return;
            case "HandRequest":
                StringBuilder sb = new StringBuilder();
                String resposta = "";

                if (!player.getHand().isEmpty()) {
                    player.getHand().forEach(valor -> sb.append(valor).append(','));
                    resposta = sb.toString().substring(0, sb.length() - 1);
                } else {
                    resposta = "";
                }

                sendResource(resposta);
                return;
            case "PileSizeRequest":
                sendResource("" + player.getPileSize());
                return;
            case "ProduceDeckRequest":
                player.initDeck();
                sendResource("DeckProduzido");
                return;
//		case "CHOSEFLOR":
//			int index = Integer.parseInt(requisicao.split(",")[2]);
//			chooseFlowerDeckEnd(index);
//			return;
            default:
                sendResource("Request-Perdida");
                System.out.println("Comando Interno Perdido : " + requisicao);
                return;
        }

    }

    private boolean isTopPlayer() {
        return gameController().getTopPlayer() != null && gameController().getTopPlayer().equals(player);
    }

    private boolean isBottonPlayer() {
        return gameController().getBottomPlayer() != null && gameController().getBottomPlayer().equals(player);
    }

    private void comandoExterno(String requisicao) {
        switch (requisicao.split(",")[1]) {
            case "HaveOponentRequest":
                if (isTopPlayer() && gameController().getBottomPlayer() == null
                        || isBottonPlayer() && gameController().getTopPlayer() == null) {
                    sendResource("false");
                } else {
                    sendResource("true");
                }
                return;
            default:
                sendResource("Request-Perdida");
                System.out.println("Comando Externo Perdido : " + requisicao);
                return;
        }
    }

    private GameController gameController() {
        return GameController.getInstance();
    }

    public void sendResource(String resource) {
        clienteAnswer.println(resource);
    }
}
