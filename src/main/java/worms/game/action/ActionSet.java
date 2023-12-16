package worms.game.action;

import worms.utils.ArraySet;

import java.io.Serializable;
import java.util.ArrayList;



public class ActionSet implements Serializable {
    private final ArrayList<Action> instantActions;
    private final ArraySet<Action> longActions;

    public ActionSet() {
        instantActions = new ArrayList<>();
        longActions = new ArraySet<>();
    }

    public ArrayList<Action> getInstantActions() {
        return instantActions;
    }

    public ArraySet<Action> getLongActions() {
        return longActions;
    }

    @Override
    public String toString() {
        return "ActionSet [instantActions=" + instantActions + ", longActions=" + longActions + "]";
    }
}
