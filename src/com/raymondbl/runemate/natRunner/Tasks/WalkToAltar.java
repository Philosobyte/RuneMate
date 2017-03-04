package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class WalkToAltar extends Task {
    private int distance = 22;

    @Override
    public void execute() {
        Path path = Data.web.getPathBuilder().buildTo(Data.RUINS);
        Data.gui.setStatus("Walking to Ruins...");
        if (Camera.getYaw() < 252 || Camera.getYaw() > 300)
            Camera.setYaw((int) Random.nextGaussian(262, 310));
        if (Camera.getPitch() < 0.25 || Camera.getPitch() > 0.40)
            Camera.setPitch(Random.nextGaussian(0.25, 0.40));
        if (path != null) ;
        else if (validate()) {
            execute();
            return;
        } else {
            return;
        }
        Execution.delayUntil(() ->
        {
            if (stepCondition()) {
                path.step();
                if (distance != 22)
                    distance = 22;
                return false;
            } else return true;
        });
        GameObject ruins = Util.firstLoaded(Data.RUINS_ID);
        if (!Util.isNull(ruins)) ;
        else if (validate()) {
            execute();
            return;
        }
        enter(ruins);
    }

    @Override
    public boolean validate() {
        return Data.player.distanceTo(Data.RUINS) < 300 &&
                !Util.isCarried(Data.NATURE_RUNE_ID);
    }

    public boolean stepCondition() {
        GameObject object = Util.firstLoaded(Data.RUINS_NAME);
        return !Util.isVisible(object) ||
                object.distanceTo(Data.player) > distance;
    }

    private void enter(GameObject ruins) {
        Util.getIntoView(ruins);
        if (validateEnter()) {
            Data.gui.setStatus("Entering ruins...");
            ruins = Util.firstLoaded(Data.RUINS_NAME);
            Mouse.setSpeedMultiplier(2);
            if (ruins.interact("Enter")) {
                if (Execution.delayUntil(() -> !validateEnter(),
                        () -> Data.player.isMoving(), 2500, 5000)) ;
                else enter(ruins);
                Mouse.setSpeedMultiplier(1);
            } else enter(ruins);
        }
    }

    private boolean validateEnter() {
        return Data.player.distanceTo(Data.ALTAR) > 15;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}
