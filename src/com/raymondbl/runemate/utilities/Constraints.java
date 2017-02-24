package com.raymondbl.runemate.utilities;

import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class Constraints extends GridBagConstraints 
{
	public Constraints() 
	{
		setGrid(3, 3);
		setInsets(3, 3, 3, 3);
		setAnchor(LINE_START);
		weightx = 0.5;
		weighty = 0.5;
	}
	
	public Constraints setGridx(int gridx)
	{
		this.gridx = gridx;
		return this;
	}
	
	public Constraints setGridy(int gridy)
	{
		this.gridy = gridy;
		return this;
	}
	
	public Constraints setGrid(int gridx, int gridy)
	{
		return setGridx(gridx).setGridy(gridy);
	}
	
	public Constraints setWidth(int gridwidth)
	{
		this.gridwidth = gridwidth;
		return this;
	}
	
	public Constraints setHeight(int gridheight)
	{
		this.gridheight = gridheight;
		return this;
	}
	
	public Constraints setWidthHeight(int gridwidth,int gridheight)
	{
		return setWidth(gridwidth).setHeight(gridheight);
	}
	
	public Constraints setFill(int fill)
	{
		this.fill = fill;
		return this;
	}
	
	public Constraints setIpadx(int ipadx)
	{
		this.ipadx = ipadx;
		return this;
	}
	
	public Constraints setIpady(int ipady)
	{
		this.ipady = ipady;
		return this;
	}
	
	public Constraints setIpad(int ipadx, int ipady)
	{
		return setIpadx(ipadx).setIpady(ipady);
	}

	public Constraints setInsets(int top, int left, int bottom, int right)
	{
		insets = new Insets(top,left, bottom, right);
		return this;
	}
	
	public Constraints setAnchor(int anchor)
	{
		this.anchor = anchor;
		return this;
	}
}
