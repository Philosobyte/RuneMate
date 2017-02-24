package com.raymondbl.runemate.utilities;

import java.io.Console;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.natRunner.NatRunnerBankUtil;
import com.raymondbl.runemate.natRunner.Pouch;
import com.raymondbl.runemate.natRunner.Data.Repair;
import com.raymondbl.runemate.natRunner.Tasks.CheckPouches;
import com.raymondbl.runemate.natRunner.Tasks.Refill;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.Varps;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.web.Web;
import com.runemate.game.api.hybrid.location.navigation.web.WebEdge;
import com.runemate.game.api.hybrid.location.navigation.web.WebVertex;
import com.runemate.game.api.hybrid.location.navigation.web.default_webs.FileWeb;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

public class Test extends LoopingScript
{

	boolean first = true;
	Web web = new Web();
	private CheckPouches checkPouches = new CheckPouches();
	private Refill refill = new Refill();
	
	@Override
	public void onStart(String... args)
	{
		setLoopDelay(1000, 1500);
		
	}
	@Override
	public void onLoop() 
	{
		execute();
		stop();
	}
	
	private int count = 11;

	public void execute() 
	{
		Repair repair = Repair.NPC_CONTACT;
		NatRunnerBankUtil.close();
		switch(repair)
		{
		case GUILD		:	guild();
							break;
		case NPC_CONTACT:	npc();
							break;
		default			:	guild();
							break;
		}
		count = 0;
	}

	public boolean validate() 
	{
		Repair repair = Repair.NPC_CONTACT;
		return repair != Repair.NONE && 
				((count == 11 && repair.equals(Repair.NPC_CONTACT)) || 
						Util.isCarried(Data.DEGR_POUCHES_FILTER) || 
						(!hasMassivePouch() && true))
				&& Util.isNearBank();
	}
	
	public void guild()
	{
		if(validate())
		{
			teleportToGuild();
			enterPortal();
			korvak();
			Data.runner.getCWarsTeleport().execute();
		}
	}
	
	public void teleportToGuild()
	{
		if(validateTeleportToGuild())
		{
			if(Util.isWorn(Data.WICKED_HOOD))
			{
				SpriteItem hood = Equipment.getItems(Data.WICKED_HOOD).first();
				if(!Util.isNull(hood))
				{
					Util.openHUD(hood);
					if(hood.interact("Teleport"))
					{
						if(Execution.delayUntil(() -> !validateTeleportToGuild(), 
								() -> Data.player.getAnimationId() != -1, 
								5000, 8000));
						else { teleportToGuild(); return;}
					}
					else { teleportToGuild(); return;}
					Keyboard.typeKey("b");
				}
			}
			else 
			{
				Execution.delay(1000);
				Data.runner.stop();
			}
		}
	}
	
	private boolean validateTeleportToGuild()
	{
		return Util.isNull(Util.firstLoaded(Data.GUILD_PORTAL));
	}
	
	public void enterPortal()
	{
		if(validateEnterPortal())
		{
			GameObject portal = Util.firstLoaded(Data.GUILD_PORTAL);
			if(Camera.getPitch() < 0.1 || Camera.getPitch() > 0.45)
				Camera.setPitch(Random.nextGaussian(0.17, 0.33));
			Util.getIntoView(portal);
			if(portal.interact("Enter"))
			{
				if(Execution.delayUntil(() -> validateKorvak(), 
						() -> Data.player.isMoving(), 5000, 9000));
				else { enterPortal();}
			}
			else { enterPortal();}
		}
	}
	
	private boolean validateEnterPortal()
	{
		return !Util.isNull(Util.firstLoaded(Data.GUILD_PORTAL));
	}
	
	public void korvak()
	{
		if(validateKorvak())
		{
			int degrMedId = Data.DEGR_MEDIUM_POUCH.getId();
			if(Util.isCarried(degrMedId))
			{
				Util.drop(degrMedId);
			}
			if(validate())
			{
				getPouches();
			}
		}
	}
	
	private boolean validateKorvak()
	{
		return !Util.isNull(Util.getNpc(Data.KORVAK_NAME)) && 
				(validate() || !hasMedPouch());
	}
	
	private boolean hasMedPouch()
	{
		return Util.isCarried(Data.MEDIUM_POUCH.getId());
	}
	
	private boolean hasMassivePouch()
	{
		return Util.isCarried(Data.MASSIVE_POUCH.getName());
	}
	
	private boolean needsLargePouch()
	{
		return Util.isCarried(Data.DEGR_LARGE_POUCH.getId());
	}
	
	private boolean needsGiantPouch()
	{
		return Util.isCarried(Data.DEGR_GIANT_POUCH.getId());
	}
	
