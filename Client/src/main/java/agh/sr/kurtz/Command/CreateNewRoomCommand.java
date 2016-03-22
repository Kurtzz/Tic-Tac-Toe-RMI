package agh.sr.kurtz.Command;

import agh.sr.kurtz.Application;
import agh.sr.kurtz.Client;
import agh.sr.kurtz.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateNewRoomCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(CreateNewRoomCommand.class);
    public static final String INVOCATION_PREFIX = "-c";
    public static final String DESCRIPTION = "Creates a new room";
    private final Application application;
    private final Client client;

    public CreateNewRoomCommand(Application application, Client client) {
        this.application = application;
        this.client = client;
    }

    public void execute() {
        try {
            Room newRoom = client.createNewRoom(application.getPlayer());
            application.gameMode(newRoom);
            newRoom.notifyPlayerReady(application.getPlayer());
        } catch (Exception e) {
            log.error("Error execution command", e);
        }
    }
}
