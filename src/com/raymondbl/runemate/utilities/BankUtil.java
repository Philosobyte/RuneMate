package com.raymondbl.runemate.utilities;

import com.raymondbl.runemate.natRunner.Data;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.script.Execution;

public abstract class BankUtil {
    public static void open() {
        if (validateOpen()) {
            GameObject bank = Util.firstLoaded("Bank chest");
            Util.getIntoView(bank);
            if (Bank.open()) {
                if (Execution.delayUntil(() -> !validateOpen(),
                        () -> Data.player.isMoving(), 1600, 2400)) ;
                else
                    open();
            } else
                open();
        }
    }

    private static boolean validateOpen() {
        return !Bank.isOpen();
    }

    public static void close() {
        if (validateClose()) {
            if (Bank.close()) {
                if (Execution.delayUntil(() -> !validateClose(), 1200, 2400)) ;
                else if (validateClose())
                    close();
            } else if (validateClose())
                close();
        }
    }

    private static boolean validateClose() {
        return Bank.isOpen();
    }

    public static void withdraw(String name, int quantity) {
        if (validateWithdraw(name)) {
            open();
            if (Bank.containsAnyOf(name)) {
                if (Bank.withdraw(name, quantity)) {
                    if (Execution.delayUntil(() -> Util.isCarried(name),
                            () -> Data.player.isMoving(), 1200, 2400)) ;
                    else withdraw(name, quantity);
                } else withdraw(name, quantity);
            } else {
                Data.gui.setStatus("Run out of " + name + "s.");
                Execution.delay(1000);
                Data.runner.stop();
                Execution.delay(1000);
                return;
            }
        }
    }

    private static boolean validateWithdraw(String name) {
        return !Util.isCarried(name);
    }

    public static SpriteItem get(int... ids) {
        return Bank.getItems(new Filter<SpriteItem>() {
            @Override
            public boolean accepts(SpriteItem item) {
                for (int id : ids) {
                    if (item.getId() == id)
                        return true;
                }
                return false;
            }
        }).first();
    }

    public static void withdraw(int... ids) {
        if (validateWithdraw(ids)) {
            open();
            if (Bank.containsAnyOf(ids)) {
                if (Bank.withdraw(get(ids).getId(), 1)) {
                    if (Execution.delayUntil(() -> Util.isCarried(ids),
                            () -> Data.player.isMoving(), 1200, 2400)) ;
                    else withdraw(ids);
                } else withdraw(ids);
            } else {
                Data.gui.setStatus("Run out of resources.");
                Execution.delay(1000);
                Data.runner.stop();
                Execution.delay(1000);
                return;
            }
        }
    }

    private static boolean validateWithdraw(int... ids) {
        return !Util.isCarried(ids);
    }

    public static void deposit(int id) {
        if (validateDeposit()) {
            BankUtil.open();
            if (Bank.deposit(id, 1)) {
                if (Execution.delayUntil(() -> !validateDeposit(), 1200, 2400)) ;
                else deposit(id);
            } else deposit(id);
        }
    }

    private static boolean validateDeposit() {
        return Inventory.isFull();
    }

    public static void wear(int... ids) {
        if (validateWear(ids)) {
            if (Bank.getItems(ids).first().interact("Wear")) {
                if (Execution.delayUntil(() -> Util.isWorn(ids), 1200, 2400)) ;
                else if (validateWear(ids))
                    wear(ids);
            } else if (validateWear(ids))
                wear(ids);
        } else if (validateWear(ids))
            wear(ids);
    }

    private static boolean validateWear(int... ids) {
        return Bank.containsAnyOf(ids) && !Util.isWorn(ids);
    }
}
