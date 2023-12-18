package worms.gui.view;

import javafx.scene.Parent;
import javafx.scene.layout.Region;
import worms.game.WormsApplication;

public abstract class BaseView extends Region {

    private static WormsApplication wormsApplication;

    public static WormsApplication getWormsApplication() {
        if (wormsApplication != null) {
            return wormsApplication;
        }
        throw new RuntimeException("No app in base view");
    }

    public static void setWormsApplication(WormsApplication wormsApplication) {
        BaseView.wormsApplication = wormsApplication;
    }

    public abstract Parent getView();

}