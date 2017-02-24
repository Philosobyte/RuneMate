package com.raymondbl.runemate.natRunner;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.web.Web;
import com.runemate.game.api.hybrid.location.navigation.web.WebVertex;
import com.runemate.game.api.hybrid.location.navigation.web.default_webs.FileWeb;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.InventoryListener;
import com.runemate.game.api.script.framework.listeners.events.ItemEvent;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;

public class RaysNatRunnerLegacy extends LoopingScript implements
									PaintListener, InventoryListener, ChatboxListener
{
	private StopWatch runtime = new StopWatch();
	private boolean condition;
	private boolean npcContact = false;
	private boolean guildRepair = true;
	private boolean repairPouch;
	private boolean degraded = false;
	private int natsAdded;
	private int natsRemoved;
	private int essAdded;
	private int i;
	private int essRemoved;
	private int summoningPoints = 69;
	private Player player;
	private Collection<WebVertex> list;
	private Map<Coordinate, WebVertex> vertices = new HashMap<>();
	private Map<Coordinate, WebVertex> webVertices = new HashMap<>();
	private Web web = new Web();
	

    public boolean merge(final File file) 
    {
    	try(FileInputStream input = new FileInputStream(file))
    	{
            byte[] obj = new byte[input.available() + 32];
            input.read(obj);
           	FileWeb fileWeb = FileWeb.fromByteArray(obj);
        	this.web.addVertices(fileWeb.getVertices());
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return true;
    }
    
	@Override
	public void onStart(String... args)
	{
		setLoopDelay(1000, 3000);
		getEventDispatcher().addListener(this);
		runtime.start();
		player = Players.getLocal();
		if(Camera.getPitch() < 0.2 || Camera.getPitch() > 0.45)
			Camera.setPitch(Random.nextGaussian(0.3, 0.6));
		if(Camera.getYaw() < 242 || Camera.getYaw() > 292)
			Camera.setYaw((int)Random.nextGaussian(242, 292));
		merge(new File("webbset.web"));
	}
	
	@Override
	public void onPaint(Graphics2D g) 
	{
		
	}
	
	@Override
	public void onItemAdded(ItemEvent event)
	{
		condition = true;
		essRemoved = 0;
	}
	
	@Override
	public void onItemRemoved(ItemEvent event)
	{
		essRemoved += event.getQuantityChange();	
	}
	
	@Override
	public void onLoop() 
	{
		refill();
		graahkTeleport();
		Execution.delayUntil(() -> player.distanceTo(Data.RUINS) <= 300);
		checkSummoningPoints();		
		walkToAltar();
		craft();
		cWarsTeleport();
	}
	
	public boolean refill()
	{
		openBank();
		checkRing();
		checkPouches();
		checkFamiliar();
		withdrawAll(Data.ESS_FILTER, Data.PURE_ESS);
		fillPouchesActBar();
		withdrawAll(Data.ESS_FILTER, Data.PURE_ESS);
		return true;
	}
	
	public void alreadyChecked()
	{
		openBank();
		checkRing();
		checkFamiliar();
		withdrawAll(Data.ESS_FILTER, Data.PURE_ESS);
		closeBank();
	}
	
	public boolean openBank()
	{	System.out.println("opening bank");
		if(!Bank.isOpen())
		{
			Execution.delayUntil(() -> Banks.getLoaded().nearest() != null);
			if(Banks.getLoaded().nearest().getVisibility() < 20)
			{
				Camera.turnTo(Banks.getLoaded().nearest());
			}
			if(Bank.open())
			{
				if(Execution.delayUntil(() -> Bank.isOpen(), 6000, 9000));
				else
				{
					openBank();
				}
			}
			else openBank();
		}
		return true;
	}
	
	public boolean closeBank()
	{	System.out.println("closing bank...");
		if(Bank.isOpen())
		{
			Bank.close();
		}
		if(Bank.isOpen())
		{
			return closeBank();
		}
		return true;
	}
	
	public boolean depositNatureRunes()
	{
		openBank();
		if(Inventory.contains(Data.NATURE_RUNE_ID))
		{
			Bank.depositAllExcept(Data.POUCHES_FILTER);
		}
		if(Inventory.contains(Data.NATURE_RUNE_ID))
			depositNatureRunes();
		return true;
	}
	
	public boolean checkPouches()
	{
		if(Inventory.containsAnyOf(Data.DEGR_POUCHES_FILTER))
		{	System.out.println("Inventory contains Data.DEGR_POUCHES");
			if(npcContact && Bank.containsAllOf(Data.ASTRAL_RUNE_ID, 
					Data.COSMIC_RUNE_ID))
				repairNPC();
			else if(guildRepair)
				repairGuild();
		}
		else return false;
		return true;
	}
	
	public boolean checkRing()
	{
		openBank();
		if(!Equipment.containsAnyOf(Data.RINGS))
		{	System.out.println("Equipment contains a ring");
			if(!Inventory.containsAnyOf(Data.RING_FILTER))
			{	System.out.println("Inventory does not contain a ring");
				if(Bank.withdraw(Data.RING_FILTER, 1))
				{
					if(Execution.delayUntil(() -> 
						Inventory.containsAnyOf(Data.RING_FILTER), 3000, 6000))
					{
						System.out.println("Now Inventory contains a ring");
					}
					else return checkRing();
				}
				else return checkRing();
			}
			if(Inventory.containsAnyOf(Data.RING_FILTER))
			{
				if(Inventory.getItems(Data.RING_FILTER).first().interact("Wear"))
				{	System.out.println("Wear returned true");
					if(Execution.delayUntil(() -> 
						!Inventory.containsAnyOf(Data.RING_FILTER), 3000, 6000));
					else return checkRing();
				}
				else return checkRing();
			}
			else 
			{
				return checkRing();
			}
		}
		return true;
	}
	
	public boolean checkFamiliar()
	{
		if(player.getFamiliar() == null)
		{	System.out.println("Player.getfamiliar() is null");
			if(!Inventory.contains(Data.GRAAHK_POUCH_ID))
			{	System.out.println("Inventory does not contain a graahk pouch");
				openBank();
				if(Bank.withdraw(Data.GRAAHK_POUCH_ID, 1))
				{	System.out.println("Successfully withdrew pouch");
					if(Execution.delayUntil(() -> 
						Inventory.contains(Data.GRAAHK_POUCH_ID), 3000, 6000));
					else return checkFamiliar();
				}
				else return checkFamiliar();
			}
			if(Inventory.contains(Data.GRAAHK_POUCH_ID))
			{	System.out.println("Inventory contains a pouch");
				closeBank();
				if(Inventory.newQuery().filter(Data.GRAAHK_POUCH_FILTER)
						.results().first().interact("Summon"))
				{	System.out.println("Pressed summon");
					if(Execution.delayUntil(() -> player.getFamiliar() != null, 
							2000, 4000));
					else return checkFamiliar();
				}
				else return checkFamiliar();
			}
			else
			{
				checkFamiliar();
				return true;
			}
			openBank();
		}
		return true;
	}
	
	public boolean withdrawAll(Filter<SpriteItem> filter, int id)
	{
		if(!Inventory.isFull())
		{
			openBank();
			if(Bank.containsAllOf(id))
			{
				/*
				SpriteItem item = Bank.getFirstItem(filter);
				item.interact("Withdraw-All");
				if(!Inventory.isFull() && Bank.containsAllOf(id))
				{
					withdrawAll(filter, id);
				}
				*/
				Keyboard.typeKey("1");
			}
			Execution.delayUntil(() -> !Bank.isOpen());
		}
		return true;

		
	}
	
	public boolean fillPouches()
	{
		openBank();
		for(int i = 0; i < Data.POUCHES_ARRAY.length; i++)
		{
			int pouchId = Data.POUCHES_ARRAY[i].getId();
			int pouchCapacity = Data.POUCHES_ARRAY[i].getCapacity();
			if(Inventory.contains(pouchId))
			{
				if(Inventory.getQuantity(Data.PURE_ESS) <= pouchCapacity)
				{
						withdrawAll(Data.ESS_FILTER, Data.PURE_ESS);
				}
				if(Inventory.getQuantity(Data.PURE_ESS) >= pouchCapacity)
				{
					Inventory.newQuery().filter(new Filter<SpriteItem>()
							{
						@Override
						public boolean accepts(SpriteItem s)
						{
							return s.getId() == pouchId;
						}
							}).results().first().interact("Fill");
					
					if(essRemoved != 0 || degraded)
					{
						essRemoved = 0;
					}
					else
					{
						i--;
					}
				}
				else if(!Bank.containsAnyOf(Data.PURE_ESS))
				{
					continue;
				}
				else i--;
			}
		}
		return true;
	}
	
	public boolean fillPouchesActBar()
	{
		List<SlotAction> actions = ActionBar.getActions(Data.POUCHES_IDS);
		System.out.println(actions);
		while(!actions.isEmpty())
		{	
			SlotAction action = actions.get(0);
			int capacity = new Pouch(action.getId()).getCapacity();
			if(Inventory.getQuantity(Data.PURE_ESS) >= capacity)
			{	
				closeBank();
				Keyboard.typeKey(action.getSlot().getKeyBind());
				if(!Execution.delayUntil(() -> essRemoved != 0, 1000, 2000))
				{
					Keyboard.typeKey(action.getSlot().getKeyBind());
					Execution.delayUntil(() -> essRemoved != 0);
				}
				essRemoved = 0;

				if(checkPouches())
					continue;
			}
			else 
			{	openBank();
				if(Bank.containsAnyOf(Data.PURE_ESS))
				{
					withdrawAll(Data.ESS_FILTER, Data.PURE_ESS);
					continue;
				}
				else break;
			}
			actions.remove(0);
		}
		return true;
	}
	
	public boolean repairNPC()
	{
		if(!Inventory.contains(Data.ASTRAL_RUNE_ID))
		{
			openBank();
			if(Bank.containsAllOf(Data.ASTRAL_RUNE_ID))
			{
				Bank.withdraw(Data.ASTRAL_RUNE_ID, 1);
				if(!Inventory.contains(Data.ASTRAL_RUNE_ID))
				{
					repairNPC();
					return true;
				}
			}
			else return false;	
		}
		
		if(!Inventory.contains(Data.COSMIC_RUNE_ID))
		{
			openBank();
			if(Bank.containsAllOf(Data.COSMIC_RUNE_ID))
			{
				Bank.withdraw(Data.COSMIC_RUNE_ID, 1);
				if(!Inventory.contains(Data.COSMIC_RUNE_ID))
				{
					repairNPC();
					return true;
				}
			}
			else return false;
		}
		
		closeBank();
		SlotAction slotAction = ActionBar.getFirstAction(Data.NPC_CONTACT);
		slotAction.activate();
		while(Inventory.containsAnyOf(Data.DEGR_POUCHES_FILTER))
		{
			if(interfaceNext(1191, 8));
			else if(interfaceNext(1184, 12));
			else if(interfaceNext(1188, 18));
			else if(interfaceNext(88, 5, 14));
		}
		return true;
	}
	
	public boolean repairGuild()
	{
		teleportToGuild();
		Execution.delayUntil(() -> GameObjects.getLoaded(Data.RUNECRAFTING_PORTAL).first() != null);
		enterPortal();
		korvak();
		cWarsTeleport();
		
		return true;
	}
	
	public boolean teleportToGuild()
	{
		closeBank();
		if(Equipment.containsAllOf(Data.WICKED_HOOD_ID))
		{
			GameObject portal = 
					GameObjects.getLoaded(Data.RUNECRAFTING_PORTAL).first();
			if(portal == null || !portal.isVisible())
			{
				SpriteItem hood = Equipment.newQuery().filter(Data.WICKED_FILTER)
						.results().first();
				if(!hood.isVisible())
				{
					Keyboard.typeKey("n");
				}
				if(hood.isVisible() && hood.interact("Teleport"))
				{
					Keyboard.typeKey("b");
				}
				else 
					return teleportToGuild();
			}
		}
		return true;
	}
	
	public boolean enterPortal()
	{
		if(Camera.getPitch() < 0.1 || Camera.getPitch() > 0.4)
		{
			Camera.setPitch(Random.nextGaussian(0.1, 0.4));
		}
		GameObject portal;
		portal = GameObjects.getLoaded(Data.RUNECRAFTING_PORTAL).first();
		if(portal != null)
		{
			if(portal.isVisible())
			{
				if(portal.interact("Enter"))
				{
					Execution.delayUntil(() -> 
						Npcs.newQuery().filter(Data.KORVAK_FILTER)
						.results().first() != null);
				}
				else if(portal != null && Camera.turnTo(portal))
					enterPortal();
			}
			else
			{
			Camera.turnTo(portal);
			enterPortal();
			};
		}
		return true;
	}
	
	public boolean korvak()
	{
		Npc korvak;
		if(Npcs.newQuery().filter(Data.KORVAK_FILTER).results().first() != null)
		{
			korvak = Npcs.newQuery().filter(Data.KORVAK_FILTER).results().first();
			if(Camera.getPitch() < 0.3)
			{
				Camera.setPitch(Random.nextGaussian(0.3, 0.5));
			}
			if(korvak.isVisible())
			{
				if(!Inventory.contains(Data.MEDIUM_POUCH.getId()))
				{
					if(Inventory.contains(Data.DEGR_MEDIUM_POUCH.getId()))
					{
						if(Inventory.getItems(Data.DEGR_MEDIUM_POUCH.getId()).first().interact("Drop"));
						else
						{
							korvak();
							return true;
						}
					}
					if(interfaceNext(1191, 8) || interfaceNext(1184, 12) || 
							interfaceNext(1188, 18) || korvak.interact("Talk-to"))
					{
						if(Execution.delayUntil(() -> 
						interfaceNext(1191, 8) || interfaceNext(1184, 12) || 
						interfaceNext(1188, 18), 5000, 8000));
						else
						{
							korvak();
							return true;
						}
					}
					else
					{
						korvak();
						return true;
					}
					while(!Inventory.contains(Data.MEDIUM_POUCH.getId()))
					{
						if(interfaceNext(1191, 8));
						else if(interfaceNext(1184, 12));
						else if(interfaceNext(1188, 18));
						else
						{
							korvak();
							return true;
						}
					}
					while(interfaceNext(1191, 8) || interfaceNext(1184, 12) || 
							interfaceNext(1188, 18));
				}
				if(Inventory.containsAnyOf(Data.DEGR_POUCHES_FILTER))
				{
					if(interfaceNext(1191, 8) || interfaceNext(1184, 12) || 
							(Inventory.contains(Data.DEGR_LARGE_POUCH.getId())
									&& interfaceNext(1188, 12)) || 
							(Inventory.contains(Data.DEGR_GIANT_POUCH.getId())
									&& interfaceNext(1188, 18)) || 
							korvak.interact("Repair-all"))
					{	
						if(Execution.delayUntil(() -> interfaceNext(1191, 8) 
								|| interfaceNext(1184, 12), () -> player.isMoving(), 1500, 2000));
						else
						{
							korvak();
							return true;
						}
					}
					else
					{
						korvak();
						return true;
					}
					
					while(Inventory.containsAnyOf(Data.DEGR_POUCHES_FILTER))
					{
						if(interfaceNext(1191, 8));
						else if(interfaceNext(1184, 12));
						else if(Inventory.contains(Data.DEGR_LARGE_POUCH.getId())
								&& interfaceNext(1188, 12));
						else if(Inventory.contains(Data.DEGR_GIANT_POUCH.getId())
								&& interfaceNext(1188, 18));
						else
						{
						korvak();
						return true;
						}
					}
					while(interfaceNext(1191, 8) || interfaceNext(1184, 12));
				}
				
			}
			else 
			{
				Camera.turnTo(korvak);
				korvak();
			}
		}
		else return korvak();
		return true;
	}
	public boolean interfaceNext(int i, int k)
	{
			if(Interfaces.getAt(i, k) != null)
			{
				if(Interfaces.getAt(i, k) != null)
				{
					return Interfaces.getAt(i, k).click();
				}
				else return true;
			}
			else return false;
	}
	
	public boolean interfaceNext(int i, int k, int m)
	{
		if(Interfaces.getAt(i, k, m) != null)
		{
			if(Interfaces.getAt(i, k, m) != null)
			{
				return Interfaces.getAt(i, k, m).click();
			}
			else return true;
		}
		else return false;
	}
	
	public boolean cWarsTeleport()
	{
		if(Equipment.containsAnyOf(Data.RINGS))
		{
			if(GameObjects.getLoaded(Data.BANK_CHEST).first() == null)
			{	Execution.delayUntil(() -> player.getAnimationId() == -1);
				SpriteItem ring = Equipment.newQuery().filter(Data.RING_FILTER)
						.results().first();
				if(!ring.isVisible())
				{
					Keyboard.typeKey("n");
				}
				if(ring.isVisible())
				{
					if(ring.interact("Castle Wars"))
					{	Keyboard.typeKey("b");
						if(Execution.delayUntil(() -> 
							GameObjects.getLoaded(Data.BANK_CHEST).first() != null,
							7000, 10000));
						else return cWarsTeleport();
					}
					else return cWarsTeleport();
					Keyboard.typeKey("b");
				}
				else 
					return cWarsTeleport();
			}
		}
		return true;
	}
	
	public boolean graahkTeleport()
	{
		if(player.getFamiliar() != null && !interfaceNext(1188, 18))
		{
			if(player.getFamiliar().interact("Interact"))
			{
				if(Execution.delayUntil(() -> interfaceNext(1188, 18), () -> 
						player.isMoving(), 1500, 3000));
				else return graahkTeleport();
			}
			else return graahkTeleport();
		}
		return true;
	}
	public boolean walkToAltar()
	{
		Path path = web.getPathBuilder().buildTo(Data.RUINS);
		Execution.delayUntil(() -> 
		{
			if(player.distanceTo(Data.RUINS) >= 20)
				path.step();
			return player.distanceTo(Data.RUINS) <= 20;
		});
		GameObject ruins = GameObjects.getLoaded(Data.RUINS_ID).first();
		if(ruins != null && ruins.getVisibility() > 20)
		{
			if(ruins.interact("Enter"))
			{
				if(Execution.delayUntil(() -> player.distanceTo(Data.ALTAR) <= 15, 
						() -> player.isMoving(), 1000, 2000));
				else return walkToAltar();
			}
			else return walkToAltar();
		}
		else 
		{
			if(ruins != null && ruins.isValid() && Camera.turnTo(ruins));
			return walkToAltar();
		}
		return true;
	}
	
	public boolean checkSummoningPoints()
	{
		System.out.println("checking summoning...");
		if(Summoning.getPoints() < summoningPoints)
		{
			Path path = web.getPathBuilder().buildTo(Data.OBELISK);
			System.out.println("in summoning delayUntil");
			Execution.delayUntil(() -> 
			{
				if(player.distanceTo(Data.OBELISK) >= 15)
					path.step();
				return player.distanceTo(Data.OBELISK) <= 15;
			});
			System.out.println("summoning delayUntil returned true");
			GameObject obelisk = GameObjects.getLoaded(Data.OBELISK_ID).first();
			if(obelisk != null && obelisk.getVisibility() > 20)
			{
				System.out.println("good visibility of obelisk");
				if(obelisk.interact("Renew points"))
				{	System.out.println("renewing points");
					if(Execution.delayUntil(() -> 
						Summoning.getPoints() > summoningPoints, 5000, 8000));
					else 
					{	System.out.println("restarting checkSummoningPoints");
						checkSummoningPoints();
						return true;
					}
				}
				else return checkSummoningPoints();
			}
			else
			{
				if(obelisk != null & obelisk.isValid() && Camera.turnTo(obelisk));
				return checkSummoningPoints();
			}
		}
		return true;
	}
	
	public boolean craft()
	{
		if(GameObjects.getLoaded(Data.ALTAR_ID).first() != null)
		{
			GameObject altar = GameObjects.getLoaded(Data.ALTAR_ID).first();
			if(altar.interact("Craft-rune"))
			{
				if(Execution.delayUntil(() -> essRemoved != 0, 2000, 4000))
				{
					essRemoved = 0;
				}
				else return craft();
			}
			else return craft();
		}
		else return craft();
		return true;
	}

	@Override
	public void onMessageReceived(MessageEvent m) 
	{
		if(m.getMessage().contains("Your pouch"))
		{
			degraded = true;
		}
	}

}