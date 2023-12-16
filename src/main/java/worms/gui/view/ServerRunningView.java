package worms.gui.view;


import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ServerRunningView extends BaseView {
    private AnchorPane pane;
    private VBox box;

    @Override
    public Parent getView() {
        if (pane == null) {
            createView();
        }
        return pane;
    }

    private void createView() {
        pane = new AnchorPane();

        box = new VBox(5);

        Label serverStateInfo = new Label("Server is running. ");


        Label portInfo = new Label("Port: " + getWormsApplication().getServerConfig().getPort());

        Label hostInfo = new Label("Host: " + getWormsApplication().getServerConfig().getHost());

        box.getChildren().addAll(serverStateInfo, portInfo, hostInfo);
        pane.getChildren().addAll(box);
    }
}
