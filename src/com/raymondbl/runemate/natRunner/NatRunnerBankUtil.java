package com.raymondbl.runemate.natRunner;

import com.raymondbl.runemate.utilities.BankUtil;
import com.raymondbl.runemate.utilities.Util;
import com.raymondbl.runemate.natRunner.Data;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;

public class NatRunnerBankUtil extends BankUtil
{
	private static String key;
	public static void preset()
	{
		if(validatePreset())
		{
			switch(Data.gui.getPreset())
			{
			case PRESET1	:	key = "1";
								break;
			case PRESET2	:	key = "2";
								break;
			case NONE		:	Data.runner.stop();
								break;
			}
			open();
			if(!Bank.containsAllOf(Data.PURE_ESS))
			{
				Data.gui.setStatus("Run out of ess.");
				Execution.delay(1000);
				Data.runner.stop();
			}
			if(Keyboard.typeKey(key))
			{
				if(Execution.delayUntil(() -> !Bank.isOpen(), 1200, 2400));
				else preset();
			}
			else preset();
		}
	}
	
	private static boolean validatePreset()
	{
		return Inventory.getEmptySlots() > 1;
	}
	
	public static void withdrawAll(int id)
	{
		if(validateWithdrawAll(id))
		{
			SpriteItem item = get(id);
	
			if(item.interact("Withdraw-All"))
			{
				if(Execution.delayUntil(() -> !validateWithdrawAll(id), 1200, 2400));
				else withdrawAll(id);
			}
			else withdrawAll(id);
		}
	}
	
	private static boolean validateWithdrawAll(int id)
	{
		if(!Bank.isOpen())
			open();
		return Bank.containsAnyOf(id) && !Inventory.isFull();
	}
	public static void depositAll()
	{
		if(validateDepositAll())
		{
			BankUtil.open();
			if (Bank.depositAllExcept(Data.POUCHES_FILTER))
			{
				if (Execution.delayUntil(() -> !validateDepositAll(), 1200, 2400)) ;
				else depositAll();
			}
			else depositAll();
		}
	}

	public static boolean validateDepositAll()
	{
		return !Inventory.containsOnly(Data.POUCHES_FILTER);
	}
	
	public static void wear(int... ids)
	{
		if(validateWear(ids))
		{
			if(Bank.getItems(ids).first().interact("Wear"))
			{	
				if(Execution.delayUntil(() -> Util.isWorn(ids), 1200, 2400));
				else if(validateWear())
				{
					if(Util.isCarried(Data.RING_FILTER))
					{
						depositAll();
					}
					else wear(ids);
				}
			}
			else wear(ids);
		}
		else wear(ids);
	}
	
	private static boolean validateWear(int... ids)
	{
		return Bank.containsAnyOf(ids) && !Util.isWorn(ids);
	}
	
	public static void withdraw(String name, int quantity)
	{
		if(validateWithdraw(name))
		{
			open();
			if(Bank.withdraw(name, quantity))
			{
				if(Execution.delayUntil(() -> Util.isCarried(name), 
						() -> Data.player.isMoving(), 1200, 2400));
				else withdraw(name, quantity);
			}
			else withdraw(name, quantity);
		}
	}
	
	private static boolean validateWithdraw(String name)
	{
		return !Util.isCarried(name);
	}
}

