package com.raymondbl.runemate.webCreator;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.web.WebVertex;
import com.runemate.game.api.hybrid.location.navigation.web.default_webs.FileWeb;
import com.runemate.game.api.hybrid.location.navigation.web.vertex_types.CoordinateVertex;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

public class RaysWebCreator extends LoopingScript implements
								PaintListener, ActionListener
{
	private boolean condition;
	private Coordinate position;
	private RaysWebCreatorGUI gui;
	private Player player;
	private String command;
	private Map<Coordinate, WebVertex> vertices = new HashMap<>();
	private Collection<WebVertex> list = new ArrayList<>();
	private StopWatch runtime = new StopWatch();
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		if(command.equals("addArea") || command.equals("gen"))
		{
			this.command = command;
			condition = true;
		}
	}

	@Override
	public void onPaint(Graphics2D g) 
	{
		if(gui != null)
		gui.setRuntime(runtime.getRuntimeAsString());
	}

	@Override
	public void onStart(String... args)
	{
		getEventDispatcher().addListener(this);
		gui = new RaysWebCreatorGUI();
		player = Players.getLocal();
		gui.setVisible(true);
		runtime.start();
		gui.setListener(this);
	}
	
	@Override
	public void onLoop() 
	{
		condition = false;
		System.out.println("Waiting...");
		gui.setStatus("Waiting...");
		Execution.delayUntil(() -> condition);
		if(command.equals("addArea"))
		{
			addArea();
		}
		if(command.equals("gen"))
			gen();
		if(command.equals("reset"))
			reset();
			
	}
	
	public void addArea()
	{
		System.out.println("Adding area");
		int radius = getRadius();
		position = player.getPosition();
		Collection<Coordinate> collection = position.getReachableCoordinates();
		Iterator<Coordinate> iterator = collection.iterator();
		while(iterator.hasNext())
		{
			Coordinate c = iterator.next();
			if((c.distanceTo(player) <= radius) && !vertices.containsKey(c))
			{
				GameObject object = GameObjects.getLoadedAt(c).first();
				if((object == null) || !object.getDefinition().impassable())
				{
					CoordinateVertex v = new CoordinateVertex(c);
					vertices.put(c, v);
				}
			}
		}	
	}
	
	public void gen()
	{
		gui.setStatus("genning...");
		System.out.println("genning...");
       	int[][] ar = new int[][]
				{
				{-1, 0, 1}, {-1, 0, 1}
				};
       	vertices.forEach((Coordinate key, WebVertex vertex) ->
       	{
       		for(int i = 0; i < 3; i++)
       		{
       			for(int k = 0; k < 3; k++)
       			{
       				if(!(i == 1 && k == 1))
       				{
           				Coordinate c = key.derive(ar[0][i], ar[1][k]);
           				if(vertices.containsKey(c))
    					{
    						vertex.addBidirectionalEdge(vertices.get(c));
    					}
       				}
       			}
       		}
       		list.add(vertex);
       	});
		FileWeb web = new FileWeb();
		web.addVertices(list);
		try(FileOutputStream fos = new FileOutputStream("webbset.web"))
		{
			fos.write(web.toByteArray());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void reset()
	{
		vertices = new HashMap<>();
	}
	
	public int getRadius()
	{
		return Integer.parseInt(gui.getRadius());
	}
	
}
