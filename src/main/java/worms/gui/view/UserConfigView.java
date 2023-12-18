package worms.gui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import worms.gui.handler.ClientInputHandler;
import worms.gui.model.UserConfig;
import worms.net.Client;
import worms.net.Server;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Stack;

public class UserConfigView extends BaseView{
    private StackPane pane;
    private VBox box;
    private TextField host;
    private TextField port;
    private TextField username;
    private Button start;
    private Canvas canvas;

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

    private void createView() {
        pane = new StackPane();

        canvas = new Canvas();

        canvas.setWidth(400);
        canvas.setHeight(150);


        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image logoImage = new Image("worms-logo.png");

        gc.drawImage(logoImage,50, 0, 300, 80);

        box = new VBox(10);

        box.setMaxWidth(800);
        box.setMaxHeight(800);

        box.setPadding(new Insets(60));

        Label usernameLabel = new Label("Username");
        username = new TextField();
        username.setPrefHeight(30);

        Label hostLabel = new Label("Host");
        host = new TextField();
        host.setText("127.0.0.1");
        host.setPrefHeight(30);

        Label portLabel = new Label("Port");
        port = new TextField();
        port.setText("" + Server.DEFAULT_PORT_NUMBER);
        port.setPrefHeight(30);

        start = new Button("Join Game");
        start.setOnAction(eventHandler);

        box.getChildren().addAll(usernameLabel, username, hostLabel, host, portLabel, port, start);

        StackPane.setAlignment(canvas, Pos.TOP_CENTER);
        StackPane.setAlignment(start, Pos.CENTER);
        StackPane.setAlignment(box, Pos.CENTER);

        StackPane.setMargin(canvas, new Insets(10));
        StackPane.setMargin(box, new Insets(10));


        pane.getChildren().addAll(box, canvas);
    }
}
