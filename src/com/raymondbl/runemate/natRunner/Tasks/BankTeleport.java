package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.natRunner.Data.Teleport;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class BankTeleport extends Task {

    @Override
    public void execute() {
        Teleport teleport = Data.gui.getTeleport();
        switch (teleport) {
            case DUEL:
                teleportDuel();
                break;
            case KIN:
                teleportKin();
                break;
            default:
                teleportTokkulZo(teleport);
                break;
        }
    }

    public void teleportDuel() {
        if (validateTeleportDuel()) {
            SpriteItem ring = Equipment.getItems(Data.RINGS).first();
            if (!Util.isNull(ring)) {
                Data.gui.setStatus("Tele to bank...");
                String key = ActionBar.getFirstAction(Data.RINGS)
                        .getSlot().getKeyBind();
                if (Keyboard.typeKey(key)) {
                    Execution.delayUntil(() -> !Util.isNull(
                            Util.getInterface("Castle Wars Arena")), 1200, 2400);
                    if (Util.processInterface("Castle Wars Arena", 2)) {
                        if (Execution.delayUntil(() -> !validate(),
                                () -> Data.player.getAnimationId() != -1,
                                4000, 8000)) ;
                        else if (validate()) {
                            execute();
                            return;
                        }
                    } else if (validate()) {
                        execute();
                        return;
                    }
                } else if (validate()) {
                    execute();
                    return;
                }
            }
        }
    }

    public void teleportKin() {

    }

    private boolean validateTeleportKin() {
        return true;
    }

    public void teleportTokkulZo(Teleport teleport) {

    }

    private boolean validateTeleportDuel() {
        return Util.isWorn(Data.RINGS);
    }

    @Override
    public boolean validate() {
        return !Util.isNearBank() && Util.isCarried(Data.NATURE_RUNE_ID);
    }

}

