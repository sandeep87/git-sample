package com.paradigmcreatives.twitter;

public class Constants {

	public static final String CONSUMER_KEY          =   "IKbVsisrYKDhb431J4HCag";
	public static final String CONSUMER_SECRET       =   "nkWAZW3hmWb3CCjYReD1QWmzYIohv3h8UWKS5aw0K8";

	public static final String REQUEST_URL           =   "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL            =   "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL         =   "https://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME =   "TestApp";
	public static final String OAUTH_CALLBACK_HOST   =   "callback";
	public static final String OAUTH_CALLBACK_URL    =   OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}
