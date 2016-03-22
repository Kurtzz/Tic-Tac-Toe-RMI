package agh.sr.kurtz;

import agh.sr.kurtz.Command.Command;
import agh.sr.kurtz.Command.CommandRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import static agh.sr.kurtz.Config.DEFAULT_RMI_REGISTRY_IP;
import static agh.sr.kurtz.Config.DEFAULT_RMI_REGISTRY_PORT;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final String rmiIP;
    private final int rmiPort;
    private final Scanner scanner = new Scanner(System.in);
    private Board currentBoard;
    private CommandRouter commandRouter;
    private Client client;
    private Player player;
    private Room currentRoom;
    private Mark mark;

    public Application (String rmiIP, int rmiPort) {
        this.rmiIP = rmiIP;
        this.rmiPort = rmiPort;
    }

    public static void main(String[] args) {
        //System.setProperty("java.rmi.server.hostname", "192.168.56.101");
        String rmiIP = DEFAULT_RMI_REGISTRY_IP;
        int rmiPort = DEFAULT_RMI_REGISTRY_PORT;

        try {
            if (args.length < 2) {
                log.info("Usage: java Application <ip> <port>");
                log.info("Loading default ip and port...");
            } else {
                rmiIP = args[0];
                rmiPort = Integer.parseInt(args[1]);
            }
        } catch (Exception e) {
            log.info("Wrongs args given!");
            log.info("Loading default ip and port");
            rmiIP = DEFAULT_RMI_REGISTRY_IP;
            rmiPort = DEFAULT_RMI_REGISTRY_PORT;
        }

        Application application = new Application(rmiIP, rmiPort);
        application.play();
    }

    private void play() {
        try {
            printInvitation();

            this.client = new Client(rmiIP, rmiPort);
            this.commandRouter = new CommandRouter(this, client);

            Player realPlayer = new RealPlayer(readNickName(), this);
            this.player = (Player) UnicastRemoteObject.exportObject(realPlayer, 0);
//            this.player = new RealPlayer(readNickName(), this);

            CommandRouter.printAvailableCommands();
            commandMode();

        } catch (Exception e) {
            log.error("Error play", e);
            System.exit(-1);
        }
    }

    public Player getPlayer() {
        return player;
    }

    private String readNickName() {
        System.out.println("What's your name?");
        String nick = null;
        while (nick == null || nick.isEmpty()) {
            nick = scanner.nextLine();
        }

        return nick;
    }

    public void commandMode() {
        String cmd;

        while((cmd = scanner.nextLine()).isEmpty()) {
            log.info("Command line is empty...");
        }

        Command command = commandRouter.route(cmd);
        command.execute();
    }

    public void gameMode(Room newRoom) {
        this.currentRoom = newRoom;
        System.out.println("Waiting for second player...");
    }

    public void readMove() {
        System.out.printf("Your turn! You have: %s\n", mark.name());

        while (!Thread.interrupted()) {
            try {
                int fieldNo = Integer.parseInt(scanner.nextLine());
                System.out.printf("You have chosen %d\n", fieldNo);
                currentBoard.mark(fieldNo, player);
            } catch (RemoteException e) {
                System.out.println("Hey you, this field is already marked! Pick sth else");
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }
    }

    public void startGame(Mark mark) throws RemoteException {
        this.mark = mark;

        System.out.println("Game is starting!");
        try {
            this.currentBoard = client.loadBoard(currentRoom);

            printBoard();
        } catch (Exception e) {
            log.error("Error starting game", e);
        }
    }

    public void printBoard() throws RemoteException {
        System.out.println(currentBoard.showBoard());
    }

    private static void printInvitation() {
        System.out.println("***************************************************");
        System.out.println("* Welcome to kurtz's distributed Tic Tac Toe game *");
        System.out.println("***************************************************");
    }
}
