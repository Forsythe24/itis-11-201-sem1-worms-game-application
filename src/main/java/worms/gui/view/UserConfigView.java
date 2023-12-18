package worms.gui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import worms.gui.handler.ClientInputHandler;
import worms.gui.model.UserConfig;
import worms.net.Client;
import worms.net.Server;

import java.io.IOException;
import java.net.ConnectException;

public class UserConfigView extends BaseView{
    private AnchorPane pane;
    private VBox box;
    private TextField host;
    private TextField port;
    private TextField username;
    private Button start;

    private Client client;

    final EventHandler<ActionEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == start) {
                UserConfig userConfig = new UserConfig();
                userConfig.setHost(host.getText());
                userConfig.setUsername(username.getText());
                int portNumber = Integer.parseInt(port.getText());
                userConfig.setPort(portNumber);


                getWormsApplication().setUserConfig(userConfig);

                getWormsApplication().startGame(host.getText(), portNumber);
            }
        }
    };

    @Override
    public Parent getView() {
        if (pane == null) {
            createView();
        }
        return pane;
    }

//    public void startGame(final String ipAddress, final int port) {
//        System.out.println("Starting game.");
//        try {
//            client = new Client(ipAddress, port);
//        } catch (final ConnectException e) {
//            System.out.println("Connection refused.");
//            return;
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
//
//        GameView grid = new GameView(1,1, client.getGame());
//        grid.setPadding(new Insets(20));
//        Scene scene = new Scene(grid, 400, 400);
//
//        ClientInputHandler cil = new ClientInputHandler(client.getGame(), scene);
//
//
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
//
//
//        grid.requestFocus();
//
//        client.run();
//    }

    private void createView() {
        pane = new AnchorPane();

        box = new VBox(10);

        Label usernameLabel = new Label("username");
        username = new TextField();
        Label hostLabel = new Label("host");
        host = new TextField();
        host.setText("127.0.0.1");
        Label portLabel = new Label("port");
        port = new TextField();
        port.setText("" + Server.DEFAULT_PORT_NUMBER);
        start = new Button("Join Game");
        start.setOnAction(eventHandler);
        box.getChildren().addAll(usernameLabel, username, hostLabel, host, portLabel, port, start);
        pane.getChildren().addAll(box);
    }
}
