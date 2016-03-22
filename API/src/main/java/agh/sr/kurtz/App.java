package agh.sr.kurtz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface App extends Remote {
    Set<Room> getRooms() throws RemoteException;
    Room createNewRoom(Player player) throws RemoteException;
    Room createNewBotRoom(Player player) throws RemoteException;
    void joinRoom(RoomID roomID, Player player) throws RemoteException;
}
