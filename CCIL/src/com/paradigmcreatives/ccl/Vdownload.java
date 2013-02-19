package com.paradigmcreatives.ccl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.lcdui.Image;

public class Vdownload implements Runnable {

	String url;
	String url1;
	private CCL MIDlet;
	private Image image = null;
	HomeScreenCanvas homeScreenCanvas;
	Vector vector = new Vector(5, 1);
	boolean value = true;

	public Vdownload(String url, CCL MIDlet) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.MIDlet = MIDlet;
	}

	public Vdownload(String thumb, CCL midlet, boolean b) {

		value = b;
		url1 = thumb;
		MIDlet = midlet;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			if (value) {
				getImage(url);
			} else {
				getImage(url1);
			}
		}

		catch (Exception e) {

			System.err.println("Msg: " + e.toString());
			getimage1();

		}
	}

	public void start() {
		Thread thread = new Thread(this);
		try {
			thread.start();
		} catch (Exception e) {
		}
	}

	private void getimage1() {
		// TODO Auto-generated method stub
		try {
			image = Image.createImage("/ThumbnailNoData3.png");
			vector.addElement(image);
			// Display.getDisplay(MIDlet).setCurrent(mList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void getImage(String url) throws IOException {
		ContentConnection connection = (ContentConnection) Connector.open(url);

		DataInputStream iStrm = connection.openDataInputStream();

		ByteArrayOutputStream bStrm = null;

		Image im = null;

		try {
			byte imageData[];
			int length = (int) connection.getLength();
			if (length != -1) {
				imageData = new byte[length];
				iStrm.readFully(imageData);

			} else {
				bStrm = new ByteArrayOutputStream();

				int ch;
				while ((ch = iStrm.read()) != -1)

					bStrm.write(ch);

				imageData = bStrm.toByteArray();
			}

			im = Image.createImage(imageData, 0, imageData.length);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			System.err.println("Msg2:" + "  : " + e);
			getimage1();
		} finally {
			// Clean up
			if (connection != null)
				connection.close();
			if (iStrm != null)
				iStrm.close();
			if (bStrm != null)
				bStrm.flush();
				bStrm.close();
		}
		if (im == null)
			getimage1();
		else {
			vector.addElement(im);
		}
		if (value)
			MIDlet.hVideos(vector);
		else {
			System.out.println("in home videos");
			MIDlet.hVideos(vector);
			
		}

	}
}
