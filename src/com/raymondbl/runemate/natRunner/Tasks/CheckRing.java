package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.natRunner.Data.Teleport;
import com.raymondbl.runemate.natRunner.NatRunnerBankUtil;
import com.raymondbl.runemate.utilities.BankUtil;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.task.Task;

public class CheckRing extends Task
{

	@Override
	public void execute() 
	{

		Data.gui.setStatus("Wearing ring...");
		BankUtil.open();
		if(Inventory.contains(Data.RING_FILTER))
			NatRunnerBankUtil.depositAll();
		if(!Bank.containsAnyOf(Data.RINGS))
		{
			Data.gui.setBankTeleport(Teleport.KIN);
			return;
		}
		else
		NatRunnerBankUtil.wear(Data.RINGS);
	}

	@Override
	public boolean validate() 
	{
		return !Util.isWorn(Data.RINGS) && Util.isNearBank();
	}
	
}
