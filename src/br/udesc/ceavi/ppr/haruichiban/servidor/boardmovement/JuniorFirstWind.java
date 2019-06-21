package br.udesc.ceavi.ppr.haruichiban.servidor.boardmovement;

import br.udesc.ceavi.ppr.haruichiban.servidor.command.FirstWindCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.command.MacroCommand;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.GameServidorController;
import br.udesc.ceavi.ppr.haruichiban.servidor.control.interfaces.IBoardController;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.IModelBoardTile;
import br.udesc.ceavi.ppr.haruichiban.servidor.model.folha.Folha;
import br.udesc.ceavi.ppr.haruichiban.utils.Diretion;
import java.awt.Point;

/**
 *
 * @author Gustavo C Santos
 * @since 13/05/2019
 *
 */
public class JuniorFirstWind implements BoardMovement {

    private final IBoardController boardController;
    private Point origem;
    private Point fim;
    private Diretion destino;

    public JuniorFirstWind() {
        this.boardController = GameServidorController.getInstance().getBoardController();
    }

    @Override
    public boolean addPoint(Point positionBoard) {
        if (validacaoOrigem(positionBoard)) {
            origem = positionBoard;
            return true;
        }
        return false;
    }

    private boolean validacaoOrigem(Point positionBoard) {
        IModelBoardTile boardTile = boardController.getBoardTile(positionBoard);
        return boardTile.hasFolha();
    }

    public boolean isReady() {
        return origem != null && destino != null;
    }

    @Override
    public synchronized void execute() {
        MacroCommand macroCommand = new MacroCommand();

        mover(origem, fim, destino, boardController.getBoardTile(origem).removeFolha(), macroCommand);
        GameServidorController.getInstance().executeCommand(macroCommand);

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
        }
        return false;
    }
}
