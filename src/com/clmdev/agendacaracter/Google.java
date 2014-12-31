package com.clmdev.agendacaracter;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clmdev.reutilizables.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

public class Google extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {
	private static final String TAG = "Conectar con Google+";
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlusClient = new PlusClient.Builder(this, this, this).setScopes(
				Scopes.PROFILE).build();
		setContentView(R.layout.activity_google);
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Conectando...");

		if (!mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
			} else {
				try {
					mConnectionResult.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mConnectionResult = null;
					mPlusClient.connect();
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}
		Toast.makeText(this, "Error al intentar conectar con Google+",
				Toast.LENGTH_LONG).show();
		mConnectionResult = result;
		Intent i = new Intent(getApplicationContext(), Login.class);
		startActivity(i);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE_RESOLVE_ERR
				&& responseCode == RESULT_OK) {
			mConnectionResult = null;
			mPlusClient.connect();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		try {
			mConnectionProgressDialog.dismiss();
			Person persona = mPlusClient.getCurrentPerson();
			String url = getResources().getString(R.string.url_web_service)
					+ "users/create_user_social/format/json";
			String username = "go" + persona.getId();
			String email = (mPlusClient.getAccountName() != null) ? mPlusClient
					.getAccountName() : "sn";
			String firstname = (persona.getName().getFamilyName() != null) ? persona
					.getName().getFamilyName() : "sn";
			String lastname = (persona.getName().getGivenName() != null) ? persona
					.getName().getGivenName() : "sn";

			new RegistroUsuarioJSONFeedTask().execute(url, username, email,
					firstname, lastname);

		} catch (Exception e) {
			Toast.makeText(this, "Error al obtener datos de cuenta",
					Toast.LENGTH_LONG).show();
			Intent i = new Intent(getApplicationContext(), Login.class);
			startActivity(i);
			finish();
		}

	}

	private class RegistroUsuarioJSONFeedTask extends
			AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Util.MostrarDialog(Google.this);
		}

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
			Util.cerrarDialogLoad();
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, " Disconnected.", Toast.LENGTH_LONG).show();
		Log.d(TAG, "disconnected");
	}
}