package worms.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import worms.gui.model.ServerConfig;
import worms.gui.model.UserConfig;
import worms.gui.view.BaseView;
import worms.gui.view.ServerConfigView;
import worms.gui.view.ServerRunningView;
import worms.gui.view.UserConfigView;

public class WormsApplication extends Application {

    private UserConfig userConfig;
    private UserConfigView userConfigView;

    private ServerConfig serverConfig;

    private ServerConfigView serverConfigView;
    private BorderPane root;

    private ServerRunningView serverRunningView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Head Ball");
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        BaseView.setHeadBallApplication(this);

        userConfigView = new UserConfigView();

        root = new BorderPane();
        Scene scene = new Scene(root, 400, 300);
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

    public void setView(BaseView view) {
        root.setCenter(view.getView());
    }

}