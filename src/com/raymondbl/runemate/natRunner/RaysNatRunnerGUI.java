package com.raymondbl.runemate.natRunner;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.raymondbl.runemate.natRunner.Data.Teleport;
import com.raymondbl.runemate.natRunner.Data.Preset;
import com.raymondbl.runemate.natRunner.Data.Repair;
import com.raymondbl.runemate.utilities.Constraints;
import com.raymondbl.runemate.utilities.GUI;

@SuppressWarnings("serial")
public class RaysNatRunnerGUI extends GUI {
    private JLabel runs;
    private JLabel runsLabel;
    private JLabel runsHour;
    private JLabel runsHourLabel;
    private JLabel crafted;
    private JLabel craftedLabel;
    private JLabel craftedHour;
    private JLabel craftedHourLabel;
    private JLabel exp;
    private JLabel expLabel;
    private JLabel expHour;
    private JLabel expHourLabel;
    private JLabel profit;
    private JLabel profitLabel;
    private JLabel profitHour;
    private JLabel profitHourLabel;
    private JRadioButton npcContact;
    private JRadioButton guild;
    private JRadioButton preset1;
    private JRadioButton preset2;
    private JLabel bankLabel;
    private JRadioButton duel;
    private JRadioButton kinship;
    private JRadioButton tokkulZo;
    private ButtonGroup repairGroup;
    private ButtonGroup presetGroup;
    private ButtonGroup bankGroup;
    private JComboBox<String> teleport;
    private JCheckBox massiveBox;
    private JTextField summoning;
    private JLabel summoningLabel;
    private JButton submit;

    private Teleport bankTeleport = Teleport.DUEL;
    private Repair repair;
    private Preset preset;
    private boolean massive;
    private int summoningThreshold;

    public RaysNatRunnerGUI() {
        super();
        setSize(420, 550);
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        initJLabel(title, new Font("Times New Roman", 1, 18),
                "Ray's Nature Altar Runner", new Constraints()
                        .setGrid(0, 0).setWidth(2).setAnchor(Constraints.CENTER));

        status = new JLabel();
        initJLabel(status, font, "Awaiting input...", new Constraints().setGrid(0, 1));

        runtime = new JLabel();
        initJLabel(runtime, font, "00:00:00", new Constraints().setGrid(1, 1));

        runsLabel = new JLabel();
        initJLabel(runsLabel, font, "Runs:", new Constraints().setGrid(0, 2));

        runs = new JLabel();
        initJLabel(runs, font, "0 runs", new Constraints().setGrid(1, 2));

        runsHourLabel = new JLabel();
        initJLabel(runsHourLabel, font, "Runs per hour:",
                new Constraints().setGrid(0, 3));

        runsHour = new JLabel();
        initJLabel(runsHour, font, "0 runs/hr", new Constraints().setGrid(1, 3));

        craftedLabel = new JLabel();
        initJLabel(craftedLabel, font, "Nature runes crafted:",
                new Constraints().setGrid(0, 4));

        crafted = new JLabel();
        initJLabel(crafted, font, "0 runes", new Constraints().setGrid(1, 4));

        craftedHourLabel = new JLabel();
        initJLabel(craftedHourLabel, font, "Nature runes crafted per hour:",
                new Constraints().setGrid(0, 5));

        craftedHour = new JLabel();
        initJLabel(craftedHour, font, "0 runes/hr", new Constraints().setGrid(1, 5));

        profitLabel = new JLabel();
        initJLabel(profitLabel, font, "Profit: ",
                new Constraints().setGrid(0, 6));

        profit = new JLabel();
        initJLabel(profit, font, "0 gp", new Constraints().setGrid(1, 6));

        profitHourLabel = new JLabel();
        initJLabel(profitHourLabel, font, "Profit per hour:",
                new Constraints().setGrid(0, 7));

        profitHour = new JLabel();
        initJLabel(profitHour, font, "0 gp/hr", new Constraints().setGrid(1, 7));

        expLabel = new JLabel();
        initJLabel(expLabel, font, "Exp gained:", new Constraints().setGrid(0, 8));

        exp = new JLabel();
        initJLabel(exp, font, "0 xp", new Constraints().setGrid(1, 8));

        expHourLabel = new JLabel();
        initJLabel(expHourLabel, font, "Exp gained per hour:",
                new Constraints().setGrid(0, 9));

        expHour = new JLabel();
        initJLabel(expHour, font, "0 xp/hr", new Constraints().setGrid(1, 9));

        repairGroup = new ButtonGroup();

        npcContact = new JRadioButton();
        initJRadioButton(npcContact, font, "Repair with NPC Contact", repairGroup,
                new Constraints().setGrid(0, 10));

        guild = new JRadioButton();
        initJRadioButton(guild, font, "Repair @ Runecrafting Guild", repairGroup,
                new Constraints().setGrid(1, 10));

        presetGroup = new ButtonGroup();

        preset1 = new JRadioButton();
        initJRadioButton(preset1, font, "Bank with Preset 1", presetGroup,
                new Constraints().setGrid(0, 11));

        preset2 = new JRadioButton();
        initJRadioButton(preset2, font, "Bank with Preset 2", presetGroup,
                new Constraints().setGrid(1, 11));

        bankLabel = new JLabel();
        initJLabel(bankLabel, font, "Teleport to bank with:",
                new Constraints().setGrid(0, 12));

        bankGroup = new ButtonGroup();

        duel = new JRadioButton();
        initJRadioButton(duel, font, "Ring of Dueling", bankGroup,
                new Constraints().setGrid(0, 13));

        kinship = new JRadioButton();
        initJRadioButton(kinship, font, "Ring of Kinship", bankGroup,
                new Constraints().setGrid(1, 13));

        tokkulZo = new JRadioButton();
        initJRadioButton(tokkulZo, font, "tokkulZo", bankGroup,
                new Constraints().setGrid(0, 14));

        teleport = new JComboBox<String>(Data.TELEPORTOPTIONS);
        initJComboBox(teleport, font, new Constraints().setGrid(1, 14));

        massiveBox = new JCheckBox();
        initJCheckBox(massiveBox, font, "Use Massive pouch", new Constraints().setGrid(1, 15));

        summoningLabel = new JLabel();
        initJLabel(summoningLabel, font, "renew summoning @",
                new Constraints().setGrid(0, 16));

        summoning = new JTextField();
        initJTextField(summoning, font, "", new Constraints().setGrid(1, 16));

        submit = new JButton();
        initJButton(submit, font, "Run", "submit", new Constraints().setGrid(0, 17)
                .setWidth(2).setAnchor(Constraints.CENTER));

    }

