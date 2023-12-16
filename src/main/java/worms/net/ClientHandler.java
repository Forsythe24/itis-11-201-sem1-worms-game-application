package worms.net;

import worms.game.ServerGame;
import worms.net.packets.ClientPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;


public class ClientHandler implements Runnable {
    private boolean isRunning;
    private final int entityId;
    private final Server server;
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public int getEntityId() {
        return entityId;
    }

    public ClientHandler( Server server,  Socket socket,  int id) {
        this.server = server;
        this.socket = socket;
        this.entityId = id;

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch ( IOException e) {
            e.printStackTrace();
        }

        initialClientCommunication();
    }

    private void initialClientCommunication() {
        try {
            outputStream.writeInt(entityId);
            server.sendUpdates(this);
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        isRunning = true;
        startRecieveMessageLoop();
    }

    // client to server
    private void startRecieveMessageLoop() {
        while (isRunning) {
            try {
                 ClientPacket packet = (ClientPacket) inputStream.readObject();
                server.processPacket(this, packet);
            } catch ( IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // server to client
    public void sendUpdate( TreeMap<Integer, ServerGame.Entity> update) {
        if (!isRunning) {
            return;
        }

        try {
            outputStream.writeObject(update);
            outputStream.reset();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        System.out.println("A client has disconnected.");

        isRunning = false;

        // close everything
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
