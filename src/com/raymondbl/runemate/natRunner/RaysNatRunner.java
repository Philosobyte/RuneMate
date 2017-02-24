package com.raymondbl.runemate.natRunner;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.raymondbl.runemate.natRunner.Tasks.BankTeleport;
import com.raymondbl.runemate.natRunner.Tasks.CheckFamiliar;
import com.raymondbl.runemate.natRunner.Tasks.CheckPouches;
import com.raymondbl.runemate.natRunner.Tasks.CheckRing;
import com.raymondbl.runemate.natRunner.Tasks.CheckSummoning;
import com.raymondbl.runemate.natRunner.Tasks.Craft;
import com.raymondbl.runemate.natRunner.Tasks.GraahkTeleport;
import com.raymondbl.runemate.natRunner.Tasks.Refill;
import com.raymondbl.runemate.natRunner.Tasks.WalkToAltar;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.location.navigation.web.default_webs.FileWeb;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.CommonMath;
import com.runemate.game.api.rs3.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.SkillListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import com.runemate.game.api.script.framework.listeners.events.SkillEvent;
import com.runemate.game.api.script.framework.task.TaskScript;

public final class RaysNatRunner extends TaskScript implements PaintListener, 
			ChatboxListener, InventoryListener, SkillListener, ActionListener
{
	private RaysNatRunnerGUI gui;
	private StopWatch runtime = new StopWatch();
	private int natureRunes;
	private int runs;
	private int natPrice;
	private int profit;
	private int exp;
	private boolean inputReceived;
	private CheckRing checkRing;
	private CheckPouches checkPouches;
	private CheckFamiliar checkFamiliar;
	private Refill refill;
	private GraahkTeleport graahkTeleport;
	private CheckSummoning checkSummoning;
	private WalkToAltar walkToAltar;
	private Craft craft;
	private BankTeleport bankTeleport;
	
	
	@Override
	public void onStart(String... args)
	{
		setLoopDelay(400, 800);
		checkPrice();
		checkRing = new CheckRing();
		checkPouches = new CheckPouches();
		checkFamiliar = new CheckFamiliar();
		refill = new Refill();
		graahkTeleport = new GraahkTeleport();
		checkSummoning = new CheckSummoning();
		walkToAltar = new WalkToAltar();
		craft = new Craft();
		bankTeleport = new BankTeleport();
		
		add(checkRing, checkPouches, checkFamiliar, refill, graahkTeleport,
				checkSummoning, walkToAltar, craft, bankTeleport);
		gui = new RaysNatRunnerGUI();
		gui.setVisible(true);
		getEventDispatcher().addListener(this);
		gui.setListener(this);
		Execution.delayUntil(() -> inputReceived);
		gui.setStatus("Initializing...");
		Data.initialize(this, gui);
		merge();
		runtime.start();
	}
	
    public  boolean  merge() 
	{
    	try(InputStream  input  =  RaysNatRunner.class.getResourceAsStream("/webbset.bin"))
        {
    		byte[]  obj  =  new  byte[input.available()  +  32];
	        input.read(obj,  0,  obj.length);
	        FileWeb  fileWeb  =  FileWeb.fromByteArray(obj);
	        Data.web.addVertices(fileWeb.getVertices());
	        }  catch  (Exception  e)  {
	        e.printStackTrace();	                }
	        return  true;
	    }
	
	@Override
	public void onItemAdded(ItemEvent e)
	{
		if(e.getItem().getId() == (Data.NATURE_RUNE_ID))
		{
			int i = e.getQuantityChange();
			natureRunes += i;
			profit += natPrice * i;
		}
	}
	
	@Override
	public void onItemRemoved(ItemEvent e)
	{
		if(e.getItem().getId() == (Data.PURE_ESS))
			refill.addToEssRemoved();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("submit"))
		{
			gui.process();
			inputReceived = true;
		}
	}
	
	@Override
	public void onExperienceGained(SkillEvent e)
	{
		if(e.getType() == SkillEvent.Type.EXPERIENCE_GAINED)
		{
			if(e.getSkill() == Skill.RUNECRAFTING)
				exp += e.getChange();
		}
	}
	
	@Override
	public void onMessageReceived(MessageEvent e) 
	{
	}
	
	@Override
	public void onPaint(Graphics2D g2d) 
	{
		if(gui != null)
		{
			gui.setRuntime(runtime.getRuntimeAsString());
			gui.setRuns(runs);
			gui.setRunsHour((int)CommonMath.rate
					(TimeUnit.HOURS, runtime.getRuntime(), runs));
			gui.setCrafted(natureRunes);
			gui.setCraftedHour((int)CommonMath.rate
					(TimeUnit.HOURS, runtime.getRuntime(), natureRunes));
			gui.setExp(exp);
			gui.setExpHour((int)CommonMath.rate
					(TimeUnit.HOURS, runtime.getRuntime(), exp));
			gui.setProfit(profit);
			gui.setProfitHour((int)CommonMath.rate
					(TimeUnit.HOURS, runtime.getRuntime(), profit));
		}
	}
	
	public void addRun()
	{
		runs++;
	}

	public int getNatureRunes()
	{
		return natureRunes;
	}
	
	public BankTeleport getCWarsTeleport()
	{
		return bankTeleport;
	}
	
	public CheckPouches getCheckPouches()
	{
		return checkPouches;
	}
	
	public CheckFamiliar getCheckFamiliar()
	{
		return checkFamiliar;
	}
	
	public CheckRing getCheckRing()
	{
		return checkRing;
	}
	
	public CheckSummoning getCheckSummoning()
	{
		return checkSummoning;
	}
	
	public Refill getRefill()
	{
		return refill;
	}
	
	public WalkToAltar getWalkToAltar()
	{
		return walkToAltar;
	}
	
	public void checkPrice()
	{
		natPrice = GrandExchange.lookup(Data.NATURE_RUNE_ID).getPrice();
	}
}