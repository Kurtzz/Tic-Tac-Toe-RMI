package agh.sr.kurtz.Command;

import agh.sr.kurtz.Application;
import agh.sr.kurtz.Client;

import java.util.HashMap;
import java.util.Map;

public class CommandRouter {
    private final static Map<String, String> commands = new HashMap<String, String>();

    private Application application;
    private Client client;

    static {
        commands.put(CreateNewRoomCommand.INVOCATION_PREFIX, CreateNewRoomCommand.DESCRIPTION);
        commands.put(PlayWithBotCommand.INVOCATION_PREFIX, PlayWithBotCommand.DESCRIPTION);
        commands.put(ListRoomsCommand.INVOCATION_PREFIX, ListRoomsCommand.DESCRIPTION);
        commands.put(JoinRoomCommand.INVOCATION_PREFIX, JoinRoomCommand.DESCRIPTION);
    }

    public CommandRouter(Application application, Client rmiClient) {
        this.application = application;
        this.client = rmiClient;
    }

    public Command route(String cmd) {
        if (cmd.startsWith(CreateNewRoomCommand.INVOCATION_PREFIX)) {
            return new CreateNewRoomCommand(application, client);
        } else if (cmd.startsWith(PlayWithBotCommand.INVOCATION_PREFIX)) {
            return new PlayWithBotCommand(application, client);
        } else if (cmd.startsWith(ListRoomsCommand.INVOCATION_PREFIX)) {
            return new ListRoomsCommand(client, application);
        } else if (cmd.startsWith(JoinRoomCommand.INVOCATION_PREFIX)) {
            return new JoinRoomCommand(client, application, cmd);
        }

        System.out.println("No such command!");
        return new HelpCommand();
    }

    public static void printAvailableCommands() {
        for (String s : commands.keySet()) {
            System.out.printf("%s - %s\n", s, commands.get(s));
        }
    }
}
