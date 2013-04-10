package com.paradigmcreatives.ccl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.lcdui.Image;

public class SliderDownload implements Runnable {

	String url;
	CCL midlet;
	Vector vector = new Vector(5, 2);

	public SliderDownload(String url, CCL midlet) {

		this.url = url;
		this.midlet = midlet;

	}

	public void run() {

		try {
			getImage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		Thread thread = new Thread(this);
		try {
			thread.start();
		} catch (Exception e) {
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
			System.err
					.println("In out of memory error" + "  : " + e.toString());
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
			// getimage1();
			System.out.println("No image available");
		else {
			vector.addElement(im);
		}
		midlet.hSliderimages(vector);

	}

}