	private void getPouches()
	{
		while(validate() || Util.processInterface(Data.NEXT_TEXTURE_ID))
		{	
			if(Util.processInterface(Data.NEXT_TEXTURE_ID));
			else if(!hasMedPouch() && Util.processInterface(Data.MED_POUCH_TEXT, 2));
			else if(needsLargePouch() && Util.processInterface(Data.LARGE_POUCH_TEXT, 1))
				Data.LARGE_POUCH.setLastRepaired();
			else if(needsGiantPouch() && Util.processInterface(Data.GIANT_POUCH_TEXT, 2))
				Data.GIANT_POUCH.setLastRepaired();
			else if(validate())
			{	
				if(!hasMedPouch())
					interactKorvak(false);
				else interactKorvak(true);
			}
			Execution.delay((int)Random.nextGaussian(150, 320));
		}
		
	}
	
	private void interactKorvak(boolean repair)
	{
		String text;
		if(repair)
			text = "Repair-all";
		else text = "Talk-to";
		Npc korvak = Util.getNpc(Data.KORVAK_NAME);
		if(!Util.isNull(korvak))
		{
			Util.getIntoView(korvak);
			if(korvak.interact(text))
			{
				if(Execution.delayUntil(() -> 
					Util.processInterface(Data.NEXT_TEXTURE_ID),
					() -> Data.player.isMoving(), 1200, 2400));
				else { interactKorvak(repair);}
			}
			else { interactKorvak(repair);}
		}
	}
	
	public void npc()
	{
		withdrawRunes();
		contactDMage();
	}
	
	public void withdrawRunes()
	{
		if(validateWithdrawRunes())
		{
			BankUtil.open();
			if(Inventory.isFull())
			{
				NatRunnerBankUtil.depositAll();
			}
			if(Bank.containsAllOf(Data.ASTRAL, Data.COSMIC))
			{
				BankUtil.withdraw(Data.ASTRAL, 1);
				BankUtil.withdraw(Data.COSMIC, 1);
			}
			else 
			{
				Data.gui.setRepair(Repair.GUILD);
				execute();
				return;
			}
			BankUtil.close();
		}
	}
	
	public boolean validateWithdrawRunes()
	{
		return !Inventory.containsAllOf(Data.ASTRAL, Data.COSMIC);
	}
	
	public void contactDMage()
	{
		String key = ActionBar.getFirstAction(Data.NPC_CONTACT)
				.getSlot().getKeyBind();
		if(validateContactDMage())
		{
			if(Keyboard.typeKey(key))
			{
				if(Execution.delayUntil(() -> 
					isInterface(), 2000, 4000));
				else if(validate()) {contactDMage(); return;}
				scroll();
				processDMage(Data.DARK_MAGE);
				if(count == 11)
				{
					if(Execution.delayUntil(() -> 
					Util.processInterface(Data.NEXT_TEXTURE_ID), 4500, 6400));
					else { execute(); return; }
					while(count == 11)
					{
						if(Execution.delayUntil(() -> 
						Util.processInterface(Data.NEXT_TEXTURE_ID) || 
						Util.processInterface("Can you repair", 2), 1200, 2400));
						else count = 0;
					}
					count = 0;
					return;
				}
				else
				{
					while(validate() || Execution.delayUntil(() -> 
					Util.processInterface(Data.NEXT_TEXTURE_ID), 470, 780))
					{
						Execution.delayUntil(() -> Util.processInterface(Data.NEXT_TEXTURE_ID)
								|| Util.processInterface("Can you repair", 2), 470, 780);
					}
					count = 0;
					Data.GIANT_POUCH.setLastRepaired();
				}
			}
			else contactDMage();
		}
	}
	
	private boolean validateContactDMage()
	{
		return validate() && !validateWithdrawRunes();
	}
	
	private void scroll()
	{
		if(Util.getInterface(788).hover())
		{
			if(Mouse.click(Mouse.Button.LEFT, 
					(int)Random.nextGaussian(550, 970, 650)));
			else scroll();
		}
		else scroll();
	}
	
	private boolean isInterface()
	{
		return !Util.isNull(Util.getInterface(Data.DARK_MAGE)) && 
				Util.getInterface(Data.DARK_MAGE).isVisible();
	}
	
	private boolean processDMage(String text)
	{
		InterfaceComponent component = Util.getInterface(text);
		if(!Util.isNull(component))
		{
			if(component.interact("Speak-to"))
			{
				if(Execution.delayUntil(() -> !Util.isNull(component), () -> 
					Data.player.getAnimationId() != -1, 2400, 4800));
				else return processDMage(text);
			}
			else return processDMage(text);
			return true;
		}
		else return false;
	}
	
	public void addToCount()
	{
		count++;
	}
	
	private int essRemoved;
	private int essAdded;
	private boolean preset = true;
	
	public void executeR() 
	{
		setPreset();
		if(Data.gui.isMassiveSelected())
		{
			if(Util.isCarried(Data.NEW_MASSIVE_POUCH.getId()))
				fill(Data.NEW_MASSIVE_POUCH);
			else if(Util.isCarried(Data.MASSIVE_POUCH.getId()))
				fill(Data.MASSIVE_POUCH);
		}
		for(int i = 0; i < Data.NON_DEGR_POUCHES_ARRAY.length; i++)
		{
			Pouch pouch = Data.NON_DEGR_POUCHES_ARRAY[i];
			if(pouch.isUsed())
			{
				fill(pouch);
			}
		}
		if(!Inventory.isFull())
			bank();
			
	}
	
