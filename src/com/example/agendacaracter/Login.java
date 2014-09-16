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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class Login extends Activity implements OnClickListener{

	public EditText usuario;
	public EditText contrasenia;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		usuario = (EditText) findViewById(R.id.txv_username_login);
		usuario.setTypeface(miPropiaTypeFace);

		contrasenia = (EditText) findViewById(R.id.txv_pass_login);
		contrasenia.setTypeface(miPropiaTypeFace);

		TextView logincon = (TextView) findViewById(R.id.txv_cabecera);

		logincon.setTypeface(miPropiaTypeFace);

		Button iniciosesion = (Button) findViewById(R.id.btn_iniciar_sesion);
		iniciosesion.setTypeface(miPropiaTypeFace);

		Button crearcuenta = (Button) findViewById(R.id.btn_crear_cuenta);
		crearcuenta.setTypeface(miPropiaTypeFace);

		iniciosesion.setOnClickListener(this);
		crearcuenta.setOnClickListener(this);
		/*usuario.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String c = String.valueOf((char)event.getUnicodeChar());
				String val = usuario.getText().toString();
				if(!Val.isUserValid(c)){					
					usuario.setText(val.substring(0, val.length()-1));
					Log.e("KEY",c);
				}
				return false;
			}
		});*/
		
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

			String us = usuario.getText().toString();
			String cl = contrasenia.getText().toString();
			
			if (us.equals("") || cl.equals("")) {
				showAlertDialog(Login.this, "Ingresar Datos...",
						"Porfavor completa todos los campos...", false);

			} else {
				new ReadUsuarioJSONFeedTask()
						.execute("http://192.168.0.55/Agenda_WS/users/login/format/json");

			}

			break;
		case R.id.btn_crear_cuenta:
			Intent i = new Intent(this, CrearCuenta.class);
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

			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Iniciando Sesion...");
			pDialog.setTitle("Login");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

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
				showAlertDialog(Login.this, "Datos Incorrectos...",
						"La contraseña o usuario no son validos...", false);
			}
			pDialog.dismiss();
		}

	}

	public String readJSONFeed(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);

		try {
			String username = usuario.getText().toString();
			String password = contrasenia.getText().toString();

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

	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon((status) ? R.drawable.ic_action_accept
				: R.drawable.ic_action_cancel);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		alertDialog.show();
	}

}
