package com.example.agendacaracter;

import com.example.twitter.*;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TwitterActivity extends Activity {

	public static final int TWITTER_LOGIN_RESULT_CODE_SUCCESS = 1;
	public static final int TWITTER_LOGIN_RESULT_CODE_FAILURE = 2;

	public static final String TWITTER_CONSUMER_KEY = "dsjO5jsysimbpexFnLvNKb6BQ";
	public static final String TWITTER_CONSUMER_SECRET = "3GFO872P5ToABHqGk0hjVnRIHLLXR9B0dXi4JZBt0ClhClaBaB";

	private WebView twitterLoginWebView;
	private ProgressDialog mProgressDialog;
	public String twitterConsumerKey;
	public String twitterConsumerSecret;

	private static Twitter twitter;
	private static RequestToken requestToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		Twitter twitter = factory.getInstance();

		twitterConsumerKey = twitter.getConfiguration().getOAuthConsumerKey();
		twitterConsumerSecret = twitter.getConfiguration()
				.getOAuthConsumerSecret();

		if (twitterConsumerKey == null || twitterConsumerSecret == null) {
			Log.e("Twitter",
					"ERROR: Consumer Key and Consumer Secret required!");
			TwitterActivity.this.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
			TwitterActivity.this.finish();
		}

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();

		twitterLoginWebView = (WebView) findViewById(R.id.twitter_login_web_view);
		twitterLoginWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains(Constants.TWITTER_CALLBACK_URL)) {
					Uri uri = Uri.parse(url);
					TwitterActivity.this.saveAccessTokenAndFinish(uri);
					return true;
				}
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

				if (mProgressDialog != null)
					mProgressDialog.show();
			}
		});

		Log.e("Twitter", "ASK OAUTH");
		askOAuth();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void saveAccessTokenAndFinish(final Uri uri) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String verifier = uri
						.getQueryParameter(Constants.IEXTRA_OAUTH_VERIFIER);
				try {
					SharedPreferences sharedPrefs = getSharedPreferences(
							Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
					AccessToken accessToken = twitter.getOAuthAccessToken(					
							requestToken, verifier);
					
					long userID = accessToken.getUserId();
					User user = twitter.showUser(userID);
										
					Editor e = sharedPrefs.edit();
					e.putString(Constants.PREF_KEY_TOKEN,
							accessToken.getToken());
					e.putString(Constants.PREF_KEY_SECRET,
							accessToken.getTokenSecret());
					e.commit();
					
					TwitterActivity.this.setResult(TWITTER_LOGIN_RESULT_CODE_SUCCESS);
					
					
					Log.e("Usuario user",user.getName()+"");
					
					Intent in = new Intent(TwitterActivity.this, MainActivity.class);
					startActivity(in);
					
					
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage() != null)
						Log.e("Twitter", e.getMessage());
					else
						Log.e("Twitter", "ERROR: Twitter callback failed");
					TwitterActivity.this
							.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
				}
				TwitterActivity.this.finish();
			}
		}).start();
	}

	// ====== TWITTER HELPER METHODS ======

	public static boolean isConnected(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Constants.PREF_KEY_TOKEN, null) != null;
	}

	public static void logOutOfTwitter(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Constants.PREF_KEY_TOKEN, null);
		e.putString(Constants.PREF_KEY_SECRET, null);
		e.commit();
	}

	public static String getAccessToken(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Constants.PREF_KEY_TOKEN, null);
	}

	public static String getAccessTokenSecret(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Constants.PREF_KEY_SECRET, null);
	}

	private void askOAuth() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(twitterConsumerKey);
		configurationBuilder.setOAuthConsumerSecret(twitterConsumerSecret);
		Configuration configuration = configurationBuilder.build();
		twitter = new TwitterFactory(configuration).getInstance();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					requestToken = twitter
							.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);
				} catch (Exception e) {
					final String errorString = e.toString();
					TwitterActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mProgressDialog.cancel();
							Toast.makeText(TwitterActivity.this,
									errorString.toString(), Toast.LENGTH_SHORT)
									.show();
							finish();
							
						}
					});
					e.printStackTrace();
					return;
				}

				TwitterActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.e("Twitter", "LOADING AUTH URL");
						twitterLoginWebView.loadUrl(requestToken.getAuthenticationURL());
						twitterLoginWebView.getSettings().setJavaScriptEnabled(true);
					}
				});
			}
		}).start();
	}
	
}
