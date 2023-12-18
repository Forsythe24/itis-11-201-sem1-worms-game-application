package worms.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import worms.gui.handler.ClientInputHandler;
import worms.gui.model.ServerConfig;
import worms.gui.model.UserConfig;
import worms.gui.view.*;
import worms.net.Client;

import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;

public class WormsApplication extends Application {

    private UserConfig userConfig;
    private UserConfigView userConfigView;

    private ServerConfig serverConfig;

    private ServerConfigView serverConfigView;
    private BorderPane root;

    private ServerRunningView serverRunningView;

    private GameView gameView;

    private Client client;

    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Worms");
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        BaseView.setWormsApplication(this);

        userConfigView = new UserConfigView();

        root = new BorderPane();
        scene = new Scene(root, 400, 300);

        scene.getStylesheets().add(WormsApplication.class.getResource("/worms.css").toExternalForm());



        primaryStage.setScene(scene);
        primaryStage.show();
        setView(userConfigView);
    }


    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }


    public UserConfig getUserConfig() {
        return userConfig;
    }

    public ServerRunningView getServerRunningView() {
        return serverRunningView;
    }

    public UserConfigView getUserConfigView() {
        return userConfigView;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public ServerConfigView getServerConfigView() {
        return serverConfigView;
    }

    public void setUserConfigView(UserConfigView userConfigView) {
        this.userConfigView = userConfigView;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setServerConfigView(ServerConfigView serverConfigView) {
        this.serverConfigView = serverConfigView;
    }

    public void setServerRunningView(ServerRunningView serverRunningView) {
        this.serverRunningView = serverRunningView;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setView(BaseView view) {
        root.setCenter(view.getView());
    }

    public void startGame(final String ipAddress, final int port) {
        System.out.println("Starting game.");
        try {
            client = new Client(ipAddress, port);
        } catch (final ConnectException e) {
            System.out.println("Connection refused.");
            return;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        gameView = new GameView(1,1, client.getGame());
        gameView.setPadding(new Insets(20));

        root.setCenter(gameView);

        ClientInputHandler cil = new ClientInputHandler(client.getGame(), scene);

        gameView.requestFocus();

        client.run();
    }

}