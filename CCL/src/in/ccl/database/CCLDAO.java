package in.ccl.database;

import in.ccl.logging.Logger;

import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class CCLDAO {

	private static SQLiteDatabase sqlDB;

	private static CCLDBHandler dbHandler;

	private static final String TAG = "CCLDAO";

	public CCLDAO (Context context) {
		dbHandler = new CCLDBHandler(context);
	}

	public void init () {
		try {
			sqlDB = dbHandler.getWritableDatabase();
			CCLItems.init(dbHandler, sqlDB);
		}
		catch (SQLiteException e) {
			Logger.info(TAG, "getting writable database is given exception");
		}
	}

	public static void insertBannerItems (ArrayList <in.ccl.model.Items> bannerList) {
		BannerItems.insertItems(dbHandler, sqlDB, bannerList);
	}

	/**
	 * Gets a collection of all banner items.
	 * 
	 * @return collection of banner items.
	 */
	public static ArrayList <in.ccl.model.Items> getBannerItems () {
		return BannerItems.getBannerItems(dbHandler, sqlDB);
	}

	public ArrayList <Items> getAllVideoItems () {
		return null;
	}

	public ArrayList <Items> getAllPhotoItems () {
		return null;
	}

	public static ArrayList <in.ccl.model.Items> getPhotoGallery () {
		return Items.getItems(dbHandler, sqlDB, 0, Constants.PHOTO_GALLERY);
	}

	public ArrayList <Items> getAllVideoAlbumsItems () {
		return null;
	}

	public static void insertPhotoGalleryItems (ArrayList <in.ccl.model.Items> photoGalleryList) {
		Items.insertItems(dbHandler, sqlDB, photoGalleryList, Constants.PHOTO_GALLERY);
	}

	public static ArrayList <in.ccl.model.Items> getVideoGallery () {
		return Items.getItems(dbHandler, sqlDB, 0, Constants.VIDEO_GALLERY);
	}

	public static void insertVideoGalleryItems (ArrayList <in.ccl.model.Items> videoGalleryList) {
		Items.insertItems(dbHandler, sqlDB, videoGalleryList, Constants.VIDEO_GALLERY);
	}

	public static void insertPhotos (ArrayList <in.ccl.model.Items> photoList) {
		Items.insertItems(dbHandler, sqlDB, photoList, Constants.PHOTO_ITEMS);
	}

	public static ArrayList <in.ccl.model.Items> getVideos (int albumId) {
		return Items.getItems(dbHandler, sqlDB, albumId, Constants.VIDEO_ITEMS);
	}

	public static void insertVideos (ArrayList <in.ccl.model.Items> photoList) {
		Items.insertItems(dbHandler, sqlDB, photoList, Constants.VIDEO_ITEMS);
	}

	public static ArrayList <in.ccl.model.Items> getPhotos (int albumId) {
		return Items.getItems(dbHandler, sqlDB, albumId, Constants.PHOTO_ITEMS);
	}

	public static void insertPages (int totalPages, int key,int album_id) {
		CCLItems.insertPages(dbHandler, sqlDB, totalPages, key,album_id);
	}
}
