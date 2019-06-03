package br.udesc.ceavi.ppr.haruichiban.boardmovement;

import br.udesc.ceavi.ppr.haruichiban.command.FirstWindCommand;
import br.udesc.ceavi.ppr.haruichiban.command.MacroCommand;
import br.udesc.ceavi.ppr.haruichiban.control.GameController;
import br.udesc.ceavi.ppr.haruichiban.control.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.control.IFluxoController;
import br.udesc.ceavi.ppr.haruichiban.control.IPlayerController;
import br.udesc.ceavi.ppr.haruichiban.model.ModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 13/05/2019
 *
 */
public class JuniorFirstWind implements BoardMovement {

    private IPlayerController player;
    private IBoardController boardController;
    private Point origem;
    private Point fim;
    private Diretion destino;
    private IFluxoController fluxoController;

    public JuniorFirstWind(IPlayerController player, IBoardController boardController, IFluxoController fluxoController) {
        this.player = player;
        this.boardController = boardController;
        this.fluxoController = fluxoController;
        boardController.notifyDirectionAtivar();
    }

    @Override
    public boolean addPoint(Point positionBoard) {
        if (validacaoOrigem(positionBoard)) {
            if (origem == null) {
                player.notifySimples("Folha Escolhida, Escolha a Direção");
            } else {
                player.notifySimples("Nova Folha Escolhida, Escolha a Direção");
            }
            origem = positionBoard;
            return true;
        }
        return false;
    }

    private boolean validacaoOrigem(Point positionBoard) {
        ModelBoardTile boardTile = boardController.getBoardTile(positionBoard);
        if (!boardTile.hasFolha()) {
            player.notifySimples("So Pode Mover Uma Folha");
            return false;
        }
        return true;
    }

    public boolean isReady() {
        return origem != null && destino != null;
    }

    @Override
    public synchronized void execute() {
        MacroCommand macroCommand = new MacroCommand();
        boardController.removeBoardMovement();

        mover(origem, fim, destino, boardController.getBoardTile(origem).removeFolha(), macroCommand);
        GameController.getInstance().executeCommand(macroCommand);

        boardController.renderBoard();
        player.setFase(fluxoController.firstWindEnd());
        fluxoController.firstWind();
        player.notifySimples("");
    }

    @Override
    public boolean tableInteraction() {
        return true;
    }

    private boolean validarMovimento(Point newPoint, Diretion p) {
        switch (p) {
            case NORTE:
                if (0 <= newPoint.y) {
                    if (boardController.getBoardTile(newPoint).hasFolha()) {
                        return validarMovimento(new Point(newPoint.x, newPoint.y - 1), p);
                    } else {
                        fim = newPoint;
                        return true;
                    }
                } else {
                    return false;
                }
            case SUL:
                if (boardController.getAlturaTabuleiro() > newPoint.y) {
                    if (boardController.getBoardTile(newPoint).hasFolha()) {
                        return validarMovimento(new Point(newPoint.x, newPoint.y + 1), p);
                    } else {
                        fim = newPoint;
                        return true;
                    }
                } else {
                    return false;
                }
            case LEST:
                if (boardController.getLarguraTabuleiro() > newPoint.x) {
                    if (boardController.getBoardTile(newPoint).hasFolha()) {
                        return validarMovimento(new Point(newPoint.x + 1, newPoint.y), p);
                    } else {
                        fim = newPoint;
                        return true;
                    }
                } else {
                    return false;
                }
            case OESTE:
                if (0 <= newPoint.x) {
                    if (boardController.getBoardTile(newPoint).hasFolha()) {
                        return validarMovimento(new Point(newPoint.x - 1, newPoint.y), p);
                    } else {
                        fim = newPoint;
                        return true;
                    }
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    private void mover(Point atual, Point fim,
            Diretion p,
            Folha peca, MacroCommand macroCommand) {
        if (!atual.equals(this.fim)) {
            Point pProxima = null;
            switch (p) {
                case NORTE:
                    pProxima = new Point(atual.x, atual.y - 1);
                    break;
                case SUL:
                    pProxima = new Point(atual.x, atual.y + 1);
                    break;
                case LEST:
                    pProxima = new Point(atual.x + 1, atual.y);
                    break;
                case OESTE:
                    pProxima = new Point(atual.x - 1, atual.y);
                    break;
                default:
                    break;
            }
            macroCommand.addCommand(new FirstWindCommand(boardController.getBoardTile(pProxima), peca));
            Folha folhaDaFrente = boardController.getBoardTile(pProxima).removeFolha();

            mover(pProxima, fim, p, folhaDaFrente, macroCommand);
        }
    }

    @Override
    public boolean addDiretion(Diretion deretion) {

        if (origem != null) {
            if (validarMovimento(origem, deretion)) {
                destino = deretion;
                if (isReady()) {
                    execute();
                }
                return true;
            }
            player.notifySimples("Movimento Não Valido");
        }
        return false;
    }
}
