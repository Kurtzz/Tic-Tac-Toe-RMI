package agh.sr.kurtz;

import agh.sr.kurtz.bot.BotLogic;

import java.rmi.RemoteException;
import java.util.UUID;

public class BotPlayer implements Player {
    private String nick;
    private Board board;
    private BotLogic logic;

    public BotPlayer(Board board, BotLogic logic) {
        this.nick = generateNick();
        this.board = board;
        this.logic = logic;
    }

    private String generateNick() {
        return "BOT" + UUID.randomUUID().toString();
    }

    public String getNick() throws RemoteException {
        return nick;
    }

    public boolean isReady() throws RemoteException {
        return true;
    }

    public void markReady() throws RemoteException {
        //pass
    }

    public void onMove() throws RemoteException {
        while(!Thread.interrupted()) {
            int field = logic.nextMove();
            board.mark(field, this);
        }
    }

    public void onGameStart(Mark mark) throws RemoteException {
        //pass
    }

    public void onBoardUpdated() throws RemoteException {
        //pass
    }

    public void onWin() throws RemoteException {
        //pass
    }

    public void onLoose() throws RemoteException {
        //pass
    }

    public void onDraw() throws RemoteException {
        //pass
    }
}
