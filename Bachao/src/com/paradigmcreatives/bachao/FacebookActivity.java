package com.paradigmcreatives.bachao;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.paradigmcreatives.bachao.logging.Logger;
import com.paradigmcreatives.bachao.util.Constants;
import com.paradigmcreatives.bachao.util.Util;

public class FacebookActivity extends Activity {

	private final String PENDING_ACTION_BUNDLE_KEY = "com.paradigmcreatives.bachao:PendingAction";

	private static final List <String> PERMISSIONS = Arrays.asList("publish_actions");

	private LoginButton loginButton;

	private ProfilePictureView profilePictureView;

	private PendingAction pendingAction = PendingAction.NONE;

	private GraphUser user;

	private EditText editMessage;

	private RelativeLayout logout;

	private TextView name;

	private CheckBox facebookCheck;

	private SharedPreferences preferences;

	private SharedPreferences.Editor editFacebook;

	private Button saveFacebook;

	private enum PendingAction {
		NONE, POST_STATUS_UPDATE
	}

	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call (Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper    = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		preferences  = getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		editFacebook = preferences.edit();

		if (savedInstanceState != null) {
			String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.activity_facebook);

		facebookCheck      = (CheckBox) findViewById(R.id.facebook_check);
		saveFacebook       = (Button) findViewById(R.id.facebook_save);
		logout             = (RelativeLayout) findViewById(R.id.logout_button);
		name               = (TextView) findViewById(R.id.logout_text);
		editMessage        = (EditText) findViewById(R.id.post_message);
		loginButton        = (LoginButton) findViewById(R.id.login_button);

		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched (GraphUser user) {
				FacebookActivity.this.user = user;

				if (user != null) {
					editFacebook.putBoolean("FACEBOOK_USER", true);
				}
				else {
					editFacebook.putBoolean("FACEBOOK_USER", false);
				}

				editFacebook.commit();
				// It's possible that we were waiting for this.user to
				// be populated in order to post a
				// status update.
				updateUI();
				handlePendingAction(FacebookActivity.this);
			}
		});

		facebookCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				if (facebookCheck.isChecked()) {
					facebookCheck.setButtonDrawable(R.drawable.check);
				}
				else {
					facebookCheck.setButtonDrawable(R.drawable.add);
				}
			}
		});

		if (preferences.getAll() != null) {

			String message = preferences.getString("FACEBOOK_MESSAGE", null);
			if (message != null && message.length() != 0) {
				editMessage.setText(preferences.getString("FACEBOOK_MESSAGE", null));
			}
			Boolean bool = preferences.getBoolean("FACEBOOK_CHECK", false);
			if (bool) {
				facebookCheck.setChecked(true);
				facebookCheck.setButtonDrawable(R.drawable.check);
			}
			else {
				facebookCheck.setChecked(false);
				facebookCheck.setButtonDrawable(R.drawable.add);
			}
		}

		// set cursor position to end of the field
		int position = editMessage.getText().length();
		Editable etext = editMessage.getText();
		Selection.setSelection(etext, position);

		saveFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				SharedPreferences.Editor edit = preferences.edit();

				Session session = Session.getActiveSession();

				String facebookMessage = editMessage.getText().toString().trim();

				// if facebook is checked and session is opened check for permissions
				if (facebookCheck.isChecked() && session.isOpened()) {

					if (!session.getPermissions().contains("publish_actions")) {
						Toast.makeText(FacebookActivity.this, getResources().getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
					}
					else {
						edit.putBoolean("FACEBOOK_CHECK", true);
						facebookCheck.setButtonDrawable(R.drawable.check);
						edit.putString("FACEBOOK_MESSAGE", facebookMessage);
						edit.commit();
						finish();
					}
				}
				else if (facebookCheck.isChecked() && !session.isOpened()) {
					// if facebook is checked and session is not opened
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.facebook_error_login), Toast.LENGTH_SHORT).show();
					edit.putString("FACEBOOK_MESSAGE", facebookMessage);
					edit.commit();
				}
				else if (!facebookCheck.isChecked()) {
					// when facebook is not checked
					edit.putBoolean("FACEBOOK_CHECK", false);
					edit.putString("FACEBOOK_MESSAGE", facebookMessage);
					edit.commit();
					finish();
				}
			}
		});

		// to show alert dialog when clicked on the logout button
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {

				AlertDialog.Builder build = new AlertDialog.Builder(FacebookActivity.this);
				build.setTitle(getResources().getString(R.string.logout_text));
				build.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

					@Override
					public void onClick (DialogInterface dialog, int which) {
						Session session = Session.getActiveSession();
						if (session.isOpened()) {
							editFacebook.putBoolean("FACEBOOK_CHECK", false);
							editFacebook.commit();
							session.closeAndClearTokenInformation();
							facebookCheck.setButtonDrawable(R.drawable.add);
							facebookCheck.setChecked(false);
							Logger.info("Successfully Loged Out");
							updateUI();
						}
					}
				});

				build.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

					@Override
					public void onClick (DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				AlertDialog alert = build.create();
				alert.show();
			}
		});

		profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);

	}

	@Override
	protected void onResume () {
		super.onResume();
		uiHelper.onResume();
		updateUI();
	}

	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		// if session is opened and post permission is not granted request for new permission
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause () {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	// on change of session state this method will be called
	private void onSessionStateChange (Session session, SessionState state, Exception exception) {

		if (session.isOpened() && !session.getPermissions().contains("publish_actions")) {
			session.requestNewPublishPermissions(new Session.NewPermissionsRequest(FacebookActivity.this, PERMISSIONS));
		}
		if (pendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(FacebookActivity.this).setTitle("Cancel").setMessage("Permission not granted").setPositiveButton("OK", null).show();
			pendingAction = PendingAction.NONE;
		}
		else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction(FacebookActivity.this);
		}

		updateUI();
	}

	// to update the UI when the user is logged in
	private void updateUI () {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());

		if (enableButtons && user != null) {
			loginButton.setVisibility(View.INVISIBLE);
			logout.setVisibility(View.VISIBLE);
			profilePictureView.setProfileId(user.getId());
			name.setText(getResources().getString(R.string.logged_text)+" " + user.getFirstName());
		}
		else {
			loginButton.setVisibility(View.VISIBLE);
			logout.setVisibility(View.INVISIBLE);
			profilePictureView.setProfileId(null);

		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction (Context context) {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
			case POST_STATUS_UPDATE:
				postStatusUpdate(context);
				break;
		}
	}

	public void onClickPostStatusUpdate (Context context) {
		performPublish(PendingAction.POST_STATUS_UPDATE, context);
	}

	// Method to show toast about the status of the post
	private void showPublishResult (String message, GraphObject result, FacebookRequestError error, Context context) {
		String alertMessage = null;
		if (error == null) {
			alertMessage = context.getResources().getString(R.string.facebook_succes_post);
			Logger.info(context.getResources().getString(R.string.facebook_succes_post));
		}
		else {
			int errorCode = error.getErrorCode();
			if (errorCode == 506) {
				alertMessage = context.getResources().getString(R.string.facebook_duplicate_post);
			}
			else {
				alertMessage = context.getResources().getString(R.string.facebook_error_post);
			}
			Logger.info(error.getErrorMessage());
		}
		Toast.makeText(context, alertMessage, Toast.LENGTH_LONG).show();
	}

	// method to post on the user wall
	public void postStatusUpdate (final Context context) {
		SharedPreferences mpreferences = context.getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Boolean userBool = mpreferences.getBoolean("FACEBOOK_USER", false);

		if (userBool && hasPublishPermission()) {

			String message = mpreferences.getString("FACEBOOK_MESSAGE", null);
			if (message == null) {
				message = context.getResources().getString(R.string.message_one);
			}
			else if (message != null && message.length() == 0) {
				message = context.getResources().getString(R.string.message_one);
			}

			String mapUrl = Util.getInstance().getLatLong(context);
			String address_location = Util.getInstance().getDetailedAddress(context);

			if (address_location.equals(null)) {
				address_location = " ";
			}

			Request request = Request.newStatusUpdateRequest(Session.getActiveSession(), message + ". " + "\n" + context.getResources().getString(R.string.message_two) + " " + address_location + " " + mapUrl, new Request.Callback() {

				@Override
				public void onCompleted (Response response) {
					showPublishResult("", response.getGraphObject(), response.getError(), context);
				}
			});
			request.executeAsync();
		}
	}

	// To check whether publish permission is there or not
	private boolean hasPublishPermission () {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().contains("publish_actions");
	}

	private void performPublish (PendingAction action, Context context) {
		View view = View.inflate(context, R.layout.activity_facebook, null);
		loginButton = (LoginButton) view.findViewById(R.id.login_button);

		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched (GraphUser user) {
				FacebookActivity.this.user = user;
			}
		});
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				// We can do the action right away.
				handlePendingAction(context);
			}
		}
	}

	@Override
	public void onBackPressed () {

		SharedPreferences.Editor edit = preferences.edit();
		if (facebookCheck.isChecked()) {
			edit.putBoolean("FACEBOOK_CHECK", true);
			facebookCheck.setButtonDrawable(R.drawable.check);
		}
		else {
			edit.putBoolean("FACEBOOK_CHECK", false);
			facebookCheck.setButtonDrawable(R.drawable.add);
		}
		edit.commit();

		Session session = Session.getActiveSession();
		String facebookMessage = editMessage.getText().toString().trim();
		String prefsMessage = preferences.getString("FACEBOOK_MESSAGE", null);

		if (facebookCheck.isChecked()) {
			if (session != null) {
				if (!session.isOpened()) {
					Toast.makeText(FacebookActivity.this, getResources().getString(R.string.facebook_error_message), Toast.LENGTH_SHORT).show();
				}
				else {
					if (!session.getPermissions().contains("publish_actions")) {
						Toast.makeText(FacebookActivity.this, getResources().getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
					}
					else {
						if (prefsMessage == null && facebookMessage.length() != 0) {
							showAlertDialog();
						}
						else if (prefsMessage != null && !facebookMessage.equalsIgnoreCase(prefsMessage)) {
							showAlertDialog();
						}
						else {
							super.onBackPressed();
						}
					}
				}
			}
			else {
				Toast.makeText(FacebookActivity.this, getResources().getString(R.string.facebook_error_message), Toast.LENGTH_SHORT).show();
			}
		}
		else {
			if (prefsMessage == null && facebookMessage.length() != 0) {
				showAlertDialog();
			}
			else if (prefsMessage != null && !facebookMessage.equalsIgnoreCase(prefsMessage)) {
				showAlertDialog();
			}
			else {
				super.onBackPressed();
			}
		}

	}

	private void showAlertDialog () {
		AlertDialog.Builder build = new AlertDialog.Builder(FacebookActivity.this);
		build.setTitle(getResources().getString(R.string.abandom_text));
		build.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				String facebookMessage = editMessage.getText().toString().trim();
				String prefsMessage = preferences.getString("FACEBOOK_MESSAGE", null);

				if (prefsMessage == null && facebookMessage.length() != 0) {
					editMessage.setText("");
				}
				else if (prefsMessage != null && !facebookMessage.equalsIgnoreCase(prefsMessage)) {
					editMessage.setText(prefsMessage);
				}
				FacebookActivity.this.finish();
			}
		});

		build.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(FacebookActivity.this, getResources().getString(R.string.call_error_tap), Toast.LENGTH_SHORT).show();
			}
		});

		AlertDialog alert = build.create();
		alert.show();
	}

}
