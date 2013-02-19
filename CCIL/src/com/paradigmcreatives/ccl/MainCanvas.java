package com.paradigmcreatives.ccl;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

public class MainCanvas extends Canvas{
	
	MIDlet midlet;
	Displayable HomeScreenCanvas;
	
	MainCanvas()
	{
		
		
	}
	MainCanvas(MIDlet mid)
	{
		midlet = mid;
	}

	protected void paint(Graphics arg0) {
		if( CCL.currentScreen == Constants.SCREEN_HOMESCREEN)
		{
			
			CCL.mDisplay.setCurrent(new HomeScreenCanvas());
		}
		
	}

}
