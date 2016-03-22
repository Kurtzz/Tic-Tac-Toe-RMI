package agh.sr.kurtz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import static agh.sr.kurtz.Config.*;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private final String rmiRegistryAddress;
    private final int registryPort;

    public Server(String registryIP, int registryPort) {
        this.registryPort = registryPort;
        this.rmiRegistryAddress = buildRegistryAddress(registryIP, registryPort);
    }

    public static void main(String[] args) {
        //System.setProperty("java.rmi.server.hostname", "192.168.56.101");
        String registryIP;
        int registryPort;

        try {
            if (args.length < 2) {
                System.out.println("Too little args given!");
                System.out.println("Usage: java Server <ip> <port>");
                System.out.println("Loading default IP and port");

                registryIP = Config.DEFAULT_RMI_REGISTRY_IP;
                registryPort = Config.DEFAULT_RMI_REGISTRY_PORT;
            } else {
                registryIP = args[0];
                registryPort = Integer.parseInt(args[1]);
            }
        } catch (Exception e) {
            System.out.println("Wrongs args given!");
            System.out.println("Loading default ip and port");
            registryIP = Config.DEFAULT_RMI_REGISTRY_IP;
            registryPort = Config.DEFAULT_RMI_REGISTRY_PORT;
        }

        Server server = new Server(registryIP, registryPort);
        server.play();
    }

    private String buildRegistryAddress(String registryIp, int registryPort) {
        return String.format(RMI_ADDRESS_FORMAT, registryIp, registryPort);
    }

    private void play() {
        try {
            setupSecurityManager();
            DefaultApp app = new DefaultApp("127.0.0.1", registryPort);
            LocateRegistry.createRegistry(registryPort);
            App appStub = (App) UnicastRemoteObject.exportObject(app, 0);
            Naming.rebind(rmiRegistryAddress + "/" + APP, appStub);

            log.debug("Registry initialized");
            log.debug("Server up...");
        } catch (Exception e) {
            log.error("Error starting server", e);
            System.exit(-1);
        }
    }

    private static void setupSecurityManager() {
        if (System.getProperty("java.security.policy") == null) {
            System.out.println("!!! Please set location of security policy file! Do it with: -Djava.security.policy=path/to/server/policy/file/server.policy");

            System.out.println("Loading very poor default...");
            String poorDefault = "/home/kurtz/Pulpit/Distributed-Systems/Lab_2/TicTacToe/Server/src/main/resources/server.policy";
            System.setProperty("java.security.policy", poorDefault);
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }
}
