package com.raymondbl.runemate.wealthEvaluator;

import com.raymondbl.runemate.wealthEvaluator.RaysWealthEvaluatorGUI.Type;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.rs3.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public final class RaysWealthEvaluator extends LoopingScript implements
        PaintListener, ActionListener {
    private static final Filter<SpriteItem> FILTER =
            new Filter<SpriteItem>() {
                public boolean accepts(SpriteItem spriteItem) {
                    return spriteItem.getDefinition().isTradeable();
                }
            };
    private int totalWealth;
    private int totalCount;
    private int bankWealth;
    private int bankCount;
    private int invWealth;
    private int invCount;
    private boolean condition;
    private Type phase;
    private RaysWealthEvaluatorGUI gui;
    private StopWatch runtime = new StopWatch();

    @Override
    public void onStart(String... args) {
        getEventDispatcher().addListener(this);
        setLoopDelay(800, 1600);
        gui = new RaysWealthEvaluatorGUI();
        gui.setButtonListener(this);
        gui.setVisible(true);
        runtime.start();
    }

    @Override
    public void onLoop() {
        Execution.delayUntil(() -> condition);
        reset();
        if (gui.isBankSelected()) {
            phase = Type.BANK;
            calculateBank();
        }
        if (gui.isInvSelected()) {
            phase = Type.INVEN;
            calculateInv();
        }
        gui.setStatus("Finished.");
        Execution.delay(1000);
    }

    public void calculateBank() {
        if (!Bank.isOpen()) {
            gui.setStatus("Opening bank");
            Execution.delayUntil(() -> Bank.open());
        }

        gui.setStatus("Loading bank...");
        List<SpriteItem> list = Bank.newQuery().filter(FILTER)
                .results().asList();
        gui.setStatus("Calculating bank...");
        calculateWealth(list, Type.BANK);
    }

    public void calculateInv() {
        if (Bank.isOpen()) {
            gui.setStatus("Closing bank...");
            Execution.delayUntil(() -> Bank.close());
        }

        gui.setStatus("Loading inventory...");
        List<SpriteItem> list = Inventory.newQuery().filter(FILTER)
                .results().asList();
        gui.setStatus("Calculating inventory");
        calculateWealth(list, Type.INVEN);
    }

    public void calculateWealth(List<SpriteItem> list, Type type) {
        int wealth = 0;
        for (SpriteItem s : list) {
            wealth = GrandExchange.lookup(s.getId()).getPrice() * s.getQuantity();
            totalWealth += wealth;
            totalCount++;
            switch (type) {
                case BANK:
                    bankWealth += wealth;
                    bankCount++;
                    break;
                case INVEN:
                    invWealth += wealth;
                    invCount++;
                    break;
                default:
                    break;
            }
        }

    }

    public void reset() {
        totalWealth = 0;
        totalCount = 0;
        bankWealth = 0;
        bankCount = 0;
        invWealth = 0;
        invCount = 0;
        condition = false;
        phase = null;

        gui.setWealth(0, Type.TOTAL);
        gui.setWealth(0, Type.BANK);
        gui.setWealth(0, Type.INVEN);
        gui.setCount(0, Type.TOTAL);
        gui.setCount(0, Type.BANK);
        gui.setCount(0, Type.INVEN);
    }

    @Override
    public void onPaint(Graphics2D g) {
        if (gui != null) {
            gui.setRunTime(runtime.getRuntimeAsString());
            if (phase != null) {
                gui.setWealth(totalWealth, Type.TOTAL);
                gui.setCount(totalCount, Type.TOTAL);
                switch (phase) {
                    case BANK:
                        gui.setWealth(bankWealth, phase);
                        gui.setCount(bankCount, phase);
                        break;
                    case INVEN:
                        gui.setWealth(invWealth, phase);
                        gui.setCount(invCount, phase);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Submit"))
            condition = true;
    }

}
