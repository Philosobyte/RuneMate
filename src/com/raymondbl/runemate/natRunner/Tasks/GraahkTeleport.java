package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.utilities.BankUtil;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class GraahkTeleport extends Task {

    @Override
    public void execute() {
        if (!Util.isNull(Data.player.getFamiliar()) && !Util.processInterface("Teleport", 2)) {
            Data.gui.setStatus("Teleporting to Shilo...");
            System.out.println("Graahk: 19");
            BankUtil.close();
            String key = ActionBar.getFirstAction(Data.FAMILIAR).getSlot().getKeyBind();
            if (Keyboard.typeKey(key)) {
                System.out.println("Graahk: 22");
                if (Execution.delayUntil(() -> !Util.isNull(Interfaces.getVisible
                        (Interfaces.getTextContainsFilter("Teleport")).first()), 1200, 2400)) ;
                else if (validate()) {
                    execute();
                    return;
                }
                System.out.println("Graahk: 26");
                if (Util.processInterface("Teleport", 2)) {
                    if (Execution.delayUntil(() ->
                            Data.player.distanceTo(Data.RUINS) < 400, () ->
                            Data.player.getAnimationId() != -1, 3000, 5000)) ;
                    else if (validate()) execute();
                } else if (validate()) execute();
            } else if (validate()) execute();
        }
    }

    @Override
    public boolean validate() {
        return Inventory.getEmptySlots() <= 1 && Util.isNearBank() &&
                Util.hasFamiliar();
    }

}
