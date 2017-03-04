package com.raymondbl.runemate.natRunner;

import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.local.Varp;
import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;

public class Pouch {

    private int id;
    private int capacity;
    private int count;
    private boolean lastRepaired;
    private String name;
    private Pouch degradedVariant;
    private Varp varp;

    public Pouch(int id) {
        this.id = id;
        switch (id) {
            case 5509:
                capacity = 3;
                name = "Small pouch";
                degradedVariant = this;
                break;
            case 5510:
                capacity = 6;
                name = "Medium pouch";
                degradedVariant = new Pouch(5511);
                break;
            case 5512:
                capacity = 9;
                name = "Large pouch";
                degradedVariant = new Pouch(5513);
                break;
            case 5514:
                capacity = 12;
                name = "Giant pouch";
                degradedVariant = new Pouch(5515);
                break;
            case 24204:
                capacity = 18;
                name = "Massive pouch";
                degradedVariant = new Pouch(24205);
                break;
            case 5511:
                capacity = 5;
                name = "Medium pouch";
                degradedVariant = this;
                break;
            case 5513:
                capacity = 7;
                name = "Large pouch";
                degradedVariant = this;
                break;
            case 5515:
                capacity = 8;
                name = "Giant pouch";
                degradedVariant = this;
                break;
            case 24205:
                capacity = 18;
                name = "Massive pouch";
                degradedVariant = this;
                break;
        }
        varp = Varps.getAt(3215);
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isUsed() {
        return !Util.isNull(getSlotAction());
    }

    public SlotAction getSlotAction() {
        return ActionBar.getFirstAction(id);
    }

    public void setLastRepaired(boolean b) {
        lastRepaired = b;
    }

    public boolean isLastRepaired() {
        return lastRepaired;
    }

    public boolean isFilled() {
        String s = Integer.toBinaryString(varp.getBits());
        int i = s.length();
        if (id == 5509 && i >= 2) {
            return process(s.substring(i - 2, i));

        } else if (id == 5510 && i >= 4) {
            return process(s.substring(i - 4, i - 2));

        } else if (id == 5512 && i >= 6) {
            return process(s.substring(i - 6, i - 4));
        } else if (id == 5514 && i >= 8) {
            return process(s.substring(i - 8, i - 6));
        } else if (id == 24204 || id == 24205) {
            count++;
            if (count < 4)
                return false;
            count = 0;
            Inventory.getItems(Data.MASSIVE_POUCH.getId()).first().hover();
            return menuAction();

        } else return false;
    }

    private boolean process(String str) {
        switch (str) {
            case "10":
                return true;
            default:
                return false;
        }
    }

    public String getName() {
        return name;
    }

    public Pouch getDegradedVariant() {
        return degradedVariant;
    }

    private boolean menuAction() {
        String action = Util.getFirstMenuAction();
        if (action.equals("Fill"))
            return false;
        else if (action.equals("Empty"))
            return true;
        else return menuAction();
    }

    public void resetCount() {
        count = 0;
    }
}
