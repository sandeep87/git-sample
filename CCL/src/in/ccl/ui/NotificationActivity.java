package in.ccl.ui;

import in.ccl.adapters.NotificationAdapter;
import in.ccl.adapters.NotificationOptionAdapter;
import in.ccl.adapters.TeamGridAdapter;
import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.model.Options;
import in.ccl.model.Players;
import in.ccl.model.Teams;
import in.ccl.model.notification.PlayerNotificationData;
import in.ccl.net.NotificationAsyncTask;
import in.ccl.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gcm.GCMRegistrar;

public class NotificationActivity extends TopActivity implements ServerResponse {

	protected static final String TAG = "NotificationActivity";

	private Facebook mFacebook;
	private Button btnFacebook;
	private TextView txtTextSave;
	private Button btnSave;
	private SharedPreferences mSharedPreferences;
	private int selectedTeamId;
	private int selectedPlayerId;

	private ArrayList<Teams> teamArrayList = new ArrayList<Teams>();
	private ArrayList<Players> allPlayersArrayList = new ArrayList<Players>();
	private ArrayList<Options> optionsArrayList = new ArrayList<Options>();

	public static Handler sHandler;

	private ListView listPlayerNames;

	private ListView listOptions;

	private NotificationAdapter notificationAdapter;

	private NotificationOptionAdapter optionsAdapter;

	private TextView teamName;

	private ViewPager pager;

	private ImageView indicatorOneImage;

	private ImageView indicatorTwoImage;

	private ImageView indicatorThreeImage;

	private ImageView indicatorFourImage;

	private LinearLayout IndicatorLayout;
	private int previousState;

	private int currentState;
	private ArrayList<Players> individualPlayersArryList;
	private RelativeLayout notificationBodyLayout;

	private GridView gridView;

	public enum RequestType {

		NOREQUEST_TYPE, FB_REQUESTTYPE, USER_SOCIALID_REQUESTTYPE, PUSHSTATUS_REQUESTTYPE, PUSH_NOTIFICATIONS_REQUESTTYPE, NOTIFICATIONS_METADATA_REQUESTTYPE, GET_NOTIFICATION_REQUESTTYPE, SETNOTIFICATION_REQUESTTYPE, GET_NOTIFICATION_REREQUEST;
	}

	RequestType mRequestType = RequestType.NOREQUEST_TYPE;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addContent(R.layout.notifications_view);

		teamName = (TextView) findViewById(R.id.txt_team_name);
		listPlayerNames = (ListView) findViewById(R.id.list_player_names);
		listOptions = (ListView) findViewById(R.id.list_options);
		notificationBodyLayout = (RelativeLayout) findViewById(R.id.notification_body_layout);
		pager = (ViewPager) findViewById(R.id.notification_view_pager);
		indicatorOneImage = (ImageView) findViewById(R.id.indicator_one);
		indicatorTwoImage = (ImageView) findViewById(R.id.indicator_two);
		indicatorThreeImage = (ImageView) findViewById(R.id.indicator_three);
		indicatorFourImage = (ImageView) findViewById(R.id.indicator_four);
		IndicatorLayout = (LinearLayout) findViewById(R.id.notification_page_indicator_layout);
		btnFacebook = (Button) findViewById(R.id.btn_facebook);
		btnSave = (Button) findViewById(R.id.btn_save);
		txtTextSave = (TextView) findViewById(R.id.txt_text_save);
		mFacebook = new Facebook(Constants.FACEBOOK_ID);

		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(NotificationActivity.this);

		if (getFacebookUserId() != null) {
			btnFacebook.setVisibility(View.GONE);
			txtTextSave.setVisibility(View.GONE);
			boolean socailStatus = mSharedPreferences.getBoolean(
					"socail_status", false);
			String serverUrl;
			if (socailStatus) {
				serverUrl = getResources().getString(R.string.push_status_url)
						+ getFacebookUserId();
				mRequestType = RequestType.PUSHSTATUS_REQUESTTYPE;

			} else {
				serverUrl = getResources().getString(R.string.social_user_url)
						+ getFacebookUserId();
				mRequestType = RequestType.USER_SOCIALID_REQUESTTYPE;
			}
			if (Util.getInstance().isOnline(NotificationActivity.this)) {
				showOverlay();
				NotificationAsyncTask mNotiFiAsyncTask = new NotificationAsyncTask(
						this, "GET", null, null);
				mNotiFiAsyncTask.execute(serverUrl);
			} else {
				showToastMessage(getResources().getString(
						R.string.network_error_message));
			}

		} else {
			btnFacebook.setVisibility(View.VISIBLE);
			txtTextSave.setVisibility(View.VISIBLE);
		}

		btnFacebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				faceBookAuthorize();
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getFacebookUserId() != null) {
					mRequestType = RequestType.SETNOTIFICATION_REQUESTTYPE;
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put("user_id", getFacebookUserId());
						jsonObject.put("team_id", selectedTeamId);
						jsonObject.put("player_id", selectedPlayerId);
						JSONArray jsonArray = new JSONArray();
						for (int i = 0; i < optionsAdapter.optionsArrayList
								.size(); i++) {
							if (optionsAdapter.optionsArrayList.get(i) != null) {
								if (optionsAdapter.optionsArrayList.get(i)
										.isChecked()) {
									JSONObject object = new JSONObject();
									object.put("options",
											optionsAdapter.optionsArrayList
													.get(i).getOptionId());
									object.put("action",
											optionsAdapter.optionsArrayList
													.get(i).getActionId());
									jsonArray.put(object);
								}
							}
						}
						jsonObject.put("notifications", jsonArray);
						nameValuePairs.add(new BasicNameValuePair("data",
								jsonObject.toString()));
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (Util.getInstance().isOnline(NotificationActivity.this)) {
						optionsAdapter.listItem.clear();
						NotificationAsyncTask mNotificationAsyncTask = new NotificationAsyncTask(
								NotificationActivity.this, "POST",
								nameValuePairs, "SetNotification");
						mNotificationAsyncTask.execute(getResources()
								.getString(R.string.set_notification_url));
					} else {
						showToastMessage(getResources().getString(
								R.string.network_error_message));
					}
				}
			}
		});

		sHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);

				if (msg.what == 0) {
					mSharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(NotificationActivity.this);
					String gcmRegisterId = mSharedPreferences.getString(
							"Gcm_RegisterId", null);
					if (gcmRegisterId != null && getFacebookUserId() != null) {
						mRequestType = RequestType.PUSH_NOTIFICATIONS_REQUESTTYPE;
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								1);
						JSONObject pushNotificationJsonObject = new JSONObject();
						try {
							pushNotificationJsonObject.put("user_id",
									getFacebookUserId());
							pushNotificationJsonObject.put("registered_id",
									gcmRegisterId);
							pushNotificationJsonObject.put("allow", "1");
							pushNotificationJsonObject.put("os", "ANDROID");
						} catch (JSONException e) {
							Log.e(TAG, e.toString());
						}
						try {
							nameValuePairs.add(new BasicNameValuePair("data",
									pushNotificationJsonObject.toString()));
						} catch (UnsupportedOperationException e) {
							Log.e(TAG, e.toString());
						} catch (IllegalArgumentException e) {
							Log.e(TAG, e.toString());
						} catch (ClassCastException e) {
							Log.e(TAG, e.toString());
						}

						if (Util.getInstance().isOnline(
								NotificationActivity.this)) {
							NotificationAsyncTask mNotificationAsyncTask = new NotificationAsyncTask(
									NotificationActivity.this, "POST",
									nameValuePairs, "PushNotification");
							mNotificationAsyncTask.execute(getResources()
									.getString(R.string.push_notification_url));
						} else {
							showToastMessage(getResources().getString(
									R.string.network_error_message));
						}

					}
				}
			}

		};

	}

	public class TeamPagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		public TeamPagerAdapter(ArrayList<Teams> list) {
			teamArrayList = list;
			inflater = NotificationActivity.this.getLayoutInflater();
		}

		@Override
		public int getCount() {
			if (teamArrayList.size() > 4) {
				if (teamArrayList.size() % 4 == 0) {
					return teamArrayList.size() / 4;
				} else {
					return (teamArrayList.size() / 4) + 1;
				}
			}
			return 1;
		}

		@Override
		public View instantiateItem(ViewGroup views, final int position) {

			View imageLayout = null;
			imageLayout = inflater.inflate(R.layout.team_grid_view, null);
			gridView = (GridView) imageLayout.findViewById(R.id.grid_view);

			String[] teamLogoUrls = new String[4];

			if (position == 0) {
				if (teamArrayList != null) {
					teamLogoUrls[0] = teamArrayList.get(0).getLogo();
					teamLogoUrls[1] = teamArrayList.get(1).getLogo();
					teamLogoUrls[2] = teamArrayList.get(2).getLogo();
					teamLogoUrls[3] = teamArrayList.get(3).getLogo();
				} else {
					Log.e("TeamActivity", "Teams Logo are not availble");
				}

			} else {
				if (teamArrayList != null) {
					teamLogoUrls[0] = teamArrayList.get(4).getLogo();
					teamLogoUrls[1] = teamArrayList.get(5).getLogo();
					teamLogoUrls[2] = teamArrayList.get(6).getLogo();
					teamLogoUrls[3] = teamArrayList.get(7).getLogo();
				} else {
					Log.e("TeamActivity", "Teams Logo are not availble");
				}
			}
			gridView.setAdapter(new TeamGridAdapter(NotificationActivity.this,
					teamLogoUrls));
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {// 3
					boolean isDataChanged = doYouWishToSaveNotificationOptions();
					if (isDataChanged) {
						showOptionChangedNotSavedDialog();
					} else {
						switch (pos) {
						case 0:
							indicatorOneImage.setVisibility(View.VISIBLE);
							indicatorTwoImage.setVisibility(View.INVISIBLE);
							indicatorThreeImage.setVisibility(View.INVISIBLE);
							indicatorFourImage.setVisibility(View.INVISIBLE);
							if (position == 0) {
								teamName.setText(teamArrayList.get(pos)
										.getName()
										.toUpperCase(Locale.getDefault()));
								setPlayersList(pos + 1, teamArrayList);
								selectedTeamId = teamArrayList.get(pos).getId();
								setOptionsList();
							} else {
								teamName.setText(teamArrayList.get(pos + 4)
										.getName()
										.toUpperCase(Locale.getDefault()));
								setPlayersList(pos + 5, teamArrayList);
								selectedTeamId = teamArrayList.get(pos + 4)
										.getId();
								setOptionsList();
							}

							break;
						case 1:
							indicatorOneImage.setVisibility(View.INVISIBLE);
							indicatorTwoImage.setVisibility(View.VISIBLE);
							indicatorThreeImage.setVisibility(View.INVISIBLE);
							indicatorFourImage.setVisibility(View.INVISIBLE);
							if (position == 0) {
								teamName.setText(teamArrayList.get(pos)
										.getName()
										.toUpperCase(Locale.getDefault()));
								selectedTeamId = teamArrayList.get(pos).getId();
								setPlayersList(pos + 1, teamArrayList);
								setOptionsList();

							} else {
								teamName.setText(teamArrayList.get(pos + 4)
										.getName()
										.toUpperCase(Locale.getDefault()));
								selectedTeamId = teamArrayList.get(pos + 4)
										.getId();
								setPlayersList(pos + 5, teamArrayList);
								setOptionsList();
							}

							break;
						case 2:
							indicatorOneImage.setVisibility(View.INVISIBLE);
							indicatorTwoImage.setVisibility(View.INVISIBLE);
							indicatorThreeImage.setVisibility(View.VISIBLE);
							indicatorFourImage.setVisibility(View.INVISIBLE);
							if (position == 0) {
								teamName.setText(teamArrayList.get(pos)
										.getName()
										.toUpperCase(Locale.getDefault()));
								selectedTeamId = teamArrayList.get(pos).getId();
								setPlayersList(pos + 1, teamArrayList);

								setOptionsList();
							} else {
								teamName.setText(teamArrayList.get(pos + 4)
										.getName()
										.toUpperCase(Locale.getDefault()));
								selectedTeamId = teamArrayList.get(pos + 4)
										.getId();
								setPlayersList(pos + 5, teamArrayList);
								setOptionsList();
							}

							break;
						case 3:
							indicatorOneImage.setVisibility(View.INVISIBLE);
							indicatorTwoImage.setVisibility(View.INVISIBLE);
							indicatorThreeImage.setVisibility(View.INVISIBLE);
							indicatorFourImage.setVisibility(View.VISIBLE);
							if (position == 0) {
								teamName.setText(teamArrayList.get(pos)
										.getName()
										.toUpperCase(Locale.getDefault()));
								selectedTeamId = teamArrayList.get(pos).getId();
								setPlayersList(pos + 1, teamArrayList);
								setOptionsList();
							} else {
								teamName.setText(teamArrayList.get(pos + 4)
										.getName()
										.toUpperCase(Locale.getDefault()));
								selectedTeamId = teamArrayList.get(pos + 4)
										.getId();
								setPlayersList(pos + 5, teamArrayList);
								setOptionsList();
							}

							break;
						default:
							break;
						}
					}
				}
			});
			((ViewPager) views).addView(imageLayout,
					new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT));
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
	}

	protected void setPlayersList(int position, ArrayList<Teams> teamArrayList) {
		individualPlayersArryList = getIndividualPlayersList(position,
				allPlayersArrayList);
		selectedPlayerId = individualPlayersArryList.get(0).getPlayerId();
		notificationAdapter = new NotificationAdapter(
				NotificationActivity.this, individualPlayersArryList);
		listPlayerNames.setAdapter(notificationAdapter);
		listPlayerNames.setSelectionAfterHeaderView();
	}

	public boolean doYouWishToSaveNotificationOptions() {
		boolean isDataChanged = false;
		if (optionsAdapter != null && optionsAdapter.optionsArrayList != null
				&& optionsAdapter.listItem != null) {
			for (int i = 0; i < optionsAdapter.optionsArrayList.size(); i++) {
				if (optionsAdapter.listItem.get(i) != null
						&& (optionsAdapter.listItem.get(i).isChecked() != optionsAdapter.optionsArrayList
								.get(i).isChecked())) {
					isDataChanged = true;
					break;
				}
			}
		}
		return isDataChanged;
	}

	private void showOptionChangedNotSavedDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				NotificationActivity.this);
		builder.setTitle("Do You Wish to save the Data");
		builder.setMessage(getResources().getString(
				R.string.options_data_changed_not_saved_message));
		builder.setPositiveButton(getResources().getString(R.string.save),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int postion) {
						dialog.dismiss();
						btnSave.performClick();
					}
				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						setOptionsList();
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private ArrayList<Players> getIndividualPlayersList(int actualReferenceId,
			ArrayList<Players> notificationPlayersArrayList) {
		ArrayList<Players> dummyIndividualPlayersList = new ArrayList<Players>();
		for (int j = 0; j < notificationPlayersArrayList.size(); j++) {
			int dummyReferenceId = notificationPlayersArrayList.get(j)
					.getPlayerTeamId();
			if (dummyReferenceId == actualReferenceId) {
				dummyIndividualPlayersList.add(notificationPlayersArrayList
						.get(j));
			}
		}
		return dummyIndividualPlayersList;
	}

	private void faceBookAuthorize() {
		mFacebook.authorize(NotificationActivity.this, new DialogListener() {
			@Override
			public void onFacebookError(FacebookError e) {
			}

			@Override
			public void onError(DialogError e) {

			}

			@Override
			public void onComplete(Bundle values) {

				mSharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(NotificationActivity.this);
				Editor mEditor = mSharedPreferences.edit();
				mEditor.putString("AccessToken", mFacebook.getAccessToken());
				mEditor.commit();
				btnFacebook.setVisibility(View.GONE);
				txtTextSave.setVisibility(View.GONE);
				mRequestType = RequestType.FB_REQUESTTYPE;
				if (Util.getInstance().isOnline(NotificationActivity.this)) {
					NotificationAsyncTask mNotiFiAsyncTask = new NotificationAsyncTask(
							NotificationActivity.this, "GET", null, null);
					mNotiFiAsyncTask.execute(getResources().getString(
							R.string.facebook_graph_url)
							+ mFacebook.getAccessToken());
				} else {
					showToastMessage(getResources().getString(
							R.string.network_error_message));
				}
			}

			@Override
			public void onCancel() {

			}
		});

	}

	private void allowNotificationDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				NotificationActivity.this);
		builder.setTitle("Allow PushNotification");
		builder.setMessage(getResources().getString(
				R.string.notification_messsage));
		builder.setPositiveButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int postion) {
						intializeGCMServer();
					}
				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void setData(String result) {
		System.out.println("sever rsponse" + result);
		if (result != null) {

			switch (mRequestType) {
			case NOREQUEST_TYPE:
				break;
			case FB_REQUESTTYPE:
				removeOverlay();
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					if (jsonObject.getString("id") != null) {
						mSharedPreferences = PreferenceManager
								.getDefaultSharedPreferences(NotificationActivity.this);
						Editor mEditor = mSharedPreferences.edit();
						mEditor.putString("Fb_UserId",
								jsonObject.getString("id"));
						mEditor.commit();

						if (Util.getInstance().isOnline(
								NotificationActivity.this)) {
							showOverlay();
							mRequestType = RequestType.USER_SOCIALID_REQUESTTYPE;
							NotificationAsyncTask mNotiFiAsyncTask = new NotificationAsyncTask(
									this, "GET", null, null);
							mNotiFiAsyncTask.execute(getResources().getString(
									R.string.social_user_url)
									+ jsonObject.getString("id"));
						} else {
							showToastMessage(getResources().getString(
									R.string.network_error_message));
						}

					}

				} catch (JSONException e) {

					Log.i(TAG, e.toString());
				}
				break;

			case USER_SOCIALID_REQUESTTYPE:
				removeOverlay();
				System.out.println("nagesh social user id" + result);
				try {
					JSONObject socialUserJsonObject = new JSONObject(result);
					if (!socialUserJsonObject.isNull("result")) {
						if (socialUserJsonObject.has("result")) {
							boolean socialStatus = socialUserJsonObject
									.getBoolean("result");
							if (socialStatus) {
								mSharedPreferences = PreferenceManager
										.getDefaultSharedPreferences(NotificationActivity.this);
								Editor editor = mSharedPreferences.edit();
								editor.putBoolean("socail_status", socialStatus);
								editor.commit();

								if (getFacebookUserId() != null) {
									if (Util.getInstance().isOnline(
											NotificationActivity.this)) {
										mRequestType = RequestType.PUSHSTATUS_REQUESTTYPE;
										showOverlay();
										NotificationAsyncTask mNotiFiAsyncTask = new NotificationAsyncTask(
												this, "GET", null, null);
										mNotiFiAsyncTask
												.execute(getResources()
														.getString(
																R.string.push_status_url)
														+ getFacebookUserId());
									} else {
										showToastMessage(getResources()
												.getString(
														R.string.network_error_message));
									}

								}
							} else {
								showToastMessage("There is Problem while Registring.");
							}
						}
					}
				} catch (JSONException e) {

					Log.i(TAG, e.toString());
				}

				break;

			case PUSHSTATUS_REQUESTTYPE:
				removeOverlay();
				if (!TextUtils.isEmpty(result)) {
					try {

						JSONObject pushStatusJsonObject = new JSONObject(result);
						if (pushStatusJsonObject.has("result")) {
							boolean pushStatus = pushStatusJsonObject
									.getBoolean("result");

							if (!pushStatus) {
								allowNotificationDialog();
							} else {
								if (Util.getInstance().isOnline(
										NotificationActivity.this)) {
									mRequestType = RequestType.GET_NOTIFICATION_REQUESTTYPE;
									showOverlay();
									NotificationAsyncTask mNotifiAsyncTask = new NotificationAsyncTask(
											this, "GET", null, null);
									mNotifiAsyncTask
											.execute(getResources()
													.getString(
															R.string.get_notification_url)
													+ getFacebookUserId());

								} else {
									showToastMessage(getResources().getString(
											R.string.network_error_message));
								}

							}
						}
					} catch (JSONException e) {
						Log.i(TAG, e.toString());
					}
				}
				break;

			case PUSH_NOTIFICATIONS_REQUESTTYPE:
				removeOverlay();
				try {
					JSONObject pushNotificationJsonObj = new JSONObject(result);

					if (pushNotificationJsonObj.has("result")) {
						String response = pushNotificationJsonObj
								.getString("result");

						if (response.equalsIgnoreCase("success")) {
							if (getFacebookUserId() != null) {
								String getNotificationUrl = getResources()
										.getString(
												R.string.get_notification_url)
										+ getFacebookUserId();
								if (Util.getInstance().isOnline(
										NotificationActivity.this)) {
									mRequestType = RequestType.GET_NOTIFICATION_REQUESTTYPE;
									showOverlay();
									NotificationAsyncTask mNotiFiAsyncTask = new NotificationAsyncTask(
											this, "GET", null, null);
									mNotiFiAsyncTask
											.execute(getNotificationUrl);
								} else {
									showToastMessage(getResources().getString(
											R.string.network_error_message));
								}
							}
						} else {
							NotificationActivity.sHandler.sendEmptyMessage(0);
						}
					}
				} catch (JSONException e) {
					Log.i(TAG, e.toString());
				}

				break;

			case GET_NOTIFICATION_REQUESTTYPE:				
				parseGetNotificationData(result);
				removeOverlay();
				if (Util.getInstance().isOnline(NotificationActivity.this)) {
					String pushNotificationMetadataUrl = getResources()
							.getString(R.string.notification_metadata_url);
					mRequestType = RequestType.NOTIFICATIONS_METADATA_REQUESTTYPE;
					showOverlay();
					NotificationAsyncTask mNotiFiAsyncTask = new NotificationAsyncTask(
							this, "GET", null, null);
					mNotiFiAsyncTask.execute(pushNotificationMetadataUrl);
				} else {
					showToastMessage(getResources().getString(
							R.string.network_error_message));
				}

				break;

			case NOTIFICATIONS_METADATA_REQUESTTYPE:
				notificationBodyLayout.setVisibility(View.VISIBLE);
				parseNotificationsMetadataJson(result);
				setPlayerUI();
				setTeamsLogos();
				setOptionsList();
				removeOverlay();
				break;
			case SETNOTIFICATION_REQUESTTYPE:
				removeOverlay();
				if (Util.getInstance().isOnline(this)) {
					if (getFacebookUserId() != null) {
						String getNotificationUrl = getResources().getString(
								R.string.get_notification_url)
								+ getFacebookUserId();
						mRequestType = RequestType.GET_NOTIFICATION_REREQUEST;
						showOverlay();
						NotificationAsyncTask mNotiFicationAsyncTask = new NotificationAsyncTask(
								this, "GET", null, null);
						mNotiFicationAsyncTask.execute(getNotificationUrl);
					}
				} else {
					showToastMessage(getResources().getString(
							R.string.network_error_message));
				}
				break;
			case GET_NOTIFICATION_REREQUEST:
				parseGetNotificationData(result);
				setOptionsList();
				removeOverlay();
			default:
				break;
			}

		}

	}

	private void intializeGCMServer() {
		GCMRegistrar.checkDevice(NotificationActivity.this);
		GCMRegistrar.checkManifest(NotificationActivity.this);
		final String regId = GCMRegistrar
				.getRegistrationId(NotificationActivity.this);
		if (regId.equals("")) {
			GCMRegistrar.register(NotificationActivity.this, "763383436164");
		} else {
			NotificationActivity.sHandler.sendEmptyMessage(0);

		}
	}

	private void showToastMessage(String message) {
		Toast.makeText(NotificationActivity.this, message, Toast.LENGTH_LONG)
				.show();
	}

	private void setOptionsList() {
		ArrayList<Integer> actionIds = new ArrayList<Integer>();
		if (playerNotificationData != null) {
			for (int i = 0; i < playerNotificationData.size(); i++) {
				if (playerNotificationData.get(i).getPlayerId() == selectedPlayerId
						&& playerNotificationData.get(i).getTeamId() == selectedTeamId) {
					actionIds
							.add(playerNotificationData.get(i).getAction() - 3);
				}
			}
		}
		for (int i = 0; i < optionsArrayList.size(); i++) {
			if (actionIds.contains(i)) {
				optionsArrayList.get(i).setChecked(true);
			} else {
				optionsArrayList.get(i).setChecked(false);
			}
		}
		if (optionsArrayList != null) {
			optionsAdapter = new NotificationOptionAdapter(
					NotificationActivity.this, optionsArrayList);
			listOptions.setAdapter(optionsAdapter);
			listOptions.setSelectionAfterHeaderView();
			btnSave.setVisibility(View.VISIBLE);
			int finalHeight = optionsArrayList.size() + 2;
			int height = getItemHightofListView(listOptions, finalHeight);

			// int listSize = height * (optionsArrayList.size());
			listOptions.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, height));

		}
	}

	public static int getItemHightofListView(ListView listView, int items) {
		ListAdapter mAdapter = listView.getAdapter();
		int listviewElementsheight = 0;
		// for listview total item hight
		// items = mAdapter.getCount();

		for (int i = 0; i < items; i++) {
			View childView = mAdapter.getView(i, null, listView);
			childView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			listviewElementsheight += childView.getMeasuredHeight();
		}
		return listviewElementsheight;
	}

	private String getFacebookUserId() {
		String fbUserId;
		if (mSharedPreferences == null) {
			mSharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(this);
		}
		fbUserId = mSharedPreferences.getString("Fb_UserId", null);
		return fbUserId;
	}

	private void setTeamsLogos() {
		pager.setAdapter(new TeamPagerAdapter(teamArrayList));
		selectedTeamId = teamArrayList.get(0).getId();
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrolled(int pageno, float arg1, int arg2) {
				indicatorOneImage.setVisibility(View.VISIBLE);
				indicatorTwoImage.setVisibility(View.INVISIBLE);
				indicatorThreeImage.setVisibility(View.INVISIBLE);
				indicatorFourImage.setVisibility(View.INVISIBLE);

				if (pageno == 0) {
					teamName.setText(teamArrayList.get(pageno).getName()
							.toUpperCase(Locale.getDefault()));
					selectedTeamId = teamArrayList.get(0).getId();
					setPlayersList(pageno + 1, teamArrayList);
					setOptionsList();
				} else {
					teamName.setText(teamArrayList.get(pageno * 4).getName()
							.toUpperCase(Locale.getDefault()));
					selectedTeamId = teamArrayList.get(4).getId();
					setPlayersList((pageno * 4) + 1, teamArrayList);
					setOptionsList();
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				int currentPage = pager.getCurrentItem();
				if (currentPage == 1 || currentPage == 0) {
					previousState = currentState;
					currentState = state;
					if (previousState == 1 && currentState == 0) {
						pager.setCurrentItem(currentPage == 0 ? 2 : 0);
					}
				}
			}

			@Override
			public void onPageSelected(int position) {
				if (IndicatorLayout != null) {
					Util.setTeamPageIndicator(position, IndicatorLayout);
				}
			}
		});
	}

	private void setPlayerUI() {
		if (teamArrayList.size() > 0) {
			teamName.setText(teamArrayList.get(0).getName()
					.toUpperCase(Locale.getDefault()));
		}
		int referenceId = allPlayersArrayList.get(0).getPlayerTeamId();
		individualPlayersArryList = getIndividualPlayersList(referenceId,
				allPlayersArrayList);
		notificationAdapter = new NotificationAdapter(
				NotificationActivity.this, individualPlayersArryList);
		listPlayerNames.setAdapter(notificationAdapter);
		selectedPlayerId = individualPlayersArryList.get(0).getPlayerId();
		listPlayerNames.setCacheColorHint(Color.TRANSPARENT);
		listPlayerNames.setFadingEdgeLength(1);
		listPlayerNames.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				boolean isDataChanged = doYouWishToSaveNotificationOptions();
				if (isDataChanged) {
					showOptionChangedNotSavedDialog();
				} else {
					selectedPlayerId = individualPlayersArryList.get(position)
							.getPlayerId();
					notificationAdapter.updateList(position);
					setOptionsList();
				}
			}
		});
	}

	private void parseNotificationsMetadataJson(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			int teamId = -1;
			if (jsonObject.has("teams")) {
				JSONArray teamsJsonArray = jsonObject.getJSONArray("teams");
				for (int i = 0; i < teamsJsonArray.length(); i++) {
					Teams teams = new Teams();
					JSONObject teamsJsonObject = teamsJsonArray
							.getJSONObject(i);
					if (teamsJsonObject.has("team_id")) {
						teamId = teamsJsonObject.getInt("team_id");
						teams.setId(teamId);
					}
					if (teamsJsonObject.has("team_name")) {
						String teamName = teamsJsonObject
								.getString("team_name");
						teams.setName(teamName);
					}
					if (teamsJsonObject.has("team_logo")) {
						String teamLogo = teamsJsonObject
								.getString("team_logo");
						teams.setLogo(teamLogo);
					}
					// add team items to team arraylist
					teamArrayList.add(teams);
					JSONArray playersJsonArray = teamsJsonObject
							.getJSONArray("players");
					for (int j = 0; j < playersJsonArray.length(); j++) {
						Players playerItem = new Players();

						JSONObject playersJsonObject = playersJsonArray
								.getJSONObject(j);
						if (playersJsonObject.has("id")) {
							int playerId = playersJsonObject.getInt("id");
							playerItem.setPlayerId(playerId);
						}
						if (playersJsonObject.has("name")) {
							String playerName = playersJsonObject
									.getString("name");
							playerItem.setPlayerName(playerName);
						}

						if (playersJsonObject.has("thumb")) {
							String playerThumbUrl = playersJsonObject
									.getString("thumb");
							playerItem.setPlayerThumbUrl(playerThumbUrl);
						}
						playerItem.setPlayerTeamId(teamId);
						allPlayersArrayList.add(playerItem);
					}
				}
				if (jsonObject.has("options")) {
					int optionsId = -1;
					String optionValue = "";
					JSONArray optionsJsonArray = jsonObject
							.getJSONArray("options");
					for (int k = 0; k < optionsJsonArray.length(); k++) {
						JSONObject optionObject = optionsJsonArray
								.getJSONObject(k);
						if (optionObject.has("id")) {
							optionsId = optionObject.getInt("id");
						}
						if (optionObject.has("value")) {
							optionValue = optionObject.getString("value");
						}
						JSONArray actionJsonArray = optionObject
								.getJSONArray("actions");
						for (int l = 0; l < actionJsonArray.length(); l++) {
							Options optionItem = new Options();
							JSONObject actionJsonObject = actionJsonArray
									.getJSONObject(l);
							if (actionJsonObject.has("id")) {
								int actionId = actionJsonObject.getInt("id");
								optionItem.setActionId(actionId);
							}
							if (actionJsonObject.has("value")) {
								String actionValue = actionJsonObject
										.getString("value");
								optionItem.setActionValue(actionValue);
							}
							optionItem.setOptionId(optionsId);
							optionItem.setOptionValue(optionValue);
							optionItem.setChecked(false);
							optionsArrayList.add(optionItem);
						}
					}

				}
			}
		} catch (JSONException e) {
			Log.i(TAG, e.toString());
		}
	}

	private void parseGetNotificationData(String result) {
		playerNotificationData = new ArrayList<PlayerNotificationData>();
		try {
			JSONObject object = new JSONObject(result);
			if (object.has("notificationdata")) {
				String data = object.getString("notificationdata");
				JSONObject notificationData = new JSONObject(data);
				if (notificationData.has("team")) {
					JSONArray teamsArray = notificationData
							.getJSONArray("team");
					for (int i = 0; i < teamsArray.length(); i++) {
						JSONObject teamObject = teamsArray.getJSONObject(i);
						int teamId = -1;
						if (teamObject.has("teamid")) {
							teamId = teamObject.getInt("teamid");
						}
						if (teamObject.has("players")) {
							JSONArray playerArray = teamObject
									.getJSONArray("players");
							for (int j = 0; j < playerArray.length(); j++) {
								JSONObject playerObject = playerArray
										.getJSONObject(j);
								int playerId = -1;
								if (playerObject.has("playerid")) {
									playerId = playerObject.getInt("playerid");
								}
								if (playerObject.has("notifications")) {
									JSONArray notificationArray = playerObject
											.getJSONArray("notifications");
									for (int k = 0; k < notificationArray
											.length(); k++) {
										PlayerNotificationData playerNotifData = new PlayerNotificationData();
										JSONObject notificationObject = notificationArray
												.getJSONObject(k);
										if (notificationObject.has("optionid")) {
											playerNotifData
													.setOptionId(notificationObject
															.getInt("optionid"));
											playerNotifData
													.setOptionId(notificationObject
															.getInt("optionid"));
										}
										if (notificationObject.has("actionid")) {
											playerNotifData
													.setAction(notificationObject
															.getInt("actionid"));
											playerNotifData
													.setOptionId(notificationObject
															.getInt("actionid"));
										}
										playerNotifData.setPlayerId(playerId);
										playerNotifData.setTeamId(teamId);
										playerNotificationData
												.add(playerNotifData);
									}
								}
							}
						}
					}
				}
			}
		} catch (JSONException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();		
	}

	private void showOverlay() {
		if (overlay == null) {
			overlay = new ProgressDialog(this);
		}
		overlay.show();
		overlay.setContentView(R.layout.overlay_layout);
	}

	private void removeOverlay() {
		if (overlay != null && overlay.isShowing()) {
			overlay.dismiss();
		}
	}

	private ProgressDialog overlay;
	private ArrayList<PlayerNotificationData> playerNotificationData;
}