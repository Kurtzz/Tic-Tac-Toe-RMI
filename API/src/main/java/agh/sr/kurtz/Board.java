package agh.sr.kurtz;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Board extends Remote{
    String showBoard() throws RemoteException;
    void mark(int field, Player player) throws RemoteException;
}
