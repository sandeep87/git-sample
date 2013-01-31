package in.ccl.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataProviderContract {

	public static final String DATABASE_NAME = "CCLDB";

	public static final int DATABASE_VERSION = 1;

	public static final String CATEGORY_TITLE = "categoryTitle";

	public static final String URL = "imageUrl";

	public static final String THUMB_IMAGE_URL = "thumbImageUrl";

	public static final String ALBUM_ID_COLUMN = "album_id";

	public static final String CATEGORY_ID = "cat_id";

	public static final String PAGES_TABLE_NAME = "Pages";

	public static final String TOTAL_PAGES = "total_pages";

	public static final String NEWS_ID = "news_id";

	public static final String NEWS_TITLE = "news_title";

	public static final String NEWS_URL = "news_url";

	public static final String NEWS_CATEGORY = "news_category";

	// downnload_image_table fields

	public static final String DOWNLOAD_IMAGE_ID = "image_id";

	public static final String DOWNLOAD_IMAGE_URL = "downloadImage_url";

	public static final String DOWNLOAD_IMAGE_THUMB_URL = "downloadImage_thumb_url";

	public static final String DOWNLOAD_IMAGE_NO_OF_PAGES = "downloadImage_no_of_pages";

//team logo table column names
	public static final String TEAM_ID_COLUMN = "team_id";

	public static final String TEAM_NAME_COLUMN = "team_name";

	public static final String TEAM_LOGO_IMAGE_URL_COLUMN = "team_logo_url";

	public static final String TEAM_BANNER_IMAGE_URL_COLUMN = "team_banner_url";

	// team members table column names
	public static final String TEAM_PERSON_ID_COLUMN = "team_persion_id";

	public static final String TEAM_PERSON_NAME_COLUMN = "team_person_name";

	public static final String TEAM_MEMBER_IMAGE_URL_COLUMN = "team_person_thumbUrl";
	
	public static final String TEAM_NAME_MEMBER_COLUMN = "team_name_member";


	public static final String TEAM_PERSON_ROLE_COLUMN = "team_person_role";

	
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
	 * teams logo table name
	 */
	public static final String TEAMS_LOGO_TABLE_NAME = "TeamsLogoData";

	/**
	 * team members table name
	 */
	public static final String TEAM_MEMBERS_TABLE_NAME = "TeamsMembersData";

	/**
	 * Content URI for news table
	 */
	public static final Uri NEWS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, NEWS_TABLE_NAME);


	/**
	 * Download image data table name
	 */

	public static final String DOWNLOAD_IMAGE_TABLE_NAME = "DownloadImageData";

	/**
	 * Team logo table content URI
	 */
	public static final Uri TEAMS_LOGO_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, TEAMS_LOGO_TABLE_NAME);

	/**
	 * Team Member table content URI
	 */
	public static final Uri TEAM_MEMBERS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, TEAM_MEMBERS_TABLE_NAME);

	/**
	 * Content URI for Download image table
	 */

	public static final Uri DOWNLOAD_IMAGE_TABLE_CONTENTURI = Uri
			.withAppendedPath(CONTENT_URI, DOWNLOAD_IMAGE_TABLE_NAME);

	
	
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
	public static final Uri BANNERURL_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, BANNERURL_TABLE_NAME);

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
