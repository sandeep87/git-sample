package com.paradigmcreatives.ccl;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class HomeScreenCanvas extends MainCanvas {

	private static final int TOP_LEFT = Graphics.TOP | Graphics.LEFT;
	private Image mBackGround;
	private Image header;
	Vector photoVector = new Vector(9);
	private String[] bannerImages = { "/img9.png", "/img20.png", "/img21.png" };
	private String[] photoImages = { "/ccl4.png", "/ccl5.png", "/ccl6.png",
			"/ccl4.png", "/ccl5.png", "/ccl6.png", "/ccl4.png", "/ccl5.png",
			"/ccl6.png" };
	private String[] videoImages = { "/video1.png", "/video2.png",
			"/video3.png", "/video4.png", "/video5.png", "/video6.png",
			"/video7.png", "/video8.png", "/video9.png" };
	private String[] updateIndicator = { "/updates1.PNG", "/updates2.PNG",
			"/updates3.PNG" };
	private String[] photoIndicator = { "/photos1.PNG", "/photos2.PNG",
			"/photos3.PNG" };
	private String[] videoIndicator = { "/videos1.PNG", "/videos2.PNG",
			"/videos3.PNG" };
	private static int bannerPointercounter = 0;
	private static int photoPointercounter = 0;
	private static int videoPointercounter = 0;
	private static Image frame;
	
	
	//VideoAlbum mVideoAlbum;

	HomeScreenCanvas() {

		try {
			// setFullScreenMode(true);
			header = Image.createImage("/header.png");
			mBackGround = Image.createImage("/backg.png");
			frame = Image.createImage("/frame.png");
			//mPhotoAlbums = new PhotoAlbums(this,false);
			//mVideoAlbum = new VideoAlbum(this,false);
		} catch (IOException ioe) {
			// Deal with failure to load image.
		}

	}

	protected void paint(Graphics graphics) {
		try {

			graphics.setColor(0x000000);
			graphics.fillRect(0, 0, getWidth(), getHeight());
			graphics.drawImage(mBackGround, 0, 0, TOP_LEFT);
			graphics.drawImage(header, 0, 0, 0);

			switch (CCL.currentZone) {
			case 0:// BANNER ZONE
				drawBanner(graphics);
				drawPhotos(graphics);
				drawVideos(graphics);
				break;
			case 1:// PHOTO ZONE
				drawBanner(graphics);
				drawPhotos(graphics);
				drawVideos(graphics);
				break;
			case 2:// VIDEO ZONE
				drawBanner(graphics);
				drawPhotos(graphics);
				drawVideos(graphics);

				break;

			}
			// graphics.drawImage(frame, 0, 0, 0);
		} catch (Exception e) {
			System.out.println("Exception in paint is:" + e);
		}

	}

	protected void pointerDragged(int x, int y) {
		// System.out.println("in pointer dragged ");
		// System.out.println(" x =  "+ x);
		// System.out.println(" x =  "+ x);

		/**
		 * Banner Area
		 */
		if (x > HomeScreenBuckets.bannerareax1
				& x < HomeScreenBuckets.bannerareax2
				& y > HomeScreenBuckets.bannerareay1
				& y < HomeScreenBuckets.bannerareay2) {
			CCL.currentZone = Constants.BANNERZone;
			if (bannerPointercounter < 3) {
				bannerPointercounter++;
				// System.out.println("Value of pointercounter in if  "+pointercounter);
				if (bannerPointercounter == 3) {
					bannerPointercounter = 0;
				}

			}

		}// Banner Area

		/**
		 * Photo Zone
		 */
		if (x > HomeScreenBuckets.photoareax1
				& x < HomeScreenBuckets.photoareax2
				& y > HomeScreenBuckets.photoareay1
				& y < HomeScreenBuckets.photoareay2) {
			CCL.currentZone = Constants.PHOTOZone;

			if (photoPointercounter < 3) {
				photoPointercounter++;
				// System.out.println("Value of photoPointercounter in if  "+photoPointercounter);
				if (photoPointercounter == 3) {
					photoPointercounter = 0;
				}

			}
		}

		/**
		 * Video Zone
		 */
		if (x > HomeScreenBuckets.videoareax1
				& x < HomeScreenBuckets.videoareax2
				& y > HomeScreenBuckets.videoareay1
				& y < HomeScreenBuckets.videoareay2) {
			CCL.currentZone = Constants.VIDEOZone;

			if (videoPointercounter < 3) {
				videoPointercounter++;
				// System.out.println("Value of videoPointercounter in if  "+videoPointercounter);
				if (videoPointercounter == 3) {
					videoPointercounter = 0;
				}

			}
		}
		repaint();
	}

	protected void pointerPressed(int x, int y) {

	}

	private void drawBanner(Graphics g) {
		try {
			// System.out.println("22222222222222222222222222222222222222222222 ");
			switch (bannerPointercounter) {

			case 0:
				g.drawImage(Image.createImage(updateIndicator[0]), 0, 30, 0);
				g.drawImage(Image.createImage(bannerImages[0]), 0, 50, TOP_LEFT);

				break;
			case 1:
				g.drawImage(Image.createImage(updateIndicator[1]), 0, 30, 0);
				g.drawImage(Image.createImage(bannerImages[1]), 0, 50, TOP_LEFT);
				break;
			case 2:
				g.drawImage(Image.createImage(updateIndicator[2]), 0, 30, 0);
				g.drawImage(Image.createImage(bannerImages[2]), 0, 50, TOP_LEFT);
				break;
			}
		} catch (Exception e) {
		}
	}

	private void drawPhotos(Graphics g) {
		try {
			switch (photoPointercounter) {
			case 0:
				g.drawImage(Image.createImage(photoIndicator[0]), 0, 150, 0);
				g.drawImage(Image.createImage(photoImages[0]), 0, 170, 0);
				g.drawImage(Image.createImage(photoImages[1]), 80, 170, 0);
				g.drawImage(Image.createImage(photoImages[2]), 160, 170, 0);
				break;
			case 1:
				g.drawImage(Image.createImage(photoIndicator[1]), 0, 150, 0);
				g.drawImage(Image.createImage(photoImages[3]), 0, 170, 0);
				g.drawImage(Image.createImage(photoImages[4]), 80, 170, 0);
				g.drawImage(Image.createImage(photoImages[5]), 160, 170, 0);
				break;
			case 2:
				g.drawImage(Image.createImage(photoIndicator[2]), 0, 150, 0);
				g.drawImage(Image.createImage(photoImages[6]), 0, 170, 0);
				g.drawImage(Image.createImage(photoImages[7]), 80, 170, 0);
				g.drawImage(Image.createImage(photoImages[8]), 160, 170, 0);
			}

		} catch (Exception e) {
		}
	}

	private void drawVideos(Graphics g) {
		try {
			switch (videoPointercounter) {
			case 0:
				g.drawImage(Image.createImage(videoIndicator[0]), 0, 250, 0);
				g.drawImage(Image.createImage(videoImages[0]), 0, 270, 0);
				g.drawImage(Image.createImage(videoImages[1]), 80, 270, 0);
				g.drawImage(Image.createImage(videoImages[2]), 160, 270, 0);
				break;
			case 1:
				g.drawImage(Image.createImage(videoIndicator[1]), 0, 250, 0);
				g.drawImage(Image.createImage(videoImages[3]), 0, 270, 0);
				g.drawImage(Image.createImage(videoImages[4]), 80, 270, 0);
				g.drawImage(Image.createImage(videoImages[5]), 160, 270, 0);
				break;
			case 2:
				g.drawImage(Image.createImage(videoIndicator[2]), 0, 250, 0);
				g.drawImage(Image.createImage(videoImages[6]), 0, 270, 0);
				g.drawImage(Image.createImage(videoImages[7]), 80, 270, 0);
				g.drawImage(Image.createImage(videoImages[8]), 160, 270, 0);
			}

		} catch (Exception e) {

		}
	}

	/*public void showPhotos(Vector vector) {
		// TODO Auto-generated method stub
		hPhotoVector = vector;
		System.out.println(hPhotoVector.size());
	}

	public void showVideos(Vector vector) {
		// TODO Auto-generated method stub
		hVideoVector = vector;
		System.out.println(hVideoVector.size());
	}*/
}
