package com.paradigmcreatives.ccl;


import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.List;

public class Schedule implements CommandListener {

	CCL midlet;
	private Display display;
	private List list;
	Command exit = new Command("Back", Command.EXIT, 1);
	Command back = new Command("Exit", Command.BACK, 1);
	
	public Schedule(CCL midlet) {
	
		this.midlet = midlet;

		try {
			String string = "TENTATIVESCHEDULE";
			int length = string.length();
			list = new List("TENTATIVESCHEDULE", Choice.IMPLICIT);
			list.addCommand(exit);
			list.addCommand(back);
			list.setCommandListener(this);
			for (int i = 0; i < length-1; i++) {
			Font f = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
			list.setFont(i, f);
			}
		} catch (IndexOutOfBoundsException iob) {
			System.out.println("The error is:" + iob);
		}

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
		display = Display.getDisplay(midlet);
		display.setCurrent(list);
	}

	public void commandAction(Command c, Displayable d) {

		if( c == exit & (d.equals(list)))
		{
			display.setCurrent(midlet.mCanvas);
		}	
	}
	
	}
