package com.paradigmcreatives.ccl;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Graphics;

public class LoadingBar {

	public long stepInterval = 250L;
	
	public int width = 0;
	public int height = 0;
	
	int padding = 0;
	
	int color = 0x000000;
	
	int squares = 0;
	int squareWidth = 0;
	
	int currentSquares = 0;
	
	Timer stepTimer = null;
	
	public LoadingBar(int width, int height, int padding, int squares, int color)
	{
		this.width = width;
		this.height = height;
		this.squares = squares;
		
		this.color = color;
		this.padding = padding;
		
		this.squareWidth = (width - padding) / (squares) - padding;
	}
	public void paint(Graphics g)
	{
		g.setColor(color);
		
		for(int i = 0; i < currentSquares; i++)
		{
			g.fillRect(i * (squareWidth + padding), 0, squareWidth, height);
		}
	}
	public void start()
	{
		stepTimer = new Timer();
		
		stepTimer.schedule(new TimerTask() 
			{
				public void run()
				{
					step();
				}
			},
			stepInterval, stepInterval
		);
	}
	public void stop()
	{
		stepTimer.cancel();
	}
	void step()
	{
		currentSquares = (currentSquares + 1) % (squares + 1);
	}

}
