/**
 * Ray's Headless Arrows (GUI)
 *
 * @author Raymondbl
 * @version 0.50
 * 12/22/2014
 */
package com.raymondbl.runemate.headlessArrows;

import com.raymondbl.runemate.utilities.Constraints;
import com.raymondbl.runemate.utilities.GUI;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class RaysHeadlessArrowsGUI extends GUI {
    private JLabel runtime;
    private JLabel headlessArrows;
    private JLabel headlessArrowsLabel;
    private JLabel headlessArrowsPerHr;
    private JLabel headlessArrowsPerHrLabel;
    private JLabel expGained;
    private JLabel expGainedLabel;
    private JLabel expPerHr;
    private JLabel expPerHrLabel;
    private JLabel profit;
    private JLabel profitLabel;
    private JLabel profitHr;
    private JLabel profitHrLabel;

    public RaysHeadlessArrowsGUI() {
        super();
        setSize(300, 280);
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        initJLabel(title, new Font("Times New Roman", 1, 18),
                "Ray's Headless Arrows", new Constraints()
                        .setGrid(0, 0).setWidth(2).setAnchor(Constraints.CENTER));

        status = new JLabel();
        initJLabel(status, font, "", new Constraints().setGrid(0, 1));

        runtime = new JLabel();
        initJLabel(runtime, font, "", new Constraints().setGrid(1, 1));

        headlessArrowsLabel = new JLabel();
        initJLabel(headlessArrowsLabel, font, "Headless Arrows crafted:",
                new Constraints().setGrid(0, 2));

        headlessArrows = new JLabel();
        initJLabel(headlessArrows, font, "0", new Constraints().setGrid(1, 2));

        headlessArrowsPerHrLabel = new JLabel();
        initJLabel(headlessArrowsPerHrLabel, font, "Headless Arrows/hr:",
                new Constraints().setGrid(0, 3));

        headlessArrowsPerHr = new JLabel();
        initJLabel(headlessArrowsPerHr, font, "0", new Constraints().setGrid(1, 3));

        expPerHrLabel = new JLabel();
        initJLabel(expPerHrLabel, font, "xp/hr:", new Constraints().setGrid(0, 4));

        expPerHr = new JLabel();
        initJLabel(expPerHr, font, "0", new Constraints().setGrid(1, 4));

        expGainedLabel = new JLabel();
        initJLabel(expGainedLabel, font, "Exp gained:",
                new Constraints().setGrid(0, 5));

        expGained = new JLabel();
        initJLabel(expGained, font, "0", new Constraints().setGrid(1, 5));

        profitLabel = new JLabel();
        initJLabel(profitLabel, font, "Profit: ",
                new Constraints().setGrid(0, 6));

        profit = new JLabel();
        initJLabel(profit, font, "0", new Constraints().setGrid(1, 6));

        profitHrLabel = new JLabel();
        initJLabel(profitHrLabel, font, "Profit per hour: ",
                new Constraints().setGrid(0, 7));

        profitHr = new JLabel();
        initJLabel(profitHr, font, "0", new Constraints().setGrid(1, 7));
    }

    public void setHeadlessArrows(Integer headlessArrows) {
        this.headlessArrows.setText(headlessArrows.toString());
    }

    public void setHeadlessArrowsPerHr(Integer headlessArrowsPerHr) {
        this.headlessArrowsPerHr.setText(headlessArrowsPerHr.toString() + " /hr");
    }

    public void setExp(Integer expGained) {
        this.expGained.setText(expGained.toString() + " xp");
    }

    public void setExpPerHr(Integer expPerHr) {
        this.expPerHr.setText(expPerHr.toString() + " xp/hr");
    }

    public void setRunTime(String runtime) {
        this.runtime.setText(runtime);
    }

    public void setStatus(String status) {
        this.status.setText(status);
        System.out.println(status);
    }

    public void setProfit(Integer profit) {
        this.profit.setText(profit.toString() + " gp");
    }

    public void setProfitHr(Integer profitHr) {
        this.profitHr.setText(profitHr.toString() + " gp/hr");
    }

}
