package com.paradigmcreatives.ccl;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

public class Schedule implements CommandListener {

	CCL midlet;
	private Display display;
	private CanvasList canvas;
	private List list;
	Command back = new Command("Back", Command.EXIT, 1);
	public Schedule(CCL midlet) {
		
		canvas = new CanvasList();

		try {
			String string = "TENTATIVESCHEDULE";
			int length = string.length();
			list = new List("TENTATIVESCHEDULE", Choice.IMPLICIT);
			for (int i = 0; i < length-1; i++) {
			Font f = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
			list.setFont(i, f);
			}
		} catch (IndexOutOfBoundsException iob) {
			System.out.println("The error is:" + iob);
		}
		//show = new Command("Show", Command.OK, 1);

		list.append("09FEB\tSATURDAY\tKOCHI\n", null);
		list.append("Chennai Rhinos VS Bhojpuri Dabangs", null);
		list.append("3:00pm - 7:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("09FEB\tSATURDAY\tKOCHI\n", null);
		list.append("Kerala Strikers VS Mumbai Heroes", null);
		list.append("7:00pm - 11:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("10FEB\tSUNDAY\tSiliguri\n", null);
		list.append("Veer Marathi VS Karnataka Bulldozers", null);
		list.append("3:00pm - 7:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("10FEB\tSUNDAY\tSiliguri\n", null);
		list.append("Telugu Warriors VS Bengal Tigers", null);
		list.append("7:00pm - 11:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("16FEB\tSATURDAY\tVizag\n", null);
		list.append("Kerala Strikers VS Bhojpuri Dabangs", null);
		list.append("3:00pm - 7:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("16FEB\tSATURDAY\tVizag\n", null);
		list.append("Chennai Rhinos VS Karnataka Bulldozers", null);
		list.append("7:00pm - 11:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("17FEB\tSUNDAY\tVizag\n", null);
		list.append("Veer Marathi VS Bengal Tigers", null);
		list.append("3:00pm - 7:00pm", null);
		list.append("-------------------------------------------", null);
		list.append("17FEB\tSUNDAY\tVizag\n", null);
		list.append("Telugu Warriors VS Mumbai Heroes", null);
		list.append("7:00pm - 11:00pm", null);
		list.append("-------------------------------------------", null);
		try {
		String str = "Chennai Rhinos VS Bhojpuri Dabangs";
		int len = str.length();
		for (int i = 0 ; i < len-1 ; i ++) {
			Font f1 = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
			list.setFont(i, f1);
		}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The error is :"+e);
		}
		
		//list.addCommand(show);
		list.setCommandListener(this);
		int str = list.getSelectedIndex();
		display = Display.getDisplay(midlet);
		display.setCurrent(list);
	}

	public void commandAction(Command c, Displayable d) {
		String label = c.getLabel();
		if (label.equals("Show")) {
			display.setCurrent(canvas);
		}		
	}
	
	class CanvasList extends Canvas implements CommandListener {
		private Image image;
		private Command back;
		int bgColor = 0x000000;

		public CanvasList() {
			back = new Command("Back", Command.BACK, 1);
			addCommand(back);
			setCommandListener(this);
		}

		public void paint(Graphics g) {
			int width = getWidth();
			int height = getHeight();
			String str = list.getString(list.getSelectedIndex());
			/*int length = str.length();
			System.out.println("Length of the string:"+str);*/
			g.setColor(bgColor);
			g.fillRect(0, 0, width, height);

			try {
				image = Image.createImage("/bgimage.png");
				g.drawImage(image, width, height, 0);
				g.fillRect(0, 0, width, height);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.setColor(255, 255, 255);
			g.drawString(str, width / 2, height / 2, Graphics.HCENTER
					| Graphics.TOP);
		}

		public void commandAction(Command c, Displayable d) {
			String label = c.getLabel();
			if (label.equals("Back")) {
				display.setCurrent(midlet.mCanvas);
			}
		}
	}
	
	}
