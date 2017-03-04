package com.raymondbl.runemate.utilities;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public abstract class GUI extends JFrame {
    protected JLabel title;
    protected JLabel status;
    @Deprecated
    protected JLabel statusLabel;
    protected JLabel runtime;
    protected Font font = new Font("Times New Roman", 0, 14);

    public GUI() {
        setLayout(new GridBagLayout());
        setSize(300, 200);
        initComponents();
    }

    protected abstract void initComponents();

    protected void initJLabel(JLabel label, Font font, String text,
                              Constraints constraints) {
        label.setFont(font);
        label.setText(text);
        add(label, constraints);
    }

    protected void initJButton(JButton button, Font font, String text,
                               String command, Constraints constraints) {
        button.setFont(font);
        button.setText(text);
        button.setActionCommand(command);
        add(button, constraints);
    }

    protected void initJCheckBox(JCheckBox checkBox, Font font,
                                 String text, Constraints constraints) {
        checkBox.setFont(font);
        checkBox.setText(text);
        add(checkBox, constraints);
    }

    protected void initJTextField(JTextField textField, Font font,
                                  String text, Constraints constraints) {
        textField.setFont(font);
        textField.setText(text);
        textField.setMinimumSize(new Dimension(50, 18));
        textField.setPreferredSize(new Dimension(50, 18));
        textField.setSize(new Dimension(50, 18));
        add(textField, constraints);
    }

    protected void initJRadioButton(JRadioButton radioButton, Font font,
                                    String text, ButtonGroup group, Constraints constraints) {
        radioButton.setFont(font);
        radioButton.setText(text);
        add(radioButton, constraints);
        group.add(radioButton);
    }

    protected void initJComboBox(JComboBox<String> comboBox, Font font,
                                 Constraints constraints) {
        comboBox.setFont(font);
        add(comboBox, constraints);
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }
}
