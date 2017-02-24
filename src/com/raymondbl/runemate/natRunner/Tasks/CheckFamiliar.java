package com.raymondbl.runemate.natRunner.Tasks;

import com.raymondbl.runemate.natRunner.Data;
import com.raymondbl.runemate.natRunner.NatRunnerBankUtil;
import com.raymondbl.runemate.utilities.BankUtil;
import com.raymondbl.runemate.utilities.Util;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class CheckFamiliar extends Task
{
	private boolean condition;
	@Override
	public void execute() 
	{
		if(!Util.isCarried(Data.GRAAHK_POUCH_ID))
		{
			Data.gui.setStatus("Withdrawing graahk pouch...");
			if(Inventory.isFull())
			{
				NatRunnerBankUtil.depositAll();
				condition = true;
			}
			NatRunnerBankUtil.withdraw(Data.GRAAHK_POUCH, 1);
			BankUtil.close();
		}
		SpriteItem graahkPouch = Inventory.getItems(Inventory
				.getNameFilter(Data.GRAAHK_POUCH)).first();
		if(!Util.isNull(graahkPouch))
		{
			Data.gui.setStatus("Summoning graahk...");
			Util.openHUD(graahkPouch);
			if(graahkPouch.interact("Summon"))
			{
				if(Execution.delayUntil(() -> !Util.hasFamiliar(), 2000, 4000));
				else if(validate()) {execute(); return;}
				if(condition)
				{
					Refill refill = Data.runner.getRefill();
					refill.setPreset();
					if(refill.getPreset())
						NatRunnerBankUtil.preset();
					else NatRunnerBankUtil.withdrawAll(Data.PURE_ESS);
					condition = false;
				}
			}
			else if(validate()) execute();
		}
		else if(validate()) execute();
	}

	@Override
	public boolean validate() 
	{
		return !Util.hasFamiliar() && Util.isNearBank();
	}
}
