package worms.net;

import worms.game.ServerGame;
import worms.game.ServerTeamGame;
import worms.net.packets.ActionPacket;
import worms.net.packets.ClientPacket;
import worms.net.packets.DisconnectPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {
    private final int TICKS_PER_SECOND = 20;
    private final int MILLISECONDS_PER_TICK = 1000000000 / TICKS_PER_SECOND;

    public final static int DEFAULT_PORT_NUMBER = 1234;

    private final ServerGame game;
    private ServerSocket serverSocket;
    private final ArrayList<ClientHandler> clientHandlers;

    public Server( int port) {
        this.game = new ServerTeamGame();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch ( IOException e) {
            e.printStackTrace();
        }
        clientHandlers = new ArrayList<>();
    }

    @Override
    public void run() {
        new Thread(this::startAcceptClientsLoop).start();
        new Thread(this::startGameloop).start();
    }

    private void startAcceptClientsLoop() {
        System.out.println("Accepting Clients.");
        while (true) {
            System.out.println("Waiting for new client.");
            try {
                 Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");
                 ClientHandler clientHandler = new ClientHandler(this, socket, game.spawnPlayerEntity());
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameloop() {
        long lastTickTime = System.nanoTime();

        while (true) {
             long whenShouldNextTickRun = lastTickTime + MILLISECONDS_PER_TICK;
            if (System.nanoTime() < whenShouldNextTickRun) {
                continue;
            }

            game.tick();

            sendUpdatesToAll();

            lastTickTime = System.nanoTime();
        }
    }

    public void processPacket( ClientHandler clientHandler,  ClientPacket packet) {
        if (packet instanceof  ActionPacket actionPacket) {
            game.updateActionSet(clientHandler.getEntityId(), actionPacket.actionSet);
        } else if (packet instanceof  DisconnectPacket disconnectPacket) {
            clientHandler.disconnect();
            game.removeEntity(clientHandler.getEntityId());
            clientHandlers.remove(clientHandler);
        }
    }

    // server to all client
    private void sendUpdatesToAll() {
        for ( ClientHandler clientHandler : clientHandlers) {
            sendUpdates(clientHandler);
        }
    }

    // server to one client
    public void sendUpdates( ClientHandler clientHandler) {
        clientHandler.sendUpdate(game.getEntities());
    }

    public void closeServer() {
        try {
            serverSocket.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void main( String[] args) {
        new Server(Server.DEFAULT_PORT_NUMBER).run();
    }
}
