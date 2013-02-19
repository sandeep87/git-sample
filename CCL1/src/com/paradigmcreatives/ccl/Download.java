package com.paradigmcreatives.ccl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;



public class Download implements Runnable{

	String url;
	String id;
	String title;
	private CCL MIDlet;
	private Image image = null;
	Form mForm = new Form("Photo Albums");
	Command back = new Command("Exit", Command.EXIT, 1);
	Command more = new Command("More photo albums", Command.SCREEN, 1);
	static int i = 0;
	Vector vector = new Vector(5, 2);
	
	public Download(String url, String id, String title, CCL MIDlet) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.id = id;
		this.title = title;
		this.MIDlet = MIDlet;
		mForm.addCommand(back);
		mForm.addCommand(more);
		mForm.setCommandListener(MIDlet);
	}
	public void run() {
		// TODO Auto-generated method stub
		try
	    {
	      getImage(url);
	     // Thread.sleep(10);
	    }
	     catch (IOException e) {
			// TODO Auto-generated catch block
			getimage1();
		      System.err.println("Msg1: " + e.toString());
		}  
		catch (ArrayIndexOutOfBoundsException e)
	    { 
	    	e.printStackTrace();
	    }	
	}
	private void getimage1() {
		// TODO Auto-generated method stub
		try {
			image = Image.createImage("/ThumbnailNoData3.png");
			vector.addElement(image);
			//Display.getDisplay(MIDlet).setCurrent(mList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	public void start()
	  {
	
	    Thread thread = new Thread(this);
	    try
	    {
	      thread.start();
	      
	    }
	    catch (Exception e)
	    {
	    }
	  }
	private void getImage(String url) throws IOException
	  {
	    ContentConnection connection = (ContentConnection) Connector.open(url);
	    
	    
	    DataInputStream iStrm = connection.openDataInputStream();    
	    
	    System.out.println("Datainput stream"+"  "+iStrm);
	    
	    ByteArrayOutputStream bStrm = null;
	   
	    Image im = null;

	    try
	    {
	      byte imageData[];      
	      int length = (int) connection.getLength();
	      if (length != -1)
	      {
	        imageData = new byte[length];      
	        iStrm.readFully(imageData);
	       
	      }
	      else  
	      {       
	        bStrm = new ByteArrayOutputStream();
	        
	        int ch;
	        while ((ch = iStrm.read()) != -1)
	         
	        	bStrm.write(ch);
	        
	        imageData = bStrm.toByteArray();
	      }
	 
	      im = Image.createImage(imageData, 0, imageData.length);        
	    }catch (OutOfMemoryError e) {
			// TODO: handle exception
	    	System.err.println("In out of memory error"+"  : "+e.toString()); 	
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Creating image"+" : "+e.toString());
		}
	    finally
	    {
	      // Clean up
	      if (connection != null)
	        connection.close();      
	      if (iStrm != null)
	        iStrm.close();
	      if (bStrm != null)
	        bStrm.close();                        
	    }
	    if (im == null)
		     // getimage1();
	    	System.out.println("No image available");
		    else
		    {		
		    	
		    	vector.addElement(im);  	
		    }
	   
	    MIDlet.showImage(vector);
	  }
	
	}

