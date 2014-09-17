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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class Login extends Activity implements OnClickListener {

	public EditText etxUsuarioNombre;
	public EditText etxContrasenia;
	private MainActivity claseprincipal = new MainActivity();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		etxUsuarioNombre = (EditText) findViewById(R.id.txv_username_login);
		etxUsuarioNombre.setTypeface(miPropiaTypeFace);

		etxContrasenia = (EditText) findViewById(R.id.txv_pass_login);
		etxContrasenia.setTypeface(miPropiaTypeFace);

		TextView txvMensajeDescripcion = (TextView) findViewById(R.id.txv_cabecera_descripcion);

		txvMensajeDescripcion.setTypeface(miPropiaTypeFace);

		Button btnIniciarSesion = (Button) findViewById(R.id.btn_iniciar_sesion);
		btnIniciarSesion.setTypeface(miPropiaTypeFace);

		Button btnCrearCuenta = (Button) findViewById(R.id.btn_crear_cuenta);
		btnCrearCuenta.setTypeface(miPropiaTypeFace);

		btnIniciarSesion.setOnClickListener(this);
		btnCrearCuenta.setOnClickListener(this);

		ImageView img_twitter = (ImageView) findViewById(R.id.imv_twitter_descripcion);
		img_twitter.setOnClickListener(this);
		// estaConectado();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_iniciar_sesion:

			String us = etxUsuarioNombre.getText().toString();
			String cl = etxContrasenia.getText().toString();

			if (us.equals("") || cl.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Porfavor completa todos los campos", Toast.LENGTH_LONG)
						.show();

			} else {
				new ReadUsuarioJSONFeedTask()
						.execute("http://192.168.0.55/Agenda_WS/users/login/format/json");

			}
			break;
		case R.id.btn_crear_cuenta:
			Intent i = new Intent(this, CrearCuenta.class);
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
				Intent in = new Intent(getApplicationContext(),
						MainActivity.class);
				SharedPreferences prefe = getSharedPreferences("user",
						Context.MODE_PRIVATE);
				Editor editor = prefe.edit();
				editor.putString("id", datos.getString("id"));
				editor.putString("username", datos.getString("usuario"));
				editor.putString("useremail", datos.getString("email"));
				editor.commit();
				startActivity(in);
				finish();

			} catch (Exception e) {
				Log.e("ReadCualidadesJSONFeedTask", e.getLocalizedMessage());
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
				Log.e("JSON", "No se ha podido descargar archivo");

			}
		} catch (Exception e) {
			Log.e("readJSONFeed", e.getLocalizedMessage());

		}

		return stringBuilder.toString();
	}

	protected Boolean estaConectado() {
		if (conectadoWifi()) {
			return true;
		} else {
			if (conectadoRedMovil()) {
				return true;
			} else {
				showAlertDialog(Login.this, "Conexion a Internet",
						"Tu Dispositivo necesita una conexion a internet.",
						false);

				return false;

			}
		}
	}

	protected Boolean conectadoWifi() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	protected Boolean conectadoRedMovil() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (info != null) {
				if (info.isConnected()) {
					return true;
				}
			}
		}
		return false;
	}

	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon((status) ? R.drawable.ic_action_accept
				: R.drawable.ic_action_cancel);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				claseprincipal.principal.finish();
				finish();
			}

		});
		alertDialog.show();

	}

}
