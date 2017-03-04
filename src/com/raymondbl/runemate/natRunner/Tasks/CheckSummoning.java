package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class CheckSummoning extends Task {
    private GameObject obelisk;

    @Override
    public void execute() {
        Path path = Data.web.getPathBuilder().buildTo(Data.OBELISK);
        Data.gui.setStatus("Walking to obelisk...");
        if (Camera.getYaw() < 252 || Camera.getYaw() > 300)
            Camera.setYaw((int) Random.nextGaussian(262, 310));
        if (Camera.getPitch() < 0.25 || Camera.getPitch() > 0.40)
            Camera.setPitch(Random.nextGaussian(0.25, 0.40));
        if (Execution.delayUntil(() ->
        {
            if (stepCondition()) {
                path.step();
                return false;
            } else return true;
        }, () -> Data.player.isMoving(), 5643, 9817)) ;
        else if (validate()) {
            execute();
            return;
        } else return;

        if (Execution.delayUntil(() -> !Util.isNull(
                obelisk = Util.nearestLoaded(Data.OBELISK_NAME)),
                () -> Data.player.isMoving(), 2400, 4800)) ;

        Data.gui.setStatus("Renewing points...");
        Util.getIntoView(obelisk);

        if (obelisk.interact("Renew points")) {
            System.out.println("renewing points");
            if (Execution.delayUntil(() ->
                    Summoning.getPoints() > Data.gui.getSummoningThreshold(), () ->
                    Data.player.isMoving(), 1800, 3600)) {
                Data.runner.getWalkToAltar().setDistance(18);
            } else if (validate()) {
                execute();
            }
        } else if (validate()) execute();
    }

    public boolean stepCondition() {
        GameObject object = Util.nearestLoaded(Data.OBELISK_NAME);
        return Util.isNull(object) ||
                !object.isVisible() ||
                Data.player.distanceTo(Data.OBELISK) > 20;
    }

    @Override
    public boolean validate() {
        return Data.player.distanceTo(Data.OBELISK) < 300 &&
                Summoning.getPoints() < Data.gui.getSummoningThreshold();
    }

}

