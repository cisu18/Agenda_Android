package com.example.agendacaracter;

import com.example.reutilizables.Val;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CrearCuenta extends Activity implements OnClickListener {
	EditText txtEmail;
	EditText txtUsuario;
	EditText txtPass1;
	EditText txtPass2;
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);

		Typeface miPropiaTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		txtEmail = (EditText) findViewById(R.id.txt_Email);
		txtEmail.setTypeface(miPropiaTypeFace);

		txtUsuario = (EditText) findViewById(R.id.txt_UserName);
		txtUsuario.setTypeface(miPropiaTypeFace);

		txtPass1 = (EditText) findViewById(R.id.txt_Pass1);
		txtPass1.setTypeface(miPropiaTypeFace);

		txtPass2 = (EditText) findViewById(R.id.txt_Pass2);
		txtPass2.setTypeface(miPropiaTypeFace);

		Button registrarse = (Button) findViewById(R.id.btnCrearcuenta);
		registrarse.setTypeface(miPropiaTypeFace);
		registrarse.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crear_cuenta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCrearcuenta:
			String usuario = txtUsuario.getText().toString();
			String email = txtEmail.getText().toString();
			String pass1 = txtPass1.getText().toString();
			String pass2 = txtPass2.getText().toString();
			StringBuilder msg = new StringBuilder();
			boolean con = true;

			if (Val.isVacio(usuario, email, pass1, pass2)) {
				if (!Val.isEmailValid(email)) {
					msg.append("Email Incorrecto" + "\n");
					con = false;
				}
				if (!Val.isPasswordEquals(pass1, pass2)) {
					msg.append("Las contraseņas no coinciden");
					con = false;
				}
			} else {
				msg.append("Complete todos los campos");
				con = false;
			}

			if (con) {
				new RegistroUsuarioJSONFeedTask()
						.execute("http://192.168.0.55/Agenda_WS/users/create_user/format/json");
			} else {
				Toast t = Toast.makeText(getApplicationContext(), msg,
						Toast.LENGTH_SHORT);
				t.show();
			}
			break;

		}
	}

	private class RegistroUsuarioJSONFeedTask extends
			AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {

			return readJSONFeed(urls[0]);

		}

		protected void onPostExecute(String result) {

			try {
				JSONObject datos = new JSONObject(result);
				alert = new AlertDialog.Builder(CrearCuenta.this).create();
				alert.setTitle("Mensaje");
				alert.setIcon(R.drawable.ic_action_accept);
				if (result.equals("error")) {
					alert.setMessage("Error al conectar con el servidor.");
					alert.setButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									alert.hide();
								}
							});
				} else if (datos.getString("res").equalsIgnoreCase("ok")) {

					SharedPreferences prefe = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = prefe.edit();
					editor.putString("id", datos.getString("data"));
					editor.commit();

					alert.setMessage("Registro correcto");
					alert.setButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent i = new Intent(
											getApplicationContext(),
											MainActivity.class);
									startActivity(i);
									finish();

									// Toast t =
									// Toast.makeText(getApplicationContext(),prefe.getString("id",""),
									// Toast.LENGTH_SHORT);
									// t.show();
								}
							});
				} else if (datos.getString("res").equalsIgnoreCase("error")) {
					alert.setMessage("El nombre de usuario ya existe");
					alert.setButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									alert.hide();
								}
							});
				}
				alert.show();
			} catch (Exception e) {
				Log.d("onPostExecute", e.getLocalizedMessage());
			}

		}
	}

	public String readJSONFeed(String url) {
		StringBuilder res = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {

			List<NameValuePair> parametros = new ArrayList<NameValuePair>(4);
			parametros.add(new BasicNameValuePair("username", txtUsuario
					.getText().toString()));
			parametros.add(new BasicNameValuePair("password", txtPass2
					.getText().toString()));
			parametros.add(new BasicNameValuePair("email", txtEmail.getText()
					.toString()));
			parametros.add(new BasicNameValuePair("tipo", "1"));
			httpPost.setEntity(new UrlEncodedFormEntity(parametros));
			HttpResponse response = httpClient.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200 || statusCode == 404) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;

				while ((line = reader.readLine()) != null) {
					res.append(line);
				}
				inputStream.close();
			} else {
				res.append("error");
				Log.e("JSON", "No se ha podido recibir archivo");
			}
		} catch (Exception e) {
			Log.e("readJSONFeed", e.getLocalizedMessage());
		}

		return res.toString();
	}

}
