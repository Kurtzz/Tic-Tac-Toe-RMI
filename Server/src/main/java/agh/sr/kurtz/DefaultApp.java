package agh.sr.kurtz;

import agh.sr.kurtz.bot.RandomBotLogic;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import static agh.sr.kurtz.Config.*;

public class DefaultApp implements App {
    private final String rmiRegistryAddress;
    private final RoomsRepository roomsRepository = new RoomsRepository();

    public DefaultApp(String rmiIP, int rmiPort) {
        rmiRegistryAddress = buildRbiRegistryAddress(rmiIP, rmiPort);
    }

    private String buildRbiRegistryAddress(String rmiIP, int rmiPort) {
        return String.format(RMI_ADDRESS_FORMAT, rmiIP, rmiPort);
    }

    public Set<Room> getRooms() throws RemoteException {
        return roomsRepository.view();
    }

    public Room createNewRoom(Player player) throws RemoteException {
        DefaultBoard board = new DefaultBoard();
        RoomID roomID = new RoomID();
        Room newRoom = new DefaultRoom(roomID, board);

        newRoom.addPlayer(player);
        roomsRepository.addRoom(newRoom);

        Board stub = (Board) UnicastRemoteObject.exportObject(board, 0);

        String boardRMIAddress = rmiRegistryAddress + "/" + ROOM + "/" + roomID.toString() + "/" + BOARD;
        try {
            Naming.rebind(boardRMIAddress, stub);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return (Room) UnicastRemoteObject.exportObject(newRoom, 0);
    }

    public Room createNewBotRoom(Player player) throws RemoteException {
        Room newRoom = createNewRoom(player);

        Player bot = (Player) UnicastRemoteObject.exportObject(new BotPlayer(newRoom.getBoard(), new RandomBotLogic()),0);
        newRoom.addPlayer(bot);

        return newRoom;
    }

    public void joinRoom(RoomID roomID, Player player) throws RemoteException {
        Room room = roomsRepository.load(roomID);
        room.addPlayer(player);
    }
}
