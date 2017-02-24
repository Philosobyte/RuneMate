/** Ray's Headless Arrows
 * @author Raymondbl
 * @version 0.50
 * 12/22/2014
 */
package com.raymondbl.runemate.headlessArrows;

import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.CommonMath;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.SkillListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;
import com.runemate.game.api.script.framework.task.TaskScript;

public class RaysHeadlessArrows extends TaskScript implements  
								PaintListener, InventoryListener, SkillListener
{
	private RaysHeadlessArrowsGUI gui;
	private StopWatch runtime = new StopWatch();
	private InterfaceComponent popup;
	private int headlessArrows;
	private int expGain;
	private int count;
	private int featherPrice;
	private int shaftPrice;
	private int hArrowPrice;
	private int priceDifference;
	private int totalProfit;
	private Interact interact;
	private ProcessInterface processInterface;
	private static final int ARROW_SHAFT_ID = 52;
	private static final int HEADLESS_ARROW_ID = 53;
	private static final int FEATHER_ID = 314;
	
	@Override
	public void onStart(String... args)
	{
		getEventDispatcher().addListener(this);
		interact = new Interact();
		processInterface = new ProcessInterface();
		add(interact, processInterface);
		gui = new RaysHeadlessArrowsGUI();
		gui.setVisible(true);
		gui.setStatus("initializing...");
		interact.setGUI(gui);
		processInterface.setGUI(gui);
		interact.setMain(this);
		setPrices();
		runtime.start();
		setLoopDelay(200, 400);
	}
	
	public boolean getPopup()
	{	
		if(Execution.delayUntil(() -> 
			(popup = Util.getInterface("Add feather")) != null 
			&& popup.isVisible(), 2200, 4300))
			return true;
		else return false;
	}
	
	public void pressSpace()
	{
		Keyboard.typeKey(" ");
		gui.setStatus("Waiting...");
		Execution.delayUntil(() -> count == 10, 15000);
		if(Random.nextDouble(0, 1) > 0.998)
		{
			Execution.delay((long)Random.nextGaussian(20700, 97830, 42740));		
		}
		resetCount();
	}
	
	public void resetCount()
	{
		count = 0;
	}
	
	@Override
	public void onItemAdded(ItemEvent event)
	{
		if(event.getItem().getId() == HEADLESS_ARROW_ID)
		{
			headlessArrows += event.getQuantityChange();
			totalProfit = headlessArrows * priceDifference;
			if(gui != null)
			{
				gui.setHeadlessArrows(headlessArrows);
				gui.setProfit(totalProfit);
			}
			count++;
		}
	}
	
	@Override
	public void onExperienceGained(SkillEvent event)
	{
		if(event.getType().equals(SkillEvent.Type.EXPERIENCE_GAINED) && 
				event.getSkill().equals(Skill.FLETCHING))
		expGain += event.getChange();
		if(gui != null)
		gui.setExp(expGain);
	}
	
	public void setPrices()
	{
		shaftPrice = GrandExchange.lookup(ARROW_SHAFT_ID).getPrice();
		featherPrice = GrandExchange.lookup(FEATHER_ID).getPrice();
		hArrowPrice = GrandExchange.lookup(HEADLESS_ARROW_ID).getPrice();
		priceDifference = hArrowPrice - featherPrice - shaftPrice;
		System.out.println(shaftPrice);
		System.out.println(priceDifference);
	}
	@Override
	public void onPaint(Graphics2D arg0) 
	{
		if(gui != null)
		{
		gui.setRunTime(runtime.getRuntimeAsString());
		gui.setHeadlessArrowsPerHr((int)CommonMath.rate(TimeUnit.HOURS, runtime.getRuntime(), headlessArrows));
		gui.setExpPerHr((int)CommonMath.rate(TimeUnit.HOURS, runtime.getRuntime(), expGain));
		gui.setProfitHr((int)CommonMath.rate(TimeUnit.HOURS, runtime.getRuntime(), totalProfit));
		}
	}
}
