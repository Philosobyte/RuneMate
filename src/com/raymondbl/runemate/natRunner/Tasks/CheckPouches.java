package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.natRunner.NatRunnerBankUtil;
import com.raymondbl.runemate.natRunner.Data.Repair;
import com.raymondbl.runemate.utilities.BankUtil;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class CheckPouches extends Task
{
	@Override
	public void execute() 
	{
		if(Data.gui.isMassiveSelected() && !hasMassivePouch())
		{
			BankUtil.open();
			if(Bank.containsAnyOf(Data.NEW_MASSIVE_POUCH.getName()))
				BankUtil.withdraw(Data.NEW_MASSIVE_POUCH.getName(), 1);
			else
				Data.gui.setMassive(false);
		}
		if(!validate())
			return;
		Repair repair = Data.gui.getRepair();
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
	}

	@Override
	public boolean validate() 
	{
		Repair repair = Data.gui.getRepair();
		return repair != Repair.NONE && 
				(Util.isCarried(Data.DEGR_POUCHES_FILTER) || !hasMedPouch() || 
						(!hasMassivePouch() && Data.gui.isMassiveSelected()
								&& Util.isNearBank()));
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
			Data.gui.setStatus("Teleporting to Guild...");
			if(Util.isWorn(Data.WICKED_HOOD))
			{
				SpriteItem hood = Equipment.getItems(Data.WICKED_HOOD).first();
				if(!Util.isNull(hood))
				{
					Util.openHUD(hood);
					if(hood.interact("Teleport"))
					{
						Keyboard.typeKey("b");
						if(Execution.delayUntil(() -> validateEnterPortal(), 
								() -> Data.player.getAnimationId() != -1, 
								5000, 8000));
						else { teleportToGuild(); return;}
					}
					else { teleportToGuild(); return;}
				}
			}
			else 
			{
				Data.gui.setStatus("Wicked hood not equipped.");
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
			Data.gui.setStatus("Entering portal...");
			GameObject portal = Util.firstLoaded(Data.GUILD_PORTAL);
			if(Camera.getPitch() < 0.08 || Camera.getPitch() > 0.22)
				Camera.setPitch(Random.nextGaussian(0.08, 0.22));
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
				Data.gui.setStatus("Dropping medium pouch...");
				Util.drop(degrMedId);
			}
			if(validate())
			{
				Data.gui.setStatus("Talking to Korvak...");
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
				Data.LARGE_POUCH.setLastRepaired(true);
			else if(needsGiantPouch() && Util.processInterface(Data.GIANT_POUCH_TEXT, 2))
				Data.GIANT_POUCH.setLastRepaired(true);
			else if(validate())
			{	
				if(!hasMedPouch())
					interactKorvak(false);
				else interactKorvak(true);
			}
			Execution.delay((int)Random.nextGaussian(150, 320));
		}
		Execution.delayUntil(() -> Util.processInterface(Data.NEXT_TEXTURE_ID), 210, 432);
		
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
			Data.gui.setStatus("Withdrawing runes...");
			BankUtil.open();
			if(Inventory.isFull())
			{
				NatRunnerBankUtil.depositAll();
			}
			if(Bank.containsAllOf(Data.ASTRAL, Data.COSMIC))
			{
				NatRunnerBankUtil.withdraw(Data.ASTRAL, 1);
				NatRunnerBankUtil.withdraw(Data.COSMIC, 1);
			}
			else 
			{
				Data.gui.setStatus("Run out of runes.");
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
		Data.gui.setStatus("Casting NPC Contact...");
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
				if(Execution.delayUntil(() -> 
				Util.processInterface(Data.NEXT_TEXTURE_ID), 4500, 6700));
				else { contactDMage(); }
				while(validate() || Execution.delayUntil(() -> 
				Util.processInterface(Data.NEXT_TEXTURE_ID), 470, 780))
				{
					Execution.delayUntil(() -> Util.processInterface(Data.NEXT_TEXTURE_ID)
							|| Util.processInterface("Can you repair my pouches?", 2), 470, 780);
				}
				Data.GIANT_POUCH.setLastRepaired(true);
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
		InterfaceComponent component = Util.getInterface(Data.DARK_MAGE);
		return !Util.isNull(component) && component.isVisible();
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

}

