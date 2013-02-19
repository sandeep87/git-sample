package com.paradigmcreatives.ccl;


import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;



public class CCL extends MIDlet implements CommandListener {

	private Command photos;
	private Command team;
	private Command videos;
	private Command ownerlounge;
	private Command home;
	private Command schedule;
	private Command backCommand;
	private Command mPbackCommand;
	public static Display mDisplay;
	private List mList;
    private Form Pform,Vform;
    Vector photoVector = new Vector(9);
	public static int currentScreen = Constants.SCREEN_HOMESCREEN;
	public static int currentZone = 0;
	HomeScreenCanvas mCanvas;//The Main Canvas
	PhotoAlbums mPhotoAlbums;
	VideoAlbum mVideoAlbum;
	Slider mSlider;
	Vector hPhotoVector = new Vector(9);
	Vector hVideoVector = new Vector(9);
	Vector hSliderVector = new Vector(9);

	public CCL() {
		// Initialization
		mDisplay = Display.getDisplay(this);
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
		/*mVideoAlbum = new VideoAlbum(this,false);
		mSlider = new Slider(this);
		mPhotoAlbums = new PhotoAlbums(this,false);*/
		CCL.mDisplay.setCurrent(new HomeScreenCanvas());
		mCanvas = new HomeScreenCanvas();
		mCanvas.setTitle("Celebrity Cricket League");
		mCanvas.addCommand(home);
		mCanvas.addCommand(videos);
		mCanvas.addCommand(team);
		mCanvas.addCommand(photos);
		mCanvas.addCommand(schedule);
		mCanvas.addCommand(ownerlounge);
		mCanvas.setCommandListener(this);
		
		hideOpenKeypadCommand();

		mDisplay.setCurrent(mCanvas);
		//mDisplay.setCurrent(new HomeScreenCanvas());
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
		
		Pform.append((Image) vector.elementAt(i));
		
	}

	mDisplay.setCurrent(Pform);	
}

public void showVImage(Vector vector){
	
	for(int i = 0 ; i < vector.size() ; i++){
		
		Vform.append((Image) vector.elementAt(i));
	}
	mDisplay.setCurrent(Vform);
	
}
	public void commandAction(Command command, Displayable disp) {
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

	public void hPhotos(Vector vector) {
		
		hPhotoVector = vector ;
	}

	public void hVideos(Vector vector) {
		
		hVideoVector = vector ;
	}

	public void hSliderimages(Vector vector) {
		
		hSliderVector = vector ;
		
		
	}



}
