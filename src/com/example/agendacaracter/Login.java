package com.example.agendacaracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reutilizables.Util;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;


public class Login extends Activity implements OnClickListener {

	public EditText etxUsuarioNombre;
	public EditText etxContrasenia;
	public TextView txvBienvenido;

	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";

	private LoginButton loginButton;
	private PendingAction pendingAction = PendingAction.NONE;
	private GraphUser user;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface HelveticaBoldTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		txvBienvenido = (TextView) findViewById(R.id.txv_bienvenido);
		txvBienvenido.setTypeface(HelveticaBoldTypeFace);

		etxUsuarioNombre = (EditText) findViewById(R.id.txv_username_login);
		etxUsuarioNombre.setTypeface(miPropiaTypeFace);

		etxContrasenia = (EditText) findViewById(R.id.txv_pass_login);
		etxContrasenia.setTypeface(miPropiaTypeFace);

		TextView txvMensajeDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvMensajeDescripcion.setTypeface(miPropiaTypeFace);

		Button btnIniciarSesion = (Button) findViewById(R.id.btn_iniciar_sesion);
		btnIniciarSesion.setTypeface(HelveticaBoldTypeFace);

		Button btnCrearCuenta = (Button) findViewById(R.id.btn_crear_cuenta);
		btnCrearCuenta.setTypeface(HelveticaBoldTypeFace);

		btnIniciarSesion.setOnClickListener(this);
		btnCrearCuenta.setOnClickListener(this);

		ImageView img_twitter = (ImageView) findViewById(R.id.imv_twitter_descripcion);
		img_twitter.setOnClickListener(this);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setBackgroundResource(R.drawable.face);
		loginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						Login.this.user = user;
						updateUI();
						handlePendingAction();
					}
				});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.btn_iniciar_sesion:

			String us = etxUsuarioNombre.getText().toString();
			String cl = etxContrasenia.getText().toString();

			final String url = getResources().getString(
					R.string.url_web_service);

			if (us.equals("") || cl.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Por favor completa todos los campos",
						Toast.LENGTH_LONG).show();
			} else if (!us.matches("([a-z]|[A-Z]|\\s|[0-9])+")
					|| !cl.matches("([a-z]|[A-Z]|\\s|[0-9])+")) {
				Toast.makeText(getApplicationContext(),
						"No se permiten caracteres especiales",
						Toast.LENGTH_LONG).show();
				if (!us.matches("([a-z]|[A-Z]|\\s|[0-9])+")) {
					etxUsuarioNombre.setText("");
				}
				if (!cl.matches("([a-z]|[A-Z]|\\s|[0-9])+")) {
					etxContrasenia.setText("");
				}
				if (!us.matches("([a-z]|[A-Z]|\\s|[0-9])+")
						&& !cl.matches("([a-z]|[A-Z]|\\s|[0-9])+")) {
					etxUsuarioNombre.setText("");
					etxContrasenia.setText("");
				}

			} else {
				new ReadUsuarioJSONFeedTask().execute(url
						+ "users/login/format/json");

			}

			break;
		case R.id.btn_crear_cuenta:
			i = new Intent(this, CrearCuenta.class);
			startActivity(i);
			break;
		case R.id.imv_twitter_descripcion:
			i = new Intent(this, TwitterActivity.class);
			startActivity(i);
			break;
		default:

			break;

		}

	}

	private class ReadUsuarioJSONFeedTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(Login.this);

		}

		@Override
		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);

		}

		@Override
		protected void onPostExecute(String result) {

			try {

				JSONObject datos = new JSONObject(result);

				SharedPreferences prefe = getSharedPreferences("user",
						Context.MODE_PRIVATE);
				Editor editor = prefe.edit();
				editor.putString("id", datos.getString("id"));
				editor.putString("username", datos.getString("usuario"));
				editor.putString("useremail", datos.getString("email"));
				editor.commit();
				Session.getActiveSession().closeAndClearTokenInformation();
				Intent in = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(in);
				finish();

			} catch (Exception e) {				
				Toast.makeText(
						getApplicationContext(),
						"Datos Incorrectos: La contraseña o usuario no son validos...",
						Toast.LENGTH_LONG).show();
			}
			Util.cerrarDialogLoad();
		}

	}

	public String readJSONFeed(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);

		try {
			String username = etxUsuarioNombre.getText().toString();
			String password = etxContrasenia.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>(3);
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("tipo", "1"));

			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse response = httpClient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.e("Error:","Status code");

			}
		} catch (Exception e) {
			Log.e("Error:","No se ncontraron los datos"+ e.getMessage());

		}

		return stringBuilder.toString();
	}

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);
		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(Login.this).setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();

	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());
		if (enableButtons && user != null) {
			String url = getResources().getString(R.string.url_web_service)
					+ "users/create_user_social/format/json";
			String username = "fb" + user.getId();
			String password = user.getId() + "fb";
			String email = user.getId();
			String firstname = user.getFirstName();
			String lastname = user.getLastName();
			String ip = "no ip";
			new RegistroUsuarioJSONFeedTask().execute(url, username, password,
					email, firstname, lastname, ip);

		}
	}

	private class RegistroUsuarioJSONFeedTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			Util.MostrarDialog(Login.this);
			super.onPreExecute();
		}

		protected String doInBackground(String... prms) {
			return Util.readJSONFeedPost(prms[0], prms[1], prms[2], prms[3],
					prms[4], prms[5], prms[6]);
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

					Session.getActiveSession().closeAndClearTokenInformation();
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(i);
					finish();
				} else if (datos.getString("res").equalsIgnoreCase("error")) {
					Toast.makeText(getApplicationContext(), "Error interno al iniciar sesión con facebook",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Error interno: "+e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			break;
		case POST_STATUS_UPDATE:
			break;
		}
	}

}
