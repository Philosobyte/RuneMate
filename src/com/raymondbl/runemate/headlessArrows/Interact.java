package com.raymondbl.runemate.headlessArrows;

import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Interact extends Task {
    private static final String ARROW_SHAFT = "Arrow shaft";
    private static final String FEATHER = "Feather";
    private RaysHeadlessArrowsGUI gui;
    private RaysHeadlessArrows main;
    private SpriteItem arrowShafts;
    private InterfaceComponent popup;

    @Override
    public void execute() {
        double random = Random.nextDouble(0, 1);
        if (random > 0.994) {
            Execution.delay((int) Random.nextGaussian(20893, 82749));
        } else if (random > 0.982) {
            Execution.delay((int) Random.nextGaussian(3781, 9258));
        }
        gui.setStatus("Clicking...");
        getSpriteItems();
        if (arrowShafts.interact("Feather")) {
            System.out.println("Interact:26");
            if (Execution.delayUntil(() ->
                    (popup = Util.getInterface("Add Feather")) != null
                            && popup.isVisible(), 2600, 5200)) ;
            else if (validate())
                execute();
        }
    }

    @Override
    public boolean validate() {
        InterfaceComponent component = Util.getInterface("Add Feather");
        return component == null || !component.isVisible();
    }

    public void setGUI(RaysHeadlessArrowsGUI gui) {
        this.gui = gui;
    }

    public void setMain(RaysHeadlessArrows main) {
        this.main = main;
    }

    public void getSpriteItems() {
        if (Inventory.containsAllOf(ARROW_SHAFT, FEATHER)) {
            arrowShafts = Inventory.getItems(ARROW_SHAFT).first();
            System.out.println("Interact:57");
        } else {
            gui.setStatus("Out of resources");
            Execution.delay(1000);
            main.stop();
        }

    }

}
