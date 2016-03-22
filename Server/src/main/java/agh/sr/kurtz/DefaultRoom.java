package agh.sr.kurtz;

import agh.sr.kurtz.exception.RoomAlreadyFullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class DefaultRoom implements Room {
    private static final Logger log = LoggerFactory.getLogger(DefaultRoom.class);
    private final RoomID roomID;
    private final Board board;
    private Player xPlayer;
    private Player oPlayer;
    private boolean isRoomReady;

    public DefaultRoom(RoomID roomID, Board board) {
        this.roomID = roomID;
        this.board = board;
    }

    public RoomID getID() throws RemoteException {
        return roomID;
    }

    public Board getBoard() throws RemoteException {
        return board;
    }

    public void notifyPlayerReady(Player player) throws RemoteException {
        player.markReady();

        if (!isRoomReady) {
            return;
        }

        if (!xPlayer.isReady() || !oPlayer.isReady()) {
            log.info("Players not ready yet. Waiting...");
            return;
        }

        startGame();
    }

    private void startGame() throws RemoteException {
        ((DefaultBoard)board).startGame(xPlayer, oPlayer);
    }

    public Set<Player> getPlayers() throws RemoteException {
        Set<Player> players = new HashSet<Player>();
        if (xPlayer != null) {
            players.add(xPlayer);
        }
        if (oPlayer != null) {
            players.add(oPlayer);
        }
        return players;
    }

    public void addPlayer(Player player) throws RemoteException {
        if (oPlayer != null && xPlayer != null) {
            throw new RoomAlreadyFullException();
        }

        if (xPlayer == null) {
            xPlayer = player;
        } else {
            oPlayer = player;
            isRoomReady = true;
        }
    }
}
