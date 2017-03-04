package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.natRunner.NatRunnerBankUtil;
import com.raymondbl.runemate.natRunner.Pouch;
import com.raymondbl.runemate.utilities.BankUtil;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.List;

public class Refill extends Task {

    private int essRemoved;
    private int essAdded;
    private boolean preset;

    @Override
    public void execute() {
        setPreset();
        if (Data.gui.isMassiveSelected()) {
            if (Util.isCarried(Data.NEW_MASSIVE_POUCH.getId()))
                fill(Data.NEW_MASSIVE_POUCH);
            else if (Util.isCarried(Data.MASSIVE_POUCH.getId()))
                fill(Data.MASSIVE_POUCH);
        }
        for (int i = 0; i < Data.NON_DEGR_POUCHES_ARRAY.length; i++) {
            Pouch pouch = Data.NON_DEGR_POUCHES_ARRAY[i];
            if (pouch.isUsed()) {
                fill(pouch);
            }
        }
        if (Inventory.getEmptySlots() > 1)
            bank();

    }

    public void fill(Pouch pouch) {
        if (validateFill(pouch)) {
            if (pouch.isLastRepaired()) {
                empty(pouch);
                pouch.setLastRepaired(false);
            }
            Data.gui.setStatus("Filling " + pouch.getName() + "...");
            int capacity = pouch.getCapacity();
            if (Inventory.getQuantity(Data.PURE_ESS) >= capacity) {
                BankUtil.close();
                CheckPouches checkPouches = Data.runner.getCheckPouches();
                essRemoved = 0;
                SlotAction action = pouch.getSlotAction();
                if (!Util.isNull(action) && Keyboard.typeKey(action.getSlot().getKeyBind())) {
                    if (Execution.delayUntil(() -> essRemoved != 0, 867, 1833)) {
                        if (pouch.getName().equals(Data.MASSIVE_POUCH.getName()))
                            pouch.resetCount();
                    } else {
                        fill(pouch);
                        return;
                    }
                    if (Execution.delayUntil(() -> checkPouches.validate(), 60, 125)) {
                        checkPouches.execute();
                        fill(pouch);
                    }
                } else fill(pouch);
            } else {
                Data.gui.setStatus("Withdrawing essence...");
                if (pouch.getId() == Data.NEW_MASSIVE_POUCH.getId()) {
                    NatRunnerBankUtil.withdrawAll(Data.PURE_ESS);
                } else
                    bank();
                fill(pouch);

            }
        }
    }

    private boolean validateFill(Pouch pouch) {
        return !pouch.isFilled() || pouch.isLastRepaired();
    }

    public void empty(Pouch pouch) {
        if (validateEmpty(pouch)) {
            int capacity = pouch.getDegradedVariant().getCapacity();
            if (Inventory.getEmptySlots() < capacity) {
                NatRunnerBankUtil.depositAll();
                empty(pouch);
                return;
            }

            essAdded = 0;
            if (Keyboard.typeKey(pouch.getSlotAction().getSlot().getKeyBind())) {
                if (Execution.delayUntil(() -> essAdded != 0, 830, 1586)) ;
                else empty(pouch);
            } else empty(pouch);
        }
    }

    private boolean validateEmpty(Pouch pouch) {
        return pouch.isFilled();
    }

    public void executePlaceholder() {
        setPreset();
        List<SlotAction> actions = ActionBar.getActions(Data.POUCHES_IDS);
        while (!actions.isEmpty()) {
            Data.gui.setStatus("Filling pouches...");
            SlotAction action = actions.get(0);
            if (Inventory.contains(Inventory.getIdFilter(action.getId())
                    .and(Inventory.getActionFilter("Fill")))) {
                int capacity = new Pouch(action.getId()).getCapacity();
                if (Inventory.getQuantity(Data.PURE_ESS) >= capacity) {
                    BankUtil.close();
                    CheckPouches checkPouches = Data.runner.getCheckPouches();
                    if (Keyboard.typeKey(action.getSlot().getKeyBind())) {
                        Execution.delayUntil(() -> essRemoved != 0, 867, 1833);
                        if (checkPouches.validate()) {
                            checkPouches.execute();
                            essRemoved = 0;
                            continue;
                        }
                    }
                } else {
                    Data.gui.setStatus("Withdrawing ess...");
                    BankUtil.open();
                    if (Bank.containsAnyOf(Data.PURE_ESS)) {
                        if (preset) {
                            NatRunnerBankUtil.preset();
                            continue;
                        } else NatRunnerBankUtil.withdrawAll(Data.NATURE_RUNE_ID);
                    } else break;
                }
            }
            essRemoved = 0;
            actions.remove(0);
        }
        if (preset)
            NatRunnerBankUtil.preset();
        else NatRunnerBankUtil.withdrawAll(Data.NATURE_RUNE_ID);
    }

    @Override
    public boolean validate() {
        return Util.isNearBank() && Inventory.getEmptySlots() > 1;
    }

    public void addToEssRemoved() {
        essRemoved++;
    }

    public void addToEssAdded() {
        essAdded++;
    }

    public void setPreset() {
        switch (Data.gui.getPreset()) {
            case NONE:
                preset = false;
                break;
            default:
                preset = true;
                break;
        }
    }

    public boolean getPreset() {
        return preset;
    }

    public void bank() {
        BankUtil.open();
        if (Bank.containsAnyOf(Data.PURE_ESS)) {
            if (preset) {
                NatRunnerBankUtil.preset();
            } else NatRunnerBankUtil.withdrawAll(Data.NATURE_RUNE_ID);
        } else {
            Data.gui.setStatus("Out of essence.");
            Execution.delay(1000);
            Data.runner.stop();
        }
    }
}
