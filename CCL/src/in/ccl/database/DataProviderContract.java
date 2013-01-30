package in.ccl.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataProviderContract {

	public static final String DATABASE_NAME ="CCLDB";

	public static final int DATABASE_VERSION = 1;

	public static final String CATEGORY_TITLE = "categoryTitle";

	public static final String URL = "imageUrl";

	public static final String THUMB_IMAGE_URL = "thumbImageUrl";

	public static final String ALBUM_ID_COLUMN  = "album_id";
	public static final String CATEGORY_ID  = "cat_id";

	public static final String PAGES_TABLE_NAME = "Pages";

	public static final String TOTAL_PAGES = "total_pages";

	public static final String NEWS_ID = "news_id";
	public static final String NEWS_TITLE = "news_title";
	public static final String NEWS_URL = "news_url";
	public static final String NEWS_CATEGORY = "news_category";


	private DataProviderContract () {
	}

	// The URI scheme used for content URIs
	public static final String SCHEME = "content";

	// The provider's authority
	public static final String AUTHORITY = "in.ccl.database";

	/**
	 * The DataProvider content URI
	 */
	public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

	/**
	 * Table primary key column name
	 */
	public static final String ROW_ID = BaseColumns._ID;

	/**
	 * Modification date table date column name
	 */
	public static final String DATA_DATE_COLUMN = "DownloadDate";

	/**
	 * Modification date table name
	 */
	public static final String DATE_TABLE_NAME = "DateMetadatData";
	
	/**
	 * News date table name
	 */
	public static final String NEWS_TABLE_NAME = "NewsMetadatData";
	
	
	/**
	 * Content URI for news table
	 */
	public static final Uri NEWS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, NEWS_TABLE_NAME);


	/**
	 * Content URI for modification date table
	 */
	public static final Uri DATE_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, DATE_TABLE_NAME);

	/**
	 * Banner table name
	 */
	public static final String BANNERURL_TABLE_NAME = "BannerUrlData";

	/**
	 * photo album table name
	 */
	public static final String PHOTO_ALBUM_TABLE_NAME = "PhotoAlbumData";

	/**
	 * video album table name
	 */
	public static final String VIDEO_ALBUM_TABLE_NAME = "VideoAlbumData";

	/**
	 * It stores photo items data as well video items data.
	 */
	public static final String RAW_TABLE_NAME = "RawData";
	
	/**
	 * It stores photo items data as well video items data.
	 */
	public static final String CATEGORY_TABLE_NAME = "CategoriesData";
	
	/**
	 * Banner table content URI
	 */
	public static final Uri PICTUREURL_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, BANNERURL_TABLE_NAME);

	/**
	 * Photo Album table content URI
	 */
	public static final Uri PHOTO_ALBUM_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, PHOTO_ALBUM_TABLE_NAME);

	/**
	 * Video Album table content URI
	 */
	public static final Uri VIDEO_ALBUM_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, VIDEO_ALBUM_TABLE_NAME);

	/**
	 * Raw table content URI
	 */
	public static final Uri RAW_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, RAW_TABLE_NAME);
	/**
	 * Pages table content URI
	 */
	public static final Uri PAGES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, PAGES_TABLE_NAME);
	/**
	 * category table content URI
	 */
	public static final Uri CATEGORY_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, CATEGORY_TABLE_NAME);

	// Banner table fields.

	/**
	 * Banner table image URL column name
	 */
	public static final String BANNER_IMAGE_URL_COLUMN = "BannerimageUrl";
	/**
	 * Photo album table image URL column name
	 */
	public static final String PHOTO_ALBUM_IMAGE_URL_COLUMN = "photoalbumimageUrl";
	/**
	 * Video album table image URL column name
	 */
	public static final String VIDEO_ALBUM_IMAGE_URL_COLUMN = "videoalbumimageUrl";

	/**
	 * Banner table banner column name
	 */
	public static final String IMAGE_NAME_COLUMN = "bannerImageName";

	/**
	 * Banner table album id column name
	 */
	public static final String BANNER_ALBUM_ID_COLUMN = "bannerAlbumId";

}
