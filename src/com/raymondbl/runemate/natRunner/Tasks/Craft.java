package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Craft extends Task {

    @Override
    public void execute() {
        Execution.delayUntil(() -> !Util.isNull(
                Util.firstLoaded(Data.ALTAR_ID)));
        Data.gui.setStatus("Crafting runes...");
        if (Util.firstLoaded(Data.ALTAR_ID).interact("Craft-rune")) {
            if (Execution.delayUntil(() -> !validate(),
                    () -> (Data.player.isMoving() ||
                            Data.player.getAnimationId() != -1), 1200, 2400)) ;
            else if (validate()) {
                execute();
                return;
            }
        } else if (validate()) {
            execute();
            return;
        }
        Data.runner.addRun();
    }

    @Override
    public boolean validate() {
        return Data.player.distanceTo(Data.ALTAR) <= 16 &&
                !Util.isCarried(Data.NATURE_RUNE_ID);
    }


}
