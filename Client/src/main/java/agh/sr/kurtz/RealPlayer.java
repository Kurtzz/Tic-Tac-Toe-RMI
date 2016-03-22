package agh.sr.kurtz;

import agh.sr.kurtz.Command.CommandRouter;

import java.io.Serializable;
import java.rmi.RemoteException;

public class RealPlayer implements Player, Serializable {
    private final String nick;
    private boolean isReady;
    private transient final Application application;

    public RealPlayer(String nick, Application application) {
        this.nick = nick;
        this.application = application;
    }

    public void onMove() {
        application.readMove();
    }

    public void onGameStart(Mark mark) throws RemoteException {
        application.startGame(mark);
    }

    public void onBoardUpdated() throws RemoteException {
        System.out.println("Move registered! Board updated!");
        application.printBoard();
    }

    public void onWin() {
        afterGameEnd(GameResult.WIN);
    }

    public void onLoose() {
        afterGameEnd(GameResult.LOST);
    }

    public void onDraw() {
        afterGameEnd(GameResult.DRAW);
    }

    private void afterGameEnd(GameResult gameResult) {
        System.out.println(gameResult.getMessage());
        System.out.println("Choose what to do next");
        CommandRouter.printAvailableCommands();
        application.commandMode();
    }

    public String getNick() {
        return nick;
    }

    public boolean isReady() {
        return isReady;
    }

    public void markReady()  {
        this.isReady = true;
    }
}
