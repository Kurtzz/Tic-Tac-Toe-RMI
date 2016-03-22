package agh.sr.kurtz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Room extends Remote {
    RoomID getID() throws RemoteException;
    Board getBoard() throws RemoteException;
    void notifyPlayerReady(Player player) throws RemoteException;
    Set<Player> getPlayers() throws RemoteException;
    void addPlayer(Player player) throws RemoteException;
}
