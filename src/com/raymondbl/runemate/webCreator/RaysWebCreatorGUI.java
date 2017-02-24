package com.raymondbl.runemate.webCreator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.raymondbl.runemate.utilities.Constraints;
import com.raymondbl.runemate.utilities.GUI;

@SuppressWarnings("serial")
public class RaysWebCreatorGUI extends GUI
{
	private JLabel status;
	private JLabel radiusLabel;
	private JTextField radius;
	private JButton add;
	private JButton finish;
	private JButton reset;
	
	@Override
	public void initComponents() 
	{
		title = new JLabel();
		initJLabel(title, font, "Ray's Web Creator", 
				new Constraints().setGrid(0, 0).setAnchor(Constraints.CENTER).setWidth(2));
		
		status = new JLabel();
		initJLabel(status, font, "", new Constraints().setGrid(0, 1));
		
		runtime = new JLabel();
		initJLabel(runtime, font, "", new Constraints().setGrid(1, 1));
		
		radiusLabel = new JLabel();
		initJLabel(radiusLabel, font, "Radius:",
				new Constraints().setGrid(0, 2));
		
		radius = new JTextField();
		initJTextField(radius, font, "Enter radius", new Constraints().setGrid(1, 2));
		
		add = new JButton();
		initJButton(add, font, "Add Area", "addArea", 
				new Constraints().setGrid(0, 3));
		
		finish = new JButton();
		initJButton(finish, font, "Generate Code", "gen", 
				new Constraints().setGrid(1, 3));
		
		reset = new JButton();
		initJButton(reset, font, "Reset", "reset", new Constraints()
				.setGrid(0, 4).setAnchor(Constraints.CENTER).setWidth(2));
		
	}
	
	public void setListener(RaysWebCreator r)
	{
		add.addActionListener(r);
		finish.addActionListener(r);
		reset.addActionListener(r);

	}
	public void setStatus(String status)
	{
		this.status.setText(status);
	}
	
	public void setRuntime(String runtime)
	{
		this.runtime.setText(runtime);
	}
	
	public String getRadius()
	{
		return radius.getText();
	}
}
