package worms.gui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import worms.gui.model.ServerConfig;
import worms.net.Server;

public class ServerConfigView extends BaseView {
    private AnchorPane pane;
    private VBox box;
    private TextField host;
    private TextField port;
    private Button start;

    private Server server;



    final EventHandler<ActionEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == start) {
                ServerConfig serverConfig = new ServerConfig();
                serverConfig.setHost(host.getText());
                int portNumber = Integer.parseInt(port.getText());
                serverConfig.setPort(portNumber);

                server = new Server(portNumber);

                getWormsApplication().setServerConfig(serverConfig);

                getWormsApplication().setView(getWormsApplication().getServerRunningView());

                server.run();

                getWormsApplication().setView(getWormsApplication().getUserConfigView());
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
        pane = new AnchorPane();

        box = new VBox(10);

        Label hostLabel = new Label("host");
        host = new TextField();
        host.setText("127.0.0.1");
        Label portLabel = new Label("port");
        port = new TextField();
        port.setText("" + Server.DEFAULT_PORT_NUMBER);
        start = new Button("Start Server");
        start.setOnAction(eventHandler);
        box.getChildren().addAll(hostLabel, host, portLabel, port, start);
        pane.getChildren().addAll(box);
    }
}
