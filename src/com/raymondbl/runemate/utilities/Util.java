package com.raymondbl.runemate.utilities;

import java.util.concurrent.Callable;

import com.raymondbl.runemate.natRunner.Data;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.Validatable;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

public class Util 
{
	public static boolean isWorn(int... ids)
	{
		return Equipment.containsAnyOf(ids);
	}
	
	public static boolean isWorn(String... str)
	{
		return Equipment.containsAnyOf(str);
	}
	
	public static boolean isCarried(int... ids)
	{
		return Inventory.containsAnyOf(ids);
	}
	
	public static boolean isCarried(String... str)
	{
		return Inventory.containsAnyOf(str);
	}
	
	public static boolean isCarried(Filter<SpriteItem> filter)
	{
		return Inventory.containsAnyOf(filter);
	}
	
	public static boolean isNull(Validatable v)
	{
		return v == null;
	}
	
	public static boolean isNearBank()
	{
		return !isNull(Banks.getLoaded().nearest());
	}
	
	public static boolean isVisible(LocatableEntity entity)
	{
		return !isNull(entity) && entity.getVisibility() > 40;
	}
	
	
	public static void getIntoView(LocatableEntity entity)
	{
		if(!isNull(entity) && !isVisible(entity))
		{
			if(Camera.getPitch() < 0.15 || Camera.getPitch() > 0.40)
				Camera.setPitch(Random.nextGaussian(0.15, 0.40));
			Camera.turnTo(entity);
		}
	}
	
	public static boolean hasFamiliar()
	{
		InterfaceComponent component = getActionInterface("Interact");
		return !isNull(component) && component.isVisible() && 
				component.getTextureId() != 8656;
				
	}
	
	public static void wear(int... ids)
	{
		if(validateWear(ids))
		{
			openHUD(Inventory.getItems(ids).first());
			if(Inventory.getItems(ids).first().interact("Wear"))
			{
				if(Execution.delayUntil(() -> !validateWear(), 1200, 2400));
				else wear(ids);
			}
			else wear(ids);
		}
	}
	
	private static boolean validateWear(int... ids)
	{
		return Inventory.containsAnyOf(ids) && !isWorn(ids);
	}
	
	public static void openHUD(SpriteItem item)
	{
		if(validateOpenHUD(item))
		{
			String key = "";
			if(Util.isCarried(item.getId()))
			{
				key = "b";
			}
			else key = "n";
			if(Keyboard.typeKey(key))
			{
				if(Execution.delayUntil(() -> !validateOpenHUD(item), 1200, 2400));
				else openHUD(item);
			}
			else openHUD(item);
		}
	}
	
	private static boolean validateOpenHUD(SpriteItem item)
	{
		return !item.isVisible();
	}
	
	public static void drop(int id)
	{
		if(validateDrop(id))
		{
			SpriteItem item = Inventory.getItems(id).first();
			if(!isNull(item))
			{
				if(item.interact("Drop"))
				{
					if(Execution.delayUntil(() -> !validateDrop(id), 2400, 4800));
					else drop(id);
				}
				else drop(id);
			}
		}
	}
	
	private static boolean validateDrop(int id)
	{
		return Inventory.contains(id);
	}
	
	public static InterfaceComponent getInterface(int textureId)
	{
		return Interfaces.getVisible(Interfaces.getTextureFilter(textureId))
				.first();
	}
	
	public static InterfaceComponent getInterface(String text)
	{
		return Interfaces.getVisible(Interfaces.getTextContainsFilter(text))
				.first();
	}
	
	public static InterfaceComponent getActionInterface(String action)
	{
		return Interfaces.getVisible(Interfaces.getActionFilter(action))
				.first();
	}
	
	public static boolean processInterface(int textureId)
	{
		InterfaceComponent component = getInterface(textureId);
		if(!isNull(component))
		{
			if(Keyboard.typeKey(" "))
			{
				if(Execution.delayUntil(() -> !isNull(component), () -> 
				Data.player.getAnimationId() != -1, 1200, 2400))
					return true;
				else return processInterface(textureId);
			}
			else return processInterface(textureId);
		}
		else return false;
	}
	
	public static boolean processInterface(String text, Integer index)
	{
		InterfaceComponent component = getInterface(text);
		if(!isNull(component))
		{
			int key = component.getIndex();
			if(key == 12)
				index = 1;
			if(key == 18)
				index = 2;
			if(Keyboard.typeKey(index.toString()))
			{
				if(Execution.delayUntil(() -> !isNull(component), () -> 
					Data.player.getAnimationId() != -1, 2400, 4800));
				else return processInterface(text, index);
			}
			else return processInterface(text, index);
			return true;
		}
		else return false;
	}
	
	public static boolean processInterface(String text, String text2)
	{
		InterfaceComponent component = getActionInterface(text);
		System.out.println("Util: 195");
		if(!isNull(component) && component.isVisible())
		{
			System.out.println("Util: 198");
			if(component.click())
			{
				System.out.println("Util: 201");
				if(Execution.delayUntil(() -> !isNull(getInterface(text2)), () -> 
					Data.player.getAnimationId() != -1, 2400, 4800));
				else return processInterface(text, text2);
			}
			else return processInterface(text, text2);
			return true;
		}
		else return false;
	}
	
	public static GameObject firstLoaded(int id)
	{
		return GameObjects.getLoaded(id).first();
	}
	
	public static GameObject firstLoaded(String name)
	{
		return GameObjects.getLoaded(name).first();
	}
	
	public static GameObject nearestLoaded(int id)
	{
		return GameObjects.newQuery().filter(GameObjects.getIdFilter(id)).results().nearest();
	}
	
	public static GameObject nearestLoaded(String name)
	{
		return GameObjects.newQuery().names(name).results().nearest();
	}
	
	public static Npc getNpc(String name)
	{
		return Npcs.getLoaded(Npcs.getNameFilter(name)).first();
	}
	
	public static String getFirstMenuAction()
	{
		return Menu.getItemAt(0).getAction();
	}
	
	public static boolean delayUntil(Callable<Boolean> callable, int minTimeOut,
			int maxTimeOut)
	{
		int gaussian = (int)Random.nextGaussian(minTimeOut, maxTimeOut);
		StopWatch timer = new StopWatch();
		timer.start();
		try {
			while(!callable.call() && timer.getRuntime() < gaussian);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(timer.getRuntime() > gaussian)
			return false;
		else return true;
	}
}
