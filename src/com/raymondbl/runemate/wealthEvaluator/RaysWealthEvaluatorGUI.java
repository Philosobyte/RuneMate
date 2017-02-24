package com.raymondbl.runemate.wealthEvaluator;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.raymondbl.runemate.utilities.Constraints;
import com.raymondbl.runemate.utilities.GUI;

@SuppressWarnings("serial")
public final class RaysWealthEvaluatorGUI extends GUI
{
	private JLabel runtime;
	private JLabel totalLabel;
	private JLabel totalWealthLabel;
	private JLabel totalWealth;
	private JLabel totalCountLabel;
	private JLabel totalCount;
	private JLabel bankLabel;
	private JLabel bankWealthLabel;
	private JLabel bankWealth;
	private JLabel bankCountLabel;
	private JLabel bankCount;
	private JLabel invLabel;
	private JLabel invWealthLabel;
	private JLabel invWealth;
	private JLabel invCountLabel;
	private JLabel invCount;
	
	private JButton submit;
	private JCheckBox bank;
	private JCheckBox inventory;
	
	public RaysWealthEvaluatorGUI()
	{
		super();
		setSize(300, 400);
	}
	
	@Override
	protected void initComponents()
	{
		title = new JLabel();
		initJLabel(title, new Font("Times New Roman", 1, 18), "Ray's Wealth Evaluator",
				new Constraints().setGrid(0, 0).setWidth(2).setAnchor(Constraints.CENTER));
		
		runtime = new JLabel();
		initJLabel(runtime, font, "", new Constraints().setGrid(0, 1));
		
		status = new JLabel();
		initJLabel(status, font, "", 
				new Constraints().setGrid(1, 1));
		
		bank = new JCheckBox();
		initJCheckBox(bank, font, "include Bank", 
				new Constraints().setGrid(0, 2));
		
		inventory = new JCheckBox();
		initJCheckBox(inventory, font, "include Inventory", 
				new Constraints().setGrid(1, 2));
		
		submit = new JButton();
		initJButton(submit, font, "Run", "Submit",
				new Constraints().setGrid(0, 3).setWidth(2).setAnchor(Constraints.CENTER));
		
		totalLabel = new JLabel();
		initJLabel(totalLabel, new Font("Times New Roman", 1, 14), "Total", 
				new Constraints().setGrid(0, 4).setWidth(2).setAnchor(Constraints.CENTER));
		
		totalWealthLabel = new JLabel();
		initJLabel(totalWealthLabel, font, "Wealth:", 
				new Constraints().setGrid(0, 5));
		
		totalWealth = new JLabel();
		initJLabel(totalWealth, font, "", new Constraints().setGrid(1, 5));
		
		totalCountLabel = new JLabel();
		initJLabel(totalCountLabel, font, "Tradeable items evaluated:", 
				new Constraints().setGrid(0, 6));
		
		totalCount = new JLabel();
		initJLabel(totalCount, font, "", new Constraints().setGrid(1, 6));
		
		bankLabel = new JLabel();
		initJLabel(bankLabel, new Font("Times New Roman", 1, 14), "Bank", 
				new Constraints().setGrid(0, 7).setWidth(2).setAnchor(Constraints.CENTER));
		
		bankWealthLabel = new JLabel();
		initJLabel(bankWealthLabel, font, "Wealth:", 
				new Constraints().setGrid(0, 8));
		
		bankWealth = new JLabel();
		initJLabel(bankWealth, font, "", new Constraints().setGrid(1, 8));
		
		bankCountLabel = new JLabel();
		initJLabel(bankCountLabel, font, "Tradeable items evaluated:", 
				new Constraints().setGrid(0, 9));
		
		bankCount = new JLabel();
		initJLabel(bankCount, font, "", new Constraints().setGrid(1, 9));
		
		invLabel = new JLabel();
		initJLabel(invLabel, new Font("Times New Roman", 1, 14), "Inventory", 
				new Constraints().setGrid(0, 10).setWidth(2).setAnchor(Constraints.CENTER));
		
		invWealthLabel = new JLabel();
		initJLabel(invWealthLabel, font, "Wealth:", 
				new Constraints().setGrid(0, 11));
		
		invWealth = new JLabel();
		initJLabel(invWealth, font, "", new Constraints().setGrid(1, 11));
		
		invCountLabel = new JLabel();
		initJLabel(invCountLabel, font, "Tradeable items evaluated:", 
				new Constraints().setGrid(0, 12));
		
		invCount = new JLabel();
		initJLabel(invCount, font, "", new Constraints().setGrid(1, 12));
	}
	
	public void setButtonListener(RaysWealthEvaluator r)
	{
		submit.addActionListener(r);
	}
	
	public void setCount(Integer count, Type type)
	{
		switch(type)
		{
		case TOTAL	: 	this.totalCount.setText(count.toString());
						break;
		case BANK	: 	this.bankCount.setText(count.toString());
						break;
		case INVEN	: 	this.invCount.setText(count.toString());
						break;
		}
	}
	
	public void setWealth(Integer wealth, Type type)
	{
		switch(type)
		{
		case TOTAL	: 	this.totalWealth.setText(wealth.toString() + " gp");
						break;
		case BANK	: 	this.bankWealth.setText(wealth.toString() + " gp");
						break;
		case INVEN	: 	this.invWealth.setText(wealth.toString() + " gp");
						break;
		}
	}
	
	public void setRunTime(String runtime)
	{
		this.runtime.setText(runtime);
	}
	public void setStatus(String status)
	{
		this.status.setText(status);
	}
	
	public boolean isBankSelected()
	{
		return bank.isSelected();
	}
	
	public boolean isInvSelected()
	{
		return inventory.isSelected();
	}
	
	enum Type
	{
		TOTAL, BANK, INVEN
	}
}
