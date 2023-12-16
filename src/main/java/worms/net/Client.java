package worms.net;

import worms.game.ClientGame;
import worms.game.ServerGame;
import worms.net.packets.ActionPacket;
import worms.net.packets.ClientPacket;
import worms.net.packets.DisconnectPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.TreeMap;


public class Client implements Runnable {
    private boolean isRunning;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private ClientGame game;
    public ClientGame getGame() {
        return game;
    }

    public Client( String ipAddress,  int port) throws IOException {
        socket = new Socket(ipAddress, port);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        initialServerCommunication();
    }

    @SuppressWarnings("unchecked")
    private void initialServerCommunication() {
        try {
             int clientId = inputStream.readInt();
            game = new ClientGame(this, clientId);

            game.processEntityList(((TreeMap<Integer, ServerGame.Entity>) inputStream.readObject()));

            System.out.println("Finished initial server communication.");
        } catch ( IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        isRunning = true;
        new Thread(this::startReadAndWriteLoop).start();
        new Thread(this::startGameloop).start();
    }

    @SuppressWarnings("unchecked")
    private void startReadAndWriteLoop() {
        while (isRunning) {
            try {
                // read
                game.processEntityList(((TreeMap<Integer, ServerGame.Entity>) inputStream.readObject()));

                // write
                sendPacket(new ActionPacket(game));
                game.getActionSet().getInstantActions().clear();
            } catch ( SocketException e) {
                break;
            } catch ( IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameloop() {
        while (isRunning) {
            game.tick();
        }
    }

    public void sendPacket( ClientPacket packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.reset();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        System.out.println("Disconnecting from server.");

        isRunning = false;

        // tell server we disconnected
        sendPacket(new DisconnectPacket());

        // close everything
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }

//        handleDeath();
    }
}