    public void process() {
        if (npcContact.isSelected())
            setRepair(Repair.NPC_CONTACT);
        else if (guild.isSelected())
            setRepair(Repair.GUILD);
        else setRepair(Repair.NONE);
        if (preset1.isSelected())
            setPreset(Preset.PRESET1);
        else if (preset2.isSelected())
            setPreset(Preset.PRESET2);
        else setPreset(Preset.NONE);
        if (duel.isSelected())
            setTeleport(Teleport.DUEL);
        else if (kinship.isSelected())
            setTeleport(Teleport.KIN);
        else if (tokkulZo.isSelected()) {
            int index = teleport.getSelectedIndex();
            switch (index) {
                case 0:
                    setBankTeleport(Teleport.PIT);
                    break;
                case 1:
                    setBankTeleport(Teleport.CAVE);
                    break;
                case 2:
                    setBankTeleport(Teleport.KILN);
                    break;
                case 3:
                    setBankTeleport(Teleport.CAULDRON);
                    break;
            }
        }
        if (massiveBox.isSelected())
            massive = true;
        summoningThreshold = Integer.parseInt(summoning.getText());
    }

    public Teleport getBankTeleport() {
        return bankTeleport;
    }

    public void setBankTeleport(Teleport b) {
        this.bankTeleport = b;
    }

    public boolean isMassiveSelected() {
        return massive;
    }

    public void setMassive(boolean b) {
        massive = b;
    }

    public Repair getRepair() {
        return repair;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }

    public Preset getPreset() {
        return preset;
    }

    public void setPreset(Preset preset) {
        this.preset = preset;
    }

    public Teleport getTeleport() {
        return bankTeleport;
    }

    public void setTeleport(Teleport teleport) {
        bankTeleport = teleport;
    }

    public int getSummoningThreshold() {
        return summoningThreshold;
    }

    public void setSummoningThreshold(int threshold) {
        this.summoningThreshold = threshold;
    }

    public void setStatus(String text) {
        System.out.println(text);
        status.setText(text);
    }

    public void setRuntime(String text) {
        runtime.setText(text);
    }

    public void setRuns(Integer i) {
        runs.setText(i.toString() + " runs");
    }

    public void setRunsHour(Integer i) {
        runsHour.setText(i.toString() + " runs/hr");
    }

    public void setCrafted(Integer i) {
        crafted.setText(i.toString() + " runes");
    }

    public void setCraftedHour(Integer i) {
        craftedHour.setText(i.toString() + " runes/hr");
    }

    public void setExp(Integer i) {
        exp.setText(i.toString() + " xp");
    }

    public void setExpHour(Integer i) {
        expHour.setText(i.toString() + " xp/hr");
    }

    public void setProfit(Integer i) {
        profit.setText(i.toString() + " gp");
    }

    public void setProfitHour(Integer i) {
        profitHour.setText(i.toString() + " gp/hr");
    }

    public void setListener(RaysNatRunner runner) {
        submit.addActionListener(runner);
    }

}
