package worms.gui.view;

import javafx.scene.Parent;
import worms.game.WormsApplication;

public abstract class BaseView {

    private static WormsApplication wormsApplication;

    public static WormsApplication getWormsApplication() {
        if (wormsApplication != null) {
            return wormsApplication;
        }
        throw new RuntimeException("No app in base view");
    }

    public static void setHeadBallApplication(WormsApplication wormsApplication) {
        BaseView.wormsApplication = wormsApplication;
    }

    public abstract Parent getView();
}