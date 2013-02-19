package com.paradigmcreatives.ccl;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class CCL extends MIDlet implements CommandListener {

	private Command photos;
	private Command team;
	private Command videos;
	private Command ownerlounge;
	private Command home;
	private Command schedule;
	 Command backCommand;
	private Command mPbackCommand;
	private Display mDisplay;
	private List mList;
    private Form Pform,Vform;
	Image im = null;
	ScrollCanvas mCanvas;

	public CCL() {
		// TODO Auto-generated constructor stub
		
		photos = new Command("PHOTOS", Command.SCREEN, 1);
		mList = new List("Photos album", List.EXCLUSIVE);
		team = new Command("TEAMS", Command.SCREEN, 1);
		backCommand = new Command("Back", Command.EXIT, 2);
		videos = new Command("VIDEOS", Command.SCREEN, 1);
		schedule = new Command("SCHEDULE", Command.SCREEN, 1);
		ownerlounge = new Command("OWNER'S LOUNGE", Command.SCREEN, 1);
		home = new Command("HOME", Command.SCREEN, 1);
		
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
		// mDisplay.setCurrent(mForm);

		mCanvas = new ScrollCanvas();
		mCanvas.setTitle("Celebrity Cricket League");
		mCanvas.addCommand(home);
		mCanvas.addCommand(videos);
		mCanvas.addCommand(team);
		mCanvas.addCommand(photos);
		mCanvas.addCommand(schedule);
		mCanvas.addCommand(ownerlounge);
		mCanvas.setCommandListener(this);
		hideOpenKeypadCommand();
		mDisplay = Display.getDisplay(this);
		mDisplay.setCurrent(mCanvas);
		
		Pform = new Form("Photo Albums");
		Pform.addCommand(backCommand);
		Pform.setCommandListener(this);
		
		Vform = new Form("Video Albums");
		Vform.addCommand(backCommand);
		Vform.setCommandListener(this);
		
		mList.addCommand(backCommand);
		mList.setCommandListener(this);	
		
		
		
	}
public void showImage(Vector vector){

	for(int i = 0 ; i < vector.size() ; i++){
		
		Image mImage = (Image) vector.elementAt(i);
		//mList.append("", (Image) vector.elementAt(i));
		
		Pform.append((Image) vector.elementAt(i));
		//Pform.append((Item) vector.elementAt(i));
	}
	//mDisplay.setCurrent(mList);
	mDisplay.setCurrent(Pform);	
}

public void showVImage(Vector vector){
	
	for(int i = 0 ; i < vector.size() ; i++){
		
		//mList.append("", (Image) vector.elementAt(i));
		Vform.append((Image) vector.elementAt(i));
	}
	//mDisplay.setCurrent(mList);
	mDisplay.setCurrent(Vform);
	
}
	public void commandAction(Command command, Displayable arg1) {
		// TODO Auto-generated method stub
		if (command == home) {
			mDisplay.setCurrent(mCanvas);
		} else if (command == videos) {	
			VideoAlbum	mVideos = new VideoAlbum(this);
		}else if (command == backCommand) {
			Display.getDisplay(this).setCurrent(mCanvas);
		}else if (command == mPbackCommand) {
			Display.getDisplay(this).setCurrent(mCanvas);
		}else if (command == photos) {
			PhotoAlbums	mPhotos = new PhotoAlbums(this);
		}else if (command == team) {
			Teams mTeams = new Teams(this);
		}else if (command == ownerlounge) {
			OwnersLounge lounge = new OwnersLounge(this);
		}else if (command == schedule) {
			Schedule schedule = new Schedule(this);
		}
	}
	public void Show(){
		mDisplay.setCurrent(mCanvas);
	}
	 private void hideOpenKeypadCommand() {
	    	
	        try {
	            String keyboardType = System.getProperty("com.nokia.keyboard.type");
	            if (keyboardType.equals("None")) {
	                // Full touch device detected
	                com.nokia.mid.ui.VirtualKeyboard.hideOpenKeypadCommand(true);
	            }
	        } catch (Exception e) {
	            // Do nothing
	        }
	        
	    }

	public class ScrollCanvas extends Canvas {

		private static final int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
		private static final int VCENTER_HCENTER = Graphics.VCENTER
				| Graphics.HCENTER;
		private Image mPhotos;
		private Image mBackGround;
		private Image mBanner;
		private Image mVideos;
		private int imgX;
		private int imgY;

		public ScrollCanvas() {
			try {

				mBackGround = Image.createImage("/backg.png");
				/*mBanner = (Image) photoVector.elementAt(0);
				mPhotos = Image.createImage("/img12.png");
				mVideos = Image.createImage("/img11.png");*/

			} catch (IOException ioe) {
				// Deal with failure to load image.
			}
		}

		protected void paint(Graphics graphics) {
			// TODO Auto-generated method stub
			graphics.setColor(0x000000);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.drawImage(mBackGround, 0, 0, TOP_LEFT);
			//graphics.drawImage(mBanner, 0, 20, TOP_LEFT);
			//graphics.drawImage(mPhotos, 0, 230, graphics.BOTTOM | graphics.LEFT);
			//graphics.drawImage(mVideos, 0, 320, graphics.BOTTOM | graphics.LEFT);
		}
		
		protected void pointerDragged(int arg0, int arg1) {
			// TODO Auto-generated method stub
			super.pointerDragged(arg0, arg1);
		}

	}


}