	public void fill(Pouch pouch)
	{
		if(validateFill(pouch))
		{
			Data.gui.setStatus("Filling pouches...");
			SlotAction action = pouch.getSlotAction();
			int capacity = pouch.getCapacity();
			if(Inventory.getQuantity(Data.PURE_ESS) >= capacity)
			{
				BankUtil.close();
				CheckPouches checkPouches = Data.runner.getCheckPouches();
				essRemoved = 0;
				if(Keyboard.typeKey(action.getSlot().getKeyBind()))
				{
					if(Execution.delayUntil(() -> essRemoved != 0, 867, 1833));
					else {fill(pouch); return;}
					if(Execution.delayUntil(() -> checkPouches.validate(), 100, 250))
					{
						checkPouches.execute();
						if(pouch.isLastRepaired())
							empty(pouch);
						fill(pouch);
					}
					if(pouch.getId() == 5514)
						checkPouches.addToCount();
				}
				else fill(pouch);
			}
			else 
			{	
				Data.gui.setStatus("Withdrawing essence...");
				if(pouch.getId() == Data.NEW_MASSIVE_POUCH.getId())
				{
					NatRunnerBankUtil.withdrawAll(Data.PURE_ESS);
				}
				else
					bank();
				fill(pouch);

			}
		}
	}
	
	private boolean validateFill(Pouch pouch)
	{
		return !pouch.isFilled();
	}
	
	public void empty(Pouch pouch)
	{
		if(validateEmpty(pouch))
		{
			int capacity = pouch.getDegradedVariant().getCapacity();
			if(Inventory.getEmptySlots() < capacity)
			{
				NatRunnerBankUtil.depositAll();
				empty(pouch);
				return;
			}
			
			essAdded = 0;
			if(Keyboard.typeKey(pouch.getSlotAction().getSlot().getKeyBind()))
			{
				if(Execution.delayUntil(() -> essAdded != 0, 830, 1586));
				else empty(pouch);
			}
			else empty(pouch);
		}
	}
	
	private boolean validateEmpty(Pouch pouch)
	{
		return pouch.isFilled();
	}
	public void executePlaceholder()
	{
		setPreset();
		List<SlotAction> actions = ActionBar.getActions(Data.POUCHES_IDS);
		while(!actions.isEmpty())
		{	
			Data.gui.setStatus("Filling pouches...");
			SlotAction action = actions.get(0);
			if(Inventory.contains(Inventory.getIdFilter(action.getId())
					.and(Inventory.getActionFilter("Fill"))))
			{
				int capacity = new Pouch(action.getId()).getCapacity();
				if(Inventory.getQuantity(Data.PURE_ESS) >= capacity)
				{	
					BankUtil.close();
					CheckPouches checkPouches = Data.runner.getCheckPouches();
					if(Keyboard.typeKey(action.getSlot().getKeyBind()))
					{
						Execution.delayUntil(() -> essRemoved != 0, 867, 1833);
						if(checkPouches.validate())
						{
							checkPouches.execute();
							essRemoved = 0;
							continue;
						}
					}
				}
				else 
				{	
					Data.gui.setStatus("Withdrawing ess...");
					BankUtil.open();
					if(Bank.containsAnyOf(Data.PURE_ESS))
					{	
						if(preset)
						{
							NatRunnerBankUtil.preset();
							continue;
						}
						else NatRunnerBankUtil.withdrawAll(Data.NATURE_RUNE_ID);
					}
					else break;
				}
			}
			essRemoved = 0;
			actions.remove(0);
		}
		if(preset)
			NatRunnerBankUtil.preset();
		else NatRunnerBankUtil.withdrawAll(Data.NATURE_RUNE_ID);
	}

	public boolean validateR() 
	{
		return Util.isNearBank() && Inventory.getEmptySlots() > 1;
	}

	public void addToEssRemoved()
	{
		essRemoved++;
	}
	
	public void addToEssAdded()
	{
		essAdded++;
	}
	
	public void setPreset()
	{
		switch(Data.gui.getPreset())
		{
		case NONE	:	preset = false;
						break;
		default		:	preset = true;
						break;
		}
	}
	
	public boolean getPreset()
	{
		return preset;
	}
	
	public void bank()
	{
		BankUtil.open();
		if(Bank.containsAnyOf(Data.PURE_ESS))
		{	
			if(preset)
			{
				NatRunnerBankUtil.preset();
			}
			else NatRunnerBankUtil.withdrawAll(Data.NATURE_RUNE_ID);
		}
		else
		{
			Data.gui.setStatus("Out of essence.");
			Execution.delay(1000);
			Data.runner.stop();
		}
	}
	
	
	
}
