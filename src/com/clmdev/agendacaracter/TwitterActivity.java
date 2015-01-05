package com.clmdev.agendacaracter;

import org.json.JSONObject;

import com.clmdev.agendacaracter.R;
import com.clmdev.reutilizables.Util;
import com.clmdev.twitter.*;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class TwitterActivity extends Activity {

	public static final int TWITTER_LOGIN_RESULT_CODE_SUCCESS = 1;
	public static final int TWITTER_LOGIN_RESULT_CODE_FAILURE = 2;

	public static final String TWITTER_CONSUMER_KEY = "SVlJLmkwzV9L03X5bNLyze5pj";//"dsjO5jsysimbpexFnLvNKb6BQ";
	public static final String TWITTER_CONSUMER_SECRET = "BYipvc5DAwSfYXSZBm1bp7lVwuEECEYiGrOGd0i53CVuuLJcJp"; //"3GFO872P5ToABHqGk0hjVnRIHLLXR9B0dXi4JZBt0ClhClaBaB";

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
				super.onPageFinished(view, url);
				if (mProgressDialog != null)
					mProgressDialog.dismiss();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
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
		super.onResume();
	}

	private void saveAccessTokenAndFinish(final Uri uri) {
		new Thread(new Runnable() {
			@Override
			public void run() {
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

					TwitterActivity.this
							.setResult(TWITTER_LOGIN_RESULT_CODE_SUCCESS);

					String url = getResources().getString(
							R.string.url_web_service)
							+ "users/create_user_social/format/json";
					String username = "tw" + user.getId();					
					String email = "sn@mail.com";
					String firstname = (user.getName()!=null)?user.getName():"sn"; 
					String lastname = "sn";
					new RegistroUsuarioJSONFeedTask().execute(url, username,
							 email, firstname, lastname);

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

	private class RegistroUsuarioJSONFeedTask extends
			AsyncTask<String, Void, String> {

		protected String doInBackground(String... prms) {
			return Util.readJSONFeedPost(prms[0], prms[1], prms[2], prms[3],
					prms[4]);
		}

		protected void onPostExecute(String result) {
			try {
				JSONObject datos = new JSONObject(result);
				if (result.equals("error")) {
					Toast.makeText(getApplicationContext(),
							"Error al conectar con el servidor",
							Toast.LENGTH_SHORT).show();
				} else if (datos.getString("res").equalsIgnoreCase("ok")) {
					SharedPreferences prefe = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = prefe.edit();
					editor.putString("id", datos.getString("data"));
					editor.commit();

//					Log.e("Id  de usuario", datos + "");

					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(i);
					finish();
				} else if (datos.getString("res").equalsIgnoreCase("error")) {
					Toast.makeText(getApplicationContext(), "Error interno",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e("Error onPostExecute", "No se descargaron los datos");
			}
		}
	}
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

	@SuppressLint("SetJavaScriptEnabled") private void askOAuth() {
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
						twitterLoginWebView.loadUrl(requestToken
								.getAuthenticationURL());
						twitterLoginWebView.getSettings().setJavaScriptEnabled(
								true);
					}
				});
			}
		}).start();
	}

}
