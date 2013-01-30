package in.ccl.imageloader;

import in.ccl.adapters.GridAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.helper.Util;
import in.ccl.ui.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class EndlessScrollListener implements OnScrollListener {

	private int visibleThreshold = 25;

	private int currentPage = 0;

	private int previousTotal = 0;

	private boolean loading = true;

	public static enum RequestType {
		NO_REQUEST, VIDEO_REQUEST, ALBUM_REQUEST, PHOTO_GALLERY_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	private int itemId;

	private int totalPages;

	private Activity activity;

	public EndlessScrollListener () {
	}

	public EndlessScrollListener (Activity activity, GridAdapter adapter, int itemId, RequestType from, int pages) {
		mRequestType = from;
		this.itemId = itemId;
		this.activity = activity;
		this.totalPages = pages;
		if (adapter != null) {
			currentPage = (int) Math.ceil((double) adapter.getCount() / 25);
		}
	}

	@Override
	public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount >= previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}
		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && totalPages > currentPage) {
			// I load the next page of gigs using a background task,
			// but you can call any function here.
			switch (mRequestType) {
				case PHOTO_GALLERY_REQUEST:
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class);
						String url = activity.getResources().getString(R.string.photo_gallery_url) + itemId + "?page=" + (currentPage + 1);
						mServiceIntent.setData(Uri.parse(url));
						mServiceIntent.putExtra("KEY", "photos_pages");
						activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
					break;
				case VIDEO_REQUEST:
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.video_gallery_url) + itemId + "?page=" + (currentPage + 1)));
						mServiceIntent.putExtra("KEY", "videos_pages");
						activity.startService(mServiceIntent);

					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
					break;

				default:
					break;
			}
			loading = true;
		}
	}

	@Override
	public void onScrollStateChanged (AbsListView view, int scrollState) {
	}

}
