package agh.sr.kurtz.Command;

import agh.sr.kurtz.Application;
import agh.sr.kurtz.Client;
import agh.sr.kurtz.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by P on 20.03.2016.
 */
public class PlayWithBotCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(PlayWithBotCommand.class);
    public static final String INVOCATION_PREFIX = "-b";
    public static final String DESCRIPTION = "Creates a new room with bot";
    private final Application application;
    private final Client client;

    public PlayWithBotCommand(Application application, Client client) {
        this.application = application;
        this.client = client;
    }

    public void execute() {
        try {
            Room newRoom = client.createNewBotRoom(application.getPlayer());
            application.gameMode(newRoom);
            newRoom.notifyPlayerReady(application.getPlayer());
        } catch (Exception e) {
            log.error("Error execution command", e);
        }
    }
}
