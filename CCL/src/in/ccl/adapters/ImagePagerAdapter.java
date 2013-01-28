package in.ccl.adapters;

import in.ccl.database.CCLPullService;
import in.ccl.database.PhotoAlbumCurosr;
import in.ccl.database.VideoAlbumCursor;
import in.ccl.helper.Category;
import in.ccl.helper.Util;
import in.ccl.imageloader.DisplayImage;
import in.ccl.logging.Logger;
import in.ccl.model.Items;
import in.ccl.photo.PhotoView;
import in.ccl.ui.MenuItems;
import in.ccl.ui.PhotoAlbumActivity;
import in.ccl.ui.R;
import in.ccl.ui.TeamActivity;
import in.ccl.ui.VideoAlbumActivity;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class ImagePagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;

	private ArrayList <Items> itemsList;

	private Activity activity;

	private Category mCategory;

	public static String AlbumTitle;

	public static int photoGalleryId;

	private static final int VIEW_PAGER_PAGE_COUNT = 3;

	protected static final String TAG = "ImagePagerAdapter";

	private enum RequestType {
		NO_REQUEST, PHOTOGALLERY_REQUEST, VIDEOGALLERY_REQUEST, TEAMLOGO_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	@Override
	public void destroyItem (ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public ImagePagerAdapter (Activity ctx, ArrayList <Items> list, Category category) {
		activity = ctx;
		itemsList = list;
		mCategory = category;
		inflater = activity.getLayoutInflater();

	}

	@Override
	public int getCount () {
		if (itemsList.size() > 0) {
			return VIEW_PAGER_PAGE_COUNT;
		}
		return 0;
	}

	@Override
	public View instantiateItem (View view, final int position) {
		View imageLayout = null;
		//ImageView imageView = null;
		PhotoView imageView = null;
		TextView errorTitleText = null;

		ImageView loadingImage = null;

		switch (mCategory) {

			case BANNER:
				mRequestType = RequestType.NO_REQUEST;
				imageLayout = inflater.inflate(R.layout.item_pager_image, null);
				imageView = (PhotoView) imageLayout.findViewById(R.id.image);
				errorTitleText = (TextView)imageLayout.findViewById(R.id.error_title);
			//	imageView = (ImageView) imageLayout.findViewById(R.id.image);

				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick (View v) {

						try {
							AlbumTitle = itemsList.get(position).getTitle();
							photoGalleryId = itemsList.get(position).getAlbumId();
							ArrayList <Items> list = PhotoAlbumCurosr.getPhotos(activity, photoGalleryId);
							if (list == null || list.size() <= 0) {
								if (Util.getInstance().isOnline(activity)) {
									Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.photo_gallery_url) + itemsList.get(position).getAlbumId()));
									mServiceIntent.putExtra("KEY", "photos");
									activity.startService(mServiceIntent);

									// asyncTask.execute(activity.getResources().getString(R.string.photo_gallery_url) + itemsList.get(index).getId());
								}
								else {
									Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
								}
							}
							else {
								Intent photoAlbumIntent = new Intent(activity, PhotoAlbumActivity.class);
								photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ITEMS, list);
								photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, photoGalleryId);
								photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
								activity.startActivity(photoAlbumIntent);
							}

						}
						catch (ArrayIndexOutOfBoundsException e) {
							Logger.info(TAG, e.toString());
						}
						catch (IndexOutOfBoundsException e) {
							e.printStackTrace();
							Logger.info(TAG, e.toString());
						}
					}
				});
				// loadingImage = (ImageView) imageLayout.findViewById(R.id.loading);
				imageView.setTag(itemsList.get(position).getPhotoOrVideoUrl());
				imageView.setImageURL(itemsList.get(position).getPhotoOrVideoUrl(), true, activity.getResources().getDrawable(R.drawable.imagenotqueued), errorTitleText);
				//DisplayImage displayImage = new DisplayImage(itemsList.get(position).getPhotoOrVideoUrl(), imageView, activity, "banner");
			//	displayImage.show();
				break;
			case PHOTO:
				mRequestType = RequestType.PHOTOGALLERY_REQUEST;
				imageLayout = load("photo_gallery", position);
				break;
			case VIDEO:
				mRequestType = RequestType.VIDEOGALLERY_REQUEST;
				imageLayout = load("video_gallery", position);
				break;
			case FULL_SCREEN:
				imageLayout = inflater.inflate(R.layout.item_pager_image, null);
				imageView = (PhotoView) imageLayout.findViewById(R.id.image);
				loadingImage = (ImageView) imageLayout.findViewById(R.id.loading);

				imageView.setTag(itemsList.get(position).getPhotoOrVideoUrl());
				DisplayImage displayImage = new DisplayImage(itemsList.get(position).getPhotoOrVideoUrl(), imageView, activity, null);
				displayImage.show();
				break;
			case TEAM_LOGO:
				mRequestType = RequestType.TEAMLOGO_REQUEST;
				imageLayout = load("team_logo", position);
				break;
			case TEAM_MEMBERS:
				// mRequestType = RequestType.TEAM_MEMBER_REQUEST;
				imageLayout = load("team_member_image", position);

				break;

			default:
				break;
		}
		((ViewPager) view).addView(imageLayout, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return imageLayout;
	}

	private View load (final String from, final int position) {
		View view = inflater.inflate(R.layout.grid_view, null);
		GridView gridView = (GridView) view.findViewById(R.id.grid_view);
		final ArrayList <Items> items = new ArrayList <Items>();
		try {
			items.add(itemsList.get(VIEW_PAGER_PAGE_COUNT * position));
			items.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 1));
			items.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 2));
			// options = new DisplayImageOptions.Builder().showStubImage(R.drawable.stub_image).showImageForEmptyUri(R.drawable.image_for_empty_url).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		}
		catch (IndexOutOfBoundsException e) {
			Logger.info(TAG, "Loading empty page ");
		}
		GridAdapter adapter = new GridAdapter(activity, items, from);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View arg1, int pos, long arg3) {
				// DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, ImagePagerAdapter.this, false);
				int index = (2 * position) + (position + pos);
				if (from.equals("video_gallery")) {
					try {

						AlbumTitle = itemsList.get(index).getTitle();
						photoGalleryId = itemsList.get(index).getId();
						ArrayList <Items> list = VideoAlbumCursor.getVideos(activity, photoGalleryId);
						if (list == null || list.size() <= 0) {
							if (Util.getInstance().isOnline(activity)) {
								Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.video_gallery_url) + itemsList.get(index).getId()));
								mServiceIntent.putExtra("KEY", "videos");
								activity.startService(mServiceIntent);
								// asyncTask.execute(activity.getResources().getString(R.string.video_gallery_url) + itemsList.get(index).getId());
							}
							else {
								Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
							}
						}
						else {
							Intent videoGalleryIntent = new Intent(activity, VideoAlbumActivity.class);
							videoGalleryIntent.putExtra(Constants.EXTRA_VIDEO_ITEMS, list);
							videoGalleryIntent.putExtra(Constants.EXTRA_ALBUM_ID, photoGalleryId);
							videoGalleryIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
							activity.startActivity(videoGalleryIntent);

						}
					}
					catch (ArrayIndexOutOfBoundsException e) {
						Logger.info(TAG, e.toString());
					}
					catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
						Logger.info(TAG, e.toString());
					}
				}
				else if (from.equals("photo_gallery")) {

					try {
						AlbumTitle = itemsList.get(index).getTitle();
						photoGalleryId = itemsList.get(index).getId();
						ArrayList <Items> list = PhotoAlbumCurosr.getPhotos(activity, photoGalleryId);
						if (list.size() <= 0) {
							if (Util.getInstance().isOnline(activity)) {
								System.out.println("Server requesting...");
								Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.photo_gallery_url) + itemsList.get(index).getId()));
								mServiceIntent.putExtra("KEY", "photos");
								activity.startService(mServiceIntent);

								// asyncTask.execute(activity.getResources().getString(R.string.photo_gallery_url) + itemsList.get(index).getId());
							}
							else {
								Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
							}
						}
						else {
							Intent photoAlbumIntent = new Intent(activity, PhotoAlbumActivity.class);
							photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ITEMS, list);
							photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, photoGalleryId);
							photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
							activity.startActivity(photoAlbumIntent);
						}

					}
					catch (ArrayIndexOutOfBoundsException e) {
						Logger.info(TAG, e.toString());
					}
					catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
						Logger.info(TAG, e.toString());
					}
				}
				else if (from.equals("team_logo")) {

					// for team memberes
					if (index == 0) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.teamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 0);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 0);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 1) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.chennaiTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 0);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 1);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 2) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.teluguTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 0);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 2);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 3) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.karnatakaTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 1);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 3);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 4) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.keralaTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 1);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 4);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 5) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.bengalTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 1);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 5);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 6) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.marathiTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 2);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 6);
						activity.startActivity(teamIntent);
						activity.finish();
					}
					else if (index == 7) {
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMLOGO_KEY, MenuItems.teamLogosList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMROLE_KEY, MenuItems.teamRolesList);
						teamIntent.putParcelableArrayListExtra(Constants.EXTRA_TEAMMEMBER_KEY, MenuItems.bhojpuriTeamMembersList);
						teamIntent.putExtra(Constants.EXTRA_TEAMNAME, MenuItems.teamLogosList.get(index).getTitle());
						teamIntent.putExtra(Constants.EXTRA_KEY, 2);
						teamIntent.putExtra(Constants.EXTRA_LOGO_POSITION, 7);
						activity.startActivity(teamIntent);
						activity.finish();
					}

				}

			}

		});
		return view;
	}

	@Override
	public boolean isViewFromObject (View view, Object object) {
		return view.equals(object);
	}

}
