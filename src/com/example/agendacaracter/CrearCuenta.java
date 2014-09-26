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
	// AlertDialog alert;
	Button btnCrearCuenta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_cuenta);

		Typeface tfHelveticaLTStdCond = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-Cond.otf");

		Typeface HelveticaBoldTypeFace = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaLTStd-BoldCond.otf");

		txtEmail = (EditText) findViewById(R.id.txt_email);
		txtEmail.setTypeface(tfHelveticaLTStdCond);

		txtUsuario = (EditText) findViewById(R.id.txt_username);
		txtUsuario.setTypeface(tfHelveticaLTStdCond);

		txtPass1 = (EditText) findViewById(R.id.txt_pass_01);
		txtPass1.setTypeface(tfHelveticaLTStdCond);

		txtPass2 = (EditText) findViewById(R.id.txt_pass_02);
		txtPass2.setTypeface(tfHelveticaLTStdCond);

		btnCrearCuenta = (Button) findViewById(R.id.btn_crear_cuenta);
		btnCrearCuenta.setTypeface(HelveticaBoldTypeFace);

		btnCrearCuenta.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_crear_cuenta:
			String usuario = txtUsuario.getText().toString();
			String email = txtEmail.getText().toString();
			String pass1 = txtPass1.getText().toString();
			String pass2 = txtPass2.getText().toString();
			StringBuilder msg = new StringBuilder();
			boolean con = true;
			String patron = "([a-z]|[A-Z]|\\s|[0-9])+";
			if (Val.isVacio(usuario, email, pass1, pass2)) {
				if (!Val.isEmailValid(email)) {
					msg.append("Email Incorrecto" + "\n");
					con = false;
				}
				if (!Val.isPasswordEquals(pass1, pass2)) {
					msg.append("Las contraseñas no coinciden");
					con = false;
				}
				if (!usuario.matches(patron) || !pass1.matches(patron)
						|| !pass2.matches(patron)) {

					if (!usuario.matches(patron)) {
						txtUsuario.setText("");
					}
					if (!pass1.matches(patron)) {
						txtPass1.setText("");

					}
					if (!pass2.matches(patron)) {
						txtPass2.setText("");

					}
					if (!usuario.matches(patron) && !pass1.matches(patron)
							&& !pass2.matches(patron)) {
						txtUsuario.setText("");
						txtPass1.setText("");
						txtPass2.setText("");

					}
					msg.append("No se permiten caracteres especiales");
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
					Toast.makeText(getApplicationContext(),
							"El nombre de usuario ya existe",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Toast.makeText(
						getApplicationContext(),
						"CrearCuenta: Error Interno -> onPostExecute. "
								+ e.getMessage(), Toast.LENGTH_SHORT).show();
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
				Toast.makeText(
						this,
						"CrearCuenta: Error Interno -> readJSONFeed. Status Code",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(
					this,
					"CrearCuenta: Error Interno -> readJSONFeed."
							+ e.getLocalizedMessage(), Toast.LENGTH_SHORT)
					.show();

		}

		return res.toString();
	}

}
