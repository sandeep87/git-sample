package com.paradigmcreatives.ccl;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

public class PhotoAlbum implements CommandListener,ItemCommandListener{

	CCL midlet;
	static int counter = 5;
	String[] Titles = new String[]{};
	Download mDownload;
	//PhotoAlbumsData mData = new PhotoAlbumsData();
	Vector Pvector = new Vector(5,1);
	Form mfForm = new Form("Photos");
	Command press = new Command("Press",Command.ITEM,1);
	Command back = new Command("Back", Command.BACK, 1);
	StringItem item = new StringItem("", "More Albums ?", Item.BUTTON);
	
	
	
	public PhotoAlbum(CCL midlet, Vector vector) {
		Pvector = vector;
		this.midlet = midlet;
		item.setDefaultCommand(press);
		item.setItemCommandListener(this);
		mfForm.append(item);
		mfForm.addCommand(back);
		mfForm.setCommandListener(this);
		for(int i = 0 ; i < 5 ; i++){
			
			PhotoAlbumsData mData = (PhotoAlbumsData) vector.elementAt(i);
			
			String title = mData.getAlbum_title();
			
			
			
			String thumb = mData.getAlbum_thumb();	
			
			String findreplace = "celebrity_cricket_league";
			int indx = thumb.indexOf(findreplace);
			thumb = thumb.substring(0, indx) + "traningtest" 
					+ thumb.substring(indx+findreplace.length());	
			
			thumb = thumb.substring(0, thumb.length()-4) + ".png";
			
			System.out.println("New URL "+"  : "+thumb);	

			mDownload = new Download(thumb,this);
			
			mDownload.start();	
		}
		
	}

	public void commandAction(Command c, Item arg1) {
		
		if(c == press){
			counter = counter+5;
			System.out.println(counter);
			if(counter <= 10){
				for(int i = 5 ; i < counter ; i++){
					
					PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
					
					String thumb = mData.getAlbum_thumb();	
					
					String findreplace = "celebrity_cricket_league";
					int indx = thumb.indexOf(findreplace);
					thumb = thumb.substring(0, indx) + "traningtest" 
							+ thumb.substring(indx+findreplace.length());	
					
					thumb = thumb.substring(0, thumb.length()-4) + ".png";
					
					System.out.println("New URL "+"  : "+thumb);	

					mDownload = new Download(thumb,this);
					
					mDownload.start();	
				}
			}else if (counter <= 15) {
                  for(int i = 10 ; i < counter ; i++){
					
					PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
					
					String thumb = mData.getAlbum_thumb();	
					
					String findreplace = "celebrity_cricket_league";
					int indx = thumb.indexOf(findreplace);
					thumb = thumb.substring(0, indx) + "traningtest" 
							+ thumb.substring(indx+findreplace.length());	
					
					thumb = thumb.substring(0, thumb.length()-4) + ".png";
					
					System.out.println("New URL "+"  : "+thumb);	

					mDownload = new Download(thumb,this);
					
					mDownload.start();	
			}
		}else if (counter <= 20) {
            for(int i = 15 ; i < counter ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}else if (counter <= 25) {
            for(int i = 20 ; i < counter ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}
		else if (counter <= 30) {
            for(int i = 25 ; i < counter ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}
		else if (counter <= 35) {
            for(int i = 30 ; i < counter ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}
		else if (counter <= 40) {
            for(int i = 35 ; i < counter ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}
		else if (counter <= 45) {
            for(int i = 40 ; i < counter ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}else if (counter <= 50) {
            for(int i = 45 ; i < Pvector.size() ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) Pvector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("New URL "+"  : "+thumb);	

				mDownload = new Download(thumb,this);
				
				mDownload.start();
		}
		}else if (counter >= Pvector.size()) {
			
			Alert mAlert = new Alert("Info", "Albums are ended", null, AlertType.INFO);
			
			mAlert.setTimeout(2000);
			Display.getDisplay(midlet).setCurrent(mAlert, mfForm);
			System.out.println("Albums are ended");
		}

		}	
		
	}

	public void commandAction(Command c, Displayable d) {
		
		if(c == back & (d.equals(mfForm))){
			
			Display.getDisplay(midlet).setCurrent(midlet.mCanvas);
		}
		
	}

	public void showImage(Vector vector) {
		for(int i = 0 ; i < vector.size() ; i++){
			
			System.out.println("Pvector size"+"  :  "+Pvector.size());
			PhotoAlbumsData mPhotoAlbumsData = (PhotoAlbumsData) Pvector.elementAt(i);
			
			mfForm.append((Image)vector.elementAt(i));
			//mfForm.append(mPhotoAlbumsData.getAlbum_title());
		}
		
		Display.getDisplay(midlet).setCurrent(mfForm);
		
	}


}
