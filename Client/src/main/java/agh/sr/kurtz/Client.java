package agh.sr.kurtz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static agh.sr.kurtz.Config.*;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private final String rmiRegistryAddress;
    private App server;

    public Client(String rmiIP, int rmiPort) {
        this.rmiRegistryAddress = buildRbiRegistryAddress(rmiIP, rmiPort);

        setupSecurityManager();
        connectToServer();
    }

    private String buildRbiRegistryAddress(String rmiIP, int rmiPort) {
        return String.format(RMI_ADDRESS_FORMAT, rmiIP, rmiPort);
    }

    private void connectToServer() {
        try {
            this.server = (App) Naming.lookup(rmiRegistryAddress + "/" + APP);
        } catch (Exception e) {
            log.error("Error connecting to server", e);
        }
    }

    private void setupSecurityManager() {
        if (System.getProperty("java.security.policy") == null) {
            System.out.println("!!! Please set location of security policy file! Do it with: -Djava.security.policy=path/to/client/policy/file/client.policy");

            System.out.println("Loading very poor default...");
            String poorDefault = "/home/kurtz/Pulpit/Distributed-Systems/Lab_2/TicTacToe/Client/src/main/resources/client.policy";
            System.setProperty("java.security.policy", poorDefault);
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }

    public App getServer() {
        return server;
    }

    public Room createNewRoom(Player player) throws RemoteException, NotBoundException, MalformedURLException {
        Player stub = (Player) UnicastRemoteObject.exportObject(player, 0);
        return server.createNewRoom(stub);
    }

    public Room createNewBotRoom(Player player) throws RemoteException, NotBoundException, MalformedURLException {
        return server.createNewBotRoom(player);
    }

    public Board loadBoard(Room newRoom) {
        try {
            String roomID = newRoom.getID().toString();
            String roomRMIAddress = rmiRegistryAddress + "/" + ROOM + "/" + roomID + "/" + BOARD;

            return (Board) Naming.lookup(roomRMIAddress);
        } catch (Exception e) {
            log.error("Error loading board", e);
            return null;
        }
    }
}
