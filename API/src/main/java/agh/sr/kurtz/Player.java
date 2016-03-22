package agh.sr.kurtz;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote, Serializable {
    String getNick() throws RemoteException;
    boolean isReady() throws RemoteException;
    void markReady() throws RemoteException;
    void onMove() throws RemoteException;
    void onGameStart(Mark mark) throws RemoteException;
    void onBoardUpdated() throws RemoteException;
    void onWin() throws RemoteException;
    void onLoose() throws RemoteException;
    void onDraw() throws RemoteException;
}
