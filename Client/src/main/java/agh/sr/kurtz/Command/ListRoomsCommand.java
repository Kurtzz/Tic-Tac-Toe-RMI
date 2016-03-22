package agh.sr.kurtz.Command;

import agh.sr.kurtz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Set;

/**
 * Created by P on 20.03.2016.
 */
public class ListRoomsCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(JoinRoomCommand.class);

    public static final String INVOCATION_PREFIX = "-l";
    public static final String DESCRIPTION = "Lists all existing rooms";
    private Client client;
    private Application application;

    public ListRoomsCommand(Client client, Application application) {
        this.client = client;
        this.application = application;
    }

    public void execute() {
        App app = client.getServer();
        try {
            Set<Room> rooms = app.getRooms();

            for (Room room : rooms) {
                System.out.printf("Room of id: %s with players:\n", room.getID());
                printPlayers(room);
            }

            application.commandMode();
        } catch (RemoteException e) {
            log.error("Error executing command", e);
        }
    }

    private void printPlayers(Room room) throws RemoteException {
        for (Player player : room.getPlayers()) {
            System.out.printf("\t%s\n", player.getNick());
        }
    }
}
