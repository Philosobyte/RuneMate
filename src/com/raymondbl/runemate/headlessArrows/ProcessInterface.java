package com.raymondbl.runemate.headlessArrows;

import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class ProcessInterface extends Task {
    private static final int ADD_FEATHER_TEXTURE_ID = 13815;
    private RaysHeadlessArrowsGUI gui;

    @Override
    public void execute() {
        gui.setStatus("Presing space");
        if (Util.processInterface(ADD_FEATHER_TEXTURE_ID)) {
            gui.setStatus("Waiting...");
            if (Execution.delayUntil(() -> Util.getInterface("CANCEL") != null, 2400, 4800)) {
                Execution.delayWhile(() -> Util.getInterface("CANCEL") != null);
            }
        } else return;
    }

    public void setGUI(RaysHeadlessArrowsGUI gui) {
        this.gui = gui;
    }

    @Override
    public boolean validate() {
        InterfaceComponent component = Util.getInterface("Add Feather");
        return component != null && component.isVisible();
    }

}
