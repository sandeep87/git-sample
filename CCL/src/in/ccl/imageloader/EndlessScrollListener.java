package in.ccl.imageloader;

import in.ccl.adapters.GridAdapter;
import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.net.DownLoadAsynTask;
import in.ccl.parser.CCLParser;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class EndlessScrollListener implements ServerResponse, OnScrollListener {

	private int visibleThreshold = 5;

	private int currentPage = 0;

	private int previousTotal = 0;

	private boolean loading = true;

	private GridAdapter adapter;

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
		this.adapter = adapter;
		mRequestType = from;
		this.itemId = itemId;
		this.activity = activity;
		this.totalPages = pages;
	}

	@Override
	public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount > previousTotal) {
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

						DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, this, true);
						asyncTask.execute(activity.getResources().getString(R.string.photo_gallery_url) + itemId + "?page=" + (currentPage + 1));

					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
					break;
				case VIDEO_REQUEST:
					if (Util.getInstance().isOnline(activity)) {
						DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, this, true);
						asyncTask.execute(activity.getResources().getString(R.string.video_gallery_url) + itemId + "?page=" + (currentPage + 1));
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

	@Override
	public void setData (String result) {
		if (result != null) {
			switch (mRequestType) {
				case PHOTO_GALLERY_REQUEST:

					ArrayList <Items> items = CCLParser.photoParser(result);
					adapter.updateList(items);
					break;
				case VIDEO_REQUEST:
					items = CCLParser.videoAlbumParser(result);
					adapter.updateList(items);
					break;

				default:
					break;
			}
		}
		else {
			Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
		}

	}

}
