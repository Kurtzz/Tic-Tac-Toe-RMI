package agh.sr.kurtz.Command;

import agh.sr.kurtz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class JoinRoomCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(JoinRoomCommand.class);

    public static final String INVOCATION_PREFIX = "-j";
    public static final String DESCRIPTION = "<room id> Joins an existing room";
    private Client client;
    private Application application;
    private String command;

    public JoinRoomCommand(Client client, Application application, String command) {
        this.client = client;
        this.application = application;
        this.command = command;
    }

    public void execute() {
        String roomIDPrefix = prepareRoomIDPrefix();

        App app = client.getServer();
        Player player = null;
        try {
            player = (Player) UnicastRemoteObject.exportObject(application.getPlayer(),0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            Set<Room> rooms = app.getRooms();

            for (Room room : rooms) {
                if (isRoomIdMatching(roomIDPrefix, room)) {
                    app.joinRoom(room.getID(), player);
                    application.gameMode(room);
                    room.notifyPlayerReady(application.getPlayer());
                    return;
                }
            }
            System.out.println("No matching room id! Try again");
        } catch (Exception e) {
            log.error("Error executing command", e);
        }
    }

    private boolean isRoomIdMatching(String roomIDPrefix, Room room) throws RemoteException {
        return room.getID().toString().startsWith(roomIDPrefix);
    }

    private String prepareRoomIDPrefix() {
        return command.split("\\s+")[1];
    }
}
