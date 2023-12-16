package worms.gui.handler;

import javafx.scene.Scene;
import worms.game.ClientGame;
import worms.game.action.Action;

import worms.utils.ArraySet;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sun.glass.events.KeyEvent.*;

public class ClientInputHandler {
    private Scene scene;
    private final boolean[] previouslyPressed;
    private final ClientGame game;

    public ClientInputHandler(final ClientGame game, Scene scene) {
        this.game = game;
        this.scene = scene;
        previouslyPressed = new boolean[0xFFFF];
        Arrays.fill(previouslyPressed, false);

        scene.setOnKeyPressed(keyEvent -> {
            final int keyCode = keyEvent.getCode().getCode();

                if (previouslyPressed[keyCode]) {
                    // means that this is due to rapid press in the OS
                    return;
                }

            final ArraySet<Action> longActions = game.getActionSet().getLongActions();
            final ArrayList<Action> instantActions = game.getActionSet().getInstantActions();

            switch (keyCode) {
                case VK_A -> {
                    longActions.add(Action.LEFT_WALK);
                }
                case VK_D -> {
                    longActions.add(Action.RIGHT_WALK);
                }
                case VK_SPACE -> {
                    instantActions.add(Action.JUMP);
                }
                case VK_ENTER -> {
                    instantActions.add(Action.SHOOT);
                }
                case VK_X -> {
                    instantActions.add(Action.SWITCH_WEAPON);
                }
                default -> {
                }
            }
            previouslyPressed[keyCode] = true;
        });

        scene.setOnKeyReleased(keyEvent -> {
            final int keyCode = keyEvent.getCode().getCode();

            final ArraySet<Action> longActions = game.getActionSet().getLongActions();
            switch (keyCode) {
                case VK_A -> {
                    longActions.remove(Action.LEFT_WALK);
                }
                case VK_D -> {
                    longActions.remove(Action.RIGHT_WALK);
                }
                default -> {
                }
            }
            previouslyPressed[keyCode] = false;
        });

    }


}
